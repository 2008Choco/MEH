package wtf.choco.meh.client.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.registry.MEHRegistries;

import static net.minecraft.ChatFormatting.*;
import static net.minecraft.network.chat.Component.literal;

public final class ChatEmotes {

    // MVP++ required
    public static final ChatEmote HEART = register("heart", "<3", "❤", RED);
    public static final ChatEmote STAR = register("star", ":star:", "✮", GOLD);
    public static final ChatEmote YES = register("yes", ":yes:", "✔", GREEN);
    public static final ChatEmote NO = register("no", ":no:", "✖", RED);
    public static final ChatEmote JAVA = register("java", ":java:", "☕", AQUA);
    public static final ChatEmote ARROW = register("arrow", ":arrow:", "➜", YELLOW);
    public static final ChatEmote SHRUG = register("shrug", ":shrug:", "¯\\_(ツ)_/¯", YELLOW);
    public static final ChatEmote TABLE_FLIP = register("table_flip", ":tableflip:", literal("(╯°□°）╯").withStyle(RED)
            .append(literal("︵").withStyle(WHITE))
            .append(literal(" ┻━┻").withStyle(GRAY))
    );
    public static final ChatEmote WAVE = register("wave", "o/", "( ﾟ◡ﾟ)/", ChatFormatting.LIGHT_PURPLE);
    public static final ChatEmote COUNTING = register("counting", ":123:", literal("1").withStyle(GREEN)
            .append(literal("2").withStyle(YELLOW))
            .append(literal("3").withStyle(RED))
    );
    public static final ChatEmote TOTEM = register("totem", ":totem:", literal("☉").withStyle(AQUA)
            .append(literal("_").withStyle(YELLOW))
            .append(literal("☉").withStyle(AQUA))
    );
    public static final ChatEmote TYPING = register("typing", ":typing:", literal("✎").withStyle(YELLOW)
            .append(literal("...").withStyle(GOLD))
    );
    public static final ChatEmote MATHS = register("maths", ":maths:", literal("√").withStyle(GREEN)
            .append(literal("(").withStyle(YELLOW, BOLD))
            .append(literal("π").withStyle(GREEN))
            .append(literal("+x").withStyle(GREEN, BOLD))
            .append(literal(")").withStyle(YELLOW, BOLD))
            .append(literal("=").withStyle(GREEN, BOLD))
            .append(literal("L").withStyle(RED, BOLD))
    );
    public static final ChatEmote SNAIL = register("snail", ":snail:", literal("@").withStyle(YELLOW)
            .append(literal("'").withStyle(GREEN))
            .append(literal("-").withStyle(YELLOW))
            .append(literal("'").withStyle(GREEN))
    );
    public static final ChatEmote THINKING = register("thinking", ":thinking:", literal("(").withStyle(GOLD)
            .append(literal("0").withStyle(GREEN))
            .append(literal(".").withStyle(GOLD))
            .append(literal("o").withStyle(GREEN))
            .append(literal("?").withStyle(RED))
            .append(literal(")").withStyle(GOLD))
    );
    public static final ChatEmote GIMME = register("gimme", ":gimme:", "༼つ◕_◕༽つ", AQUA);
    public static final ChatEmote WIZARD = register("wizard", ":wizard:", literal("(").withStyle(YELLOW)
            .append(literal("'").withStyle(AQUA))
            .append(literal("-").withStyle(YELLOW))
            .append(literal("'").withStyle(AQUA))
            .append(literal(")⊃").withStyle(YELLOW))
            .append(literal("━").withStyle(RED))
            .append(literal("☆ﾟ.*･｡ﾟ").withStyle(LIGHT_PURPLE))
    );
    public static final ChatEmote PVP = register("pvp", ":pvp:", "⚔", YELLOW);
    public static final ChatEmote PEACE = register("peace", ":peace:", "✌", GREEN);
    public static final ChatEmote OOF = register("oof", ":oof:", "OOF", RED, BOLD);
    public static final ChatEmote PUFFER = register("puffer", ":puffer:", "<('O')>", YELLOW, BOLD);

    // Ranked gifting required
    public static final ChatEmote GLEE_LOWER = register("glee_lower", 5, "^_^", "^_^", GREEN);
    public static final ChatEmote GLEE_UPPER = register("glee_upper", 5, "^-^", "^-^", GREEN);
    public static final ChatEmote CUTE = register("cute", 5, ":cute:", literal("(").withStyle(YELLOW)
            .append(literal("✿").withStyle(GREEN))
            .append(literal("◠‿◠)").withStyle(YELLOW))
    );
    public static final ChatEmote DAB = register("dab", 20, ":dab:", literal("<").withStyle(LIGHT_PURPLE)
            .append(literal("o").withStyle(YELLOW))
            .append(literal("/").withStyle(LIGHT_PURPLE))
    );
    public static final ChatEmote YEY = register("yey", 20, ":yey:", "ヽ (◕◡◕) ﾉ", GREEN);
    public static final ChatEmote DJ = register("dj", 50, ":dj:", literal("ヽ").withStyle(BLUE)
            .append(literal("(").withStyle(DARK_PURPLE))
            .append(literal("⌐").withStyle(LIGHT_PURPLE))
            .append(literal("■").withStyle(RED))
            .append(literal("_").withStyle(GOLD))
            .append(literal("■").withStyle(YELLOW))
            .append(literal(")").withStyle(AQUA))
            .append(literal("ノ").withStyle(DARK_AQUA))
            .append(literal("♬").withStyle(BLUE))
    );
    public static final ChatEmote DOG = register("dog", 50, ":dog:", "(ᵔᴥᵔ)", GOLD);
    public static final ChatEmote CAT = register("cat", 100, ":cat:", literal("= ").withStyle(YELLOW)
            .append(literal("＾● ⋏ ●＾").withStyle(AQUA))
            .append(literal(" =").withStyle(YELLOW))
    );
    public static final ChatEmote EXCITED_WAVE = register("excited_wave", 100, "h/", "ヽ(^◇^*)/", YELLOW);
    public static final ChatEmote SLOTH = register("sloth", 200, ":sloth:", literal("(").withStyle(GOLD)
            .append(literal("・").withStyle(DARK_GRAY))
            .append(literal("⊝").withStyle(GOLD))
            .append(literal("・").withStyle(DARK_GRAY))
            .append(literal(")").withStyle(GOLD))
    );
    public static final ChatEmote SNOWMAN = register("snowman", 200, ":snow:", "☃", AQUA);

    private ChatEmotes() { }

    public static void bootstrap() { }

    private static ChatEmote register(String id, int requiredRankedGifts, String chatInput, Component output) {
        ChatEmote emote = new ChatEmote(requiredRankedGifts, chatInput, output);
        ResourceKey<ChatEmote> key = ResourceKey.create(MEHRegistries.CHAT_EMOTES.key(), MEHClient.key(id));
        Registry.register(MEHRegistries.CHAT_EMOTES, key, emote);
        return emote;
    }

    private static ChatEmote register(String id, String chatInput, Component output) {
        return register(id, 0, chatInput, output);
    }

    private static ChatEmote register(String id, int requiredRankedGifts, String chatInput, String output, ChatFormatting... format) {
        return register(id, requiredRankedGifts, chatInput, literal(output).withStyle(format));
    }

    private static ChatEmote register(String id, String chatInput, String output, ChatFormatting... format) {
        return register(id, 0, chatInput, output, format);
    }

}
