package wtf.choco.meh.client.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public final class Components {

    private static final Style STYLE_NON_ITALIC = Style.EMPTY.withItalic(false);
    private static final Map<TextColor, ChatFormatting> COLOR_TO_LEGACY_FORMATTING = Util.make(new HashMap<>(), map -> {
        for (ChatFormatting formatting : ChatFormatting.values()) {
            TextColor color = TextColor.fromLegacyFormat(formatting);
            if (color == null) {
                continue;
            }

            map.put(color, formatting);
        }
    });

    private Components() { }

    public static MutableComponent emptyNonItalic() {
        return Component.empty().setStyle(STYLE_NON_ITALIC);
    }

    public static boolean anyMatch(Component[] components, Component input) {
        boolean dev = FabricLoader.getInstance().isDevelopmentEnvironment();

        for (Component component : components) {
            /*
             * It's difficult to get coloured items in single player "vanilla", so we're going
             * to just ignore colour codes when in a development environment as a quick band-aid patch.
             */
            if ((dev && input.getString().equals(component.getString())) || component.equals(input)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasFormattingAnywhere(Component component, ChatFormatting format) {
        return component.visit((style, content) -> {
            if (format.isColor()) {
                TextColor color = style.getColor();
                if (color != null) {
                    ChatFormatting componentFormatting = COLOR_TO_LEGACY_FORMATTING.get(color);
                    if (componentFormatting == format) {
                        return Optional.of(true);
                    }
                }

                return Optional.empty();
            } else if (format == ChatFormatting.BOLD && style.isBold()
                    || format == ChatFormatting.ITALIC && style.isItalic()
                    || format == ChatFormatting.UNDERLINE && style.isUnderlined()
                    || format == ChatFormatting.STRIKETHROUGH && style.isStrikethrough()
                    || format == ChatFormatting.OBFUSCATED && style.isObfuscated()) {
                return Optional.of(true);
            }

            return Optional.empty();
        }, Style.EMPTY).orElse(false);
    }

    // Taken in part from CraftBukkit, but modified to work a bit more flexibly and in full Mojmaps
    public static String toLegacyText(Component component, char colorChar) {
        if (component == null) {
            return "";
        }

        StringBuilder output = new StringBuilder();

        AtomicBoolean hadFormat = new AtomicBoolean(false);
        component.visit((style, content) -> {
            TextColor color = style.getColor();
            if (color != null) {
                ChatFormatting formatting = COLOR_TO_LEGACY_FORMATTING.get(color);
                if (formatting != null) {
                    output.append(colorChar).append(formatting.getChar());
                }
                hadFormat.set(true);
            } else if (hadFormat.get()) {
                output.append(colorChar).append(ChatFormatting.RESET.getChar());
                hadFormat.set(false);
            }

            if (style.isBold()) {
                output.append(colorChar).append(ChatFormatting.BOLD.getChar());
                hadFormat.set(true);
            }
            if (style.isItalic()) {
                output.append(colorChar).append(ChatFormatting.ITALIC.getChar());
                hadFormat.set(true);
            }
            if (style.isUnderlined()) {
                output.append(colorChar).append(ChatFormatting.UNDERLINE.getChar());
                hadFormat.set(true);
            }
            if (style.isStrikethrough()) {
                output.append(colorChar).append(ChatFormatting.STRIKETHROUGH.getChar());
                hadFormat.set(true);
            }
            if (style.isObfuscated()) {
                output.append(colorChar).append(ChatFormatting.OBFUSCATED.getChar());
                hadFormat.set(true);
            }

            output.append(content);
            return Optional.empty();
        }, component.getStyle());

        return output.toString();
    }

    public static String toLegacyText(Component component) {
        return toLegacyText(component, ChatFormatting.PREFIX_CODE);
    }

    public static Component fromLegacyText(String text, char colorChar) {
        MutableComponent component = null;

        Style style = Style.EMPTY;
        StringBuilder buffer = new StringBuilder();

        boolean nextIsFormat = false;
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);

            // If we're expecting a format code, try to handle that first
            if (nextIsFormat) {
                nextIsFormat = false;

                // If this character that we expected to be a format is, in fact, a format, then update the style accordingly
                ChatFormatting formatting = ChatFormatting.getByCode(character);
                if (formatting != null) {
                    // If we have text in our buffer, we need to flush the buffer and reset our style
                    if (!buffer.isEmpty()) {
                        component = appendAndClearBuffer(component, buffer, style);
                        style = Style.EMPTY;
                    }

                    if (formatting.isColor()) {
                        // If it was a colour, discard all previous formatting
                        style = style.withBold(false).withItalic(false).withUnderlined(false).withStrikethrough(false).withObfuscated(false).withColor(formatting);
                        // But if it was a formatting code, we can append it to an existing style
                    } else if (formatting == ChatFormatting.BOLD) {
                        style = style.withBold(true);
                    } else if (formatting == ChatFormatting.ITALIC) {
                        style = style.withItalic(true);
                    } else if (formatting == ChatFormatting.UNDERLINE) {
                        style = style.withUnderlined(true);
                    } else if (formatting == ChatFormatting.STRIKETHROUGH) {
                        style = style.withStrikethrough(true);
                    } else if (formatting == ChatFormatting.OBFUSCATED) {
                        style = style.withObfuscated(true);
                    }

                    continue;
                } else {
                    // If we were expecting a formatting code but didn't actually get one, we'll append the color character that we skipped in the last iteration
                    buffer.append(colorChar);
                }
            }

            // If the current character is a color character and there's still another character, skip this character and move on
            if (character == colorChar && i < (text.length() - 1)) {
                nextIsFormat = true;
                continue;
            }

            buffer.append(character);
        }

        return appendAndClearBuffer(component, buffer, style);
    }

    private static MutableComponent appendAndClearBuffer(MutableComponent component, StringBuilder buffer, Style style) {
        if (buffer.isEmpty()) {
            return component;
        }

        MutableComponent remaining = Component.literal(buffer.toString()).withStyle(style);
        buffer.delete(0, buffer.length());
        return (component != null) ? component.append(remaining) : remaining;
    }

}
