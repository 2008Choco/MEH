package wtf.choco.meh.client.scoreboard;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.event.MEHEvents;

public final class HypixelScoreboard {

    static {
        ClientTickEvents.END_WORLD_TICK.register(level -> {
            HypixelScoreboard scoreboard = MEHClient.getInstance().getHypixelScoreboard();
            if (scoreboard != null) {
                scoreboard.onTick(level);
            }
        });
    }

    /*
     * Hypixel is extremely smart with their scoreboards...
     *
     * For 1.13 clients they abuse teams' prefixes and suffixes for scoreboard entries as the entries for their
     * scoreboards. By adding "empty" entries into the scoreboard (really, hidden entries with different format
     * codes, as seen below), they can assign a team to each entry and set the team's prefix and suffix instead
     * of trying to remove and re-add a new entry when they want to update the scoreboard. This prevents any
     * flickering because the entry never leaves the scoreboard, but the team prefix and suffix updates in real
     * time!
     *
     * Albeit this is a pain in the ass to work with on the client, but extremely smart on their part!
     */

    private static enum Line {

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

        private static final Int2ObjectMap<Line> BY_LINE_NUMBER = new Int2ObjectOpenHashMap<>();
        private static final Char2ObjectMap<Line> BY_CHARACTER = new Char2ObjectOpenHashMap<>();

        static {
            for (Line line : Line.values()) {
                BY_LINE_NUMBER.put(line.lineNumber, line);
                BY_CHARACTER.put(line.character, line);
            }
        }

        private final char character;
        private final int lineNumber;

        private Line(char character) {
            this.character = character;
            this.lineNumber = 15 - ordinal();
        }

    }

    private int tick = 0;
    private int autoRefreshInterval = 0;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<Line, String> entries = new EnumMap<>(Line.class);
    private final DisplaySlot slot;

    public HypixelScoreboard(DisplaySlot slot) {
        this.slot = slot;
    }

    @Nullable
    public String getLine(int lineNumber) {
        Line line = Line.BY_LINE_NUMBER.get(lineNumber);
        if (line == null) {
            throw new IllegalArgumentException("No line exists for line number " + lineNumber);
        }

        this.lock.readLock().lock();
        String text;
        try {
            text = entries.getOrDefault(line, null);
        } finally {
            this.lock.readLock().unlock();
        }

        return text;
    }

    public int getMaxLine() {
        return entries.size();
    }

    public void setAutoRefreshInterval(int refreshIntervalTicks) {
        this.autoRefreshInterval = refreshIntervalTicks;
    }

    public int getAutoRefreshInterval() {
        return autoRefreshInterval;
    }

    public void refresh(ClientLevel level) {
        this.lock.writeLock().lock();
        try {
            this.entries.clear();

            Scoreboard scoreboard = level.getScoreboard();
            Objective objective = scoreboard.getDisplayObjective(slot);
            if (objective == null) {
                return;
            }

            Collection<PlayerScoreEntry> scores = scoreboard.listPlayerScores(objective);
            for (PlayerScoreEntry score : scores) {
                String entry = score.owner();
                if (entry.length() != 2) {
                    continue;
                }

                char character = entry.charAt(1);
                Line line = Line.BY_CHARACTER.get(character);
                if (line == null) {
                    continue;
                }

                PlayerTeam team = scoreboard.getPlayersTeam(entry);
                if (team == null) {
                    continue;
                }

                String text = team.getPlayerPrefix().getString() + team.getPlayerSuffix().getString();
                this.entries.put(line, text);
            }
        } finally {
            this.lock.writeLock().unlock();
        }

        MEHEvents.HYPIXEL_SCOREBOARD_REFRESH.invoker().onRefresh(this);
    }

    public void onTick(ClientLevel level) {
        if (autoRefreshInterval <= 0 || level == null) {
            return;
        }

        if (tick++ % autoRefreshInterval != 0) {
            return;
        }

        this.refresh(level);
    }

}
