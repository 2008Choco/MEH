package wtf.choco.meh.client.chat.emote;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import static net.minecraft.ChatFormatting.*;
import static net.minecraft.network.chat.Component.literal;

/**
 * Chat emotes that are recognized and supported by Hypixel's chat system.
 */
public enum HypixelChatEmote implements ChatEmote {

    // MVP++ required
    HEART("<3", "❤", RED),
    STAR(":star:", "✮", GOLD),
    YES(":yes:", "✔", GREEN),
    NO(":no:", "✖", RED),
    JAVA(":java:", "☕", AQUA),
    ARROW(":arrow:", "➜", YELLOW),
    SHRUG(":shrug:", "¯\\_(ツ)_/¯", YELLOW),
    TABLE_FLIP(":tableflip:", literal("(╯°□°）╯").withStyle(RED)
            .append(literal("︵").withStyle(WHITE))
            .append(literal(" ┻━┻").withStyle(GRAY))
    ),
    WAVE("o/", "( ﾟ◡ﾟ)/", ChatFormatting.LIGHT_PURPLE),
    COUNTING(":123:", literal("1").withStyle(GREEN)
            .append(literal("2").withStyle(YELLOW))
            .append(literal("3").withStyle(RED))
    ),
    TOTEM(":totem:", literal("☉").withStyle(AQUA)
            .append(literal("_").withStyle(YELLOW))
            .append(literal("☉").withStyle(AQUA))
    ),
    TYPING(":typing:", literal("✎").withStyle(YELLOW)
            .append(literal("...").withStyle(GOLD))
    ),
    MATHS(":maths:", literal("√").withStyle(GREEN)
            .append(literal("(").withStyle(YELLOW, BOLD))
            .append(literal("π").withStyle(GREEN))
            .append(literal("+x").withStyle(GREEN, BOLD))
            .append(literal(")").withStyle(YELLOW, BOLD))
            .append(literal("=").withStyle(GREEN, BOLD))
            .append(literal("L").withStyle(RED, BOLD))
    ),
    SNAIL(":snail:", literal("@").withStyle(YELLOW)
            .append(literal("'").withStyle(GREEN))
            .append(literal("-").withStyle(YELLOW))
            .append(literal("'").withStyle(GREEN))
    ),
    THINKING(":thinking:", literal("(").withStyle(GOLD)
            .append(literal("0").withStyle(GREEN))
            .append(literal(".").withStyle(GOLD))
            .append(literal("o").withStyle(GREEN))
            .append(literal("?").withStyle(RED))
            .append(literal(")").withStyle(GOLD))
    ),
    GIMME(":gimme:", "༼つ◕_◕༽つ", AQUA),
    WIZARD(":wizard:", literal("(").withStyle(YELLOW)
            .append(literal("'").withStyle(AQUA))
            .append(literal("-").withStyle(YELLOW))
            .append(literal("'").withStyle(AQUA))
            .append(literal(")⊃").withStyle(YELLOW))
            .append(literal("━").withStyle(RED))
            .append(literal("☆ﾟ.*･｡ﾟ").withStyle(LIGHT_PURPLE))
    ),
    PVP(":pvp:", "⚔", YELLOW),
    PEACE(":peace:", "✌", GREEN),
    OOF(":oof:", "OOF", RED, BOLD),
    PUFFER(":puffer:", "<('O')>", YELLOW, BOLD),

    // Ranked gifting required
    GLEE_LOWER(5, "^_^", "^_^", GREEN),
    GLEE_UPPER(5, "^-^", "^-^", GREEN),
    CUTE(5, ":cute:", literal("(").withStyle(YELLOW)
            .append(literal("✿").withStyle(GREEN))
            .append(literal("◠‿◠)").withStyle(YELLOW))
    ),
    DAB(20, ":dab:", literal("<").withStyle(LIGHT_PURPLE)
            .append(literal("o").withStyle(YELLOW))
            .append(literal("/").withStyle(LIGHT_PURPLE))
    ),
    YEY(20, ":yey:", "ヽ (◕◡◕) ﾉ", GREEN),
    DJ(50, ":dj:", literal("ヽ").withStyle(BLUE)
            .append(literal("(").withStyle(DARK_PURPLE))
            .append(literal("⌐").withStyle(LIGHT_PURPLE))
            .append(literal("■").withStyle(RED))
            .append(literal("_").withStyle(GOLD))
            .append(literal("■").withStyle(YELLOW))
            .append(literal(")").withStyle(AQUA))
            .append(literal("ノ").withStyle(DARK_AQUA))
            .append(literal("♬").withStyle(BLUE))
    ),
    DOG(50,":dog:", "(ᵔᴥᵔ)", GOLD),
    CAT(100, ":cat:", literal("= ").withStyle(YELLOW)
            .append(literal("＾● ⋏ ●＾").withStyle(AQUA))
            .append(literal(" =").withStyle(YELLOW))
    ),
    EXCITED_WAVE(100, "h/", "ヽ(^◇^*)/", YELLOW),
    SLOTH(200, ":sloth:", literal("(").withStyle(GOLD)
            .append(literal("・").withStyle(DARK_GRAY))
            .append(literal("⊝").withStyle(GOLD))
            .append(literal("・").withStyle(DARK_GRAY))
            .append(literal(")").withStyle(GOLD))
    ),
    SNOW(200, ":snow:", "☃", AQUA);

    private String descriptionKey;

    private final String chatInput;
    private final Component output;
    private final int requiredRankedGifts;

    private HypixelChatEmote(String chatInput, Component output, int requiredRankedGifts) {
        this.chatInput = chatInput;
        this.output = output;
        this.requiredRankedGifts = requiredRankedGifts;
    }

    private HypixelChatEmote(int requiredRankedGifts, String chatInput, Component output) {
        this(chatInput, output, requiredRankedGifts);
    }

    private HypixelChatEmote(int requiredRankedGifts, String chatInput, String output, ChatFormatting... color) {
        this(chatInput, literal(output).withStyle(color), requiredRankedGifts);
    }

    private HypixelChatEmote(String chatInput, Component output) {
        this(chatInput, output, 0);
    }

    private HypixelChatEmote(String chatInput, String output, ChatFormatting... color) {
        this(chatInput, literal(output).withStyle(color), 0);
    }

    @Override
    public String getInputText() {
        return chatInput;
    }

    @Override
    public Component getEmoteDisplayText() {
        return output;
    }

    public int getRequiredRankedGifts() {
        return requiredRankedGifts;
    }

    public boolean requiresRankedGifts() {
        return getRequiredRankedGifts() > 0;
    }

    public boolean requiresMVPPlusPlus() {
        return getRequiredRankedGifts() == 0;
    }

    public String getDescriptionKey() {
        if (descriptionKey == null) {
            this.descriptionKey = "meh.hypixel.emote." + name().toLowerCase();
        }

        return descriptionKey;
    }

}
