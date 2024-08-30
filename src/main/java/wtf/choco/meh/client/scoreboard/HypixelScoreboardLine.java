package wtf.choco.meh.client.scoreboard;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

enum HypixelScoreboardLine {

    LINE_15('!'), // Top line
    LINE_14('z'),
    LINE_13('y'),
    LINE_12('x'),
    LINE_11('w'),
    LINE_10('v'),
    LINE_9('u'),
    LINE_8('t'),
    LINE_7('s'),
    LINE_6('q'),
    LINE_5('p'),
    LINE_4('j'),
    LINE_3('i'),
    LINE_2('h'),
    LINE_1('g'); // Bottom line

    private static final Int2ObjectMap<HypixelScoreboardLine> BY_LINE_NUMBER = new Int2ObjectOpenHashMap<>();
    private static final Char2ObjectMap<HypixelScoreboardLine> BY_CHARACTER = new Char2ObjectOpenHashMap<>();

    static {
        for (HypixelScoreboardLine line : HypixelScoreboardLine.values()) {
            BY_LINE_NUMBER.put(line.lineNumber, line);
            BY_CHARACTER.put(line.character, line);
        }
    }

    private final char character;
    private final int lineNumber;

    private HypixelScoreboardLine(char character) {
        this.character = character;
        this.lineNumber = 15 - ordinal();
    }

    static HypixelScoreboardLine getByLineNumber(int lineNumber) {
        return BY_LINE_NUMBER.get(lineNumber);
    }

    static HypixelScoreboardLine getByCharacter(char character) {
        return BY_CHARACTER.get(character);
    }

}
