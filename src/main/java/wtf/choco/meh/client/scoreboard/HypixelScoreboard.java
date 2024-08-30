package wtf.choco.meh.client.scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.event.MEHEvents;

/**
 * A thread-safe auto-refreshing cache of a Hypixel server scoreboard.
 * <p>
 * An instance of HypixelScoreboard can read information from the active scoreboard with
 * {@link #getLine(int)} so long as {@link #refresh(ClientLevel)} has been called at least
 * once. Each call to refresh() will create a snapshot of what's on the scoreboard at the
 * moment of invocation as is seen by the client. To automatically refresh,
 * {@link #setAutoRefreshInterval(int)} can be called with a positive, non-zero integer.
 * <p>
 * This class will only scrape scoreboards specifically implemented by Hypixel. This is not
 * a general purpose scoreboard scraper because Hypixel's implementation is done uniquely
 * with teams rather than true scoreboard entries to avoid flickering when updating lines.
 */
public final class HypixelScoreboard {

    private static final Collection<HypixelScoreboard> LISTENING_SCOREBOARDS = Collections.synchronizedCollection(new ArrayList<>());

    static {
        ClientTickEvents.END_WORLD_TICK.register(level -> {
            for (HypixelScoreboard scoreboard : LISTENING_SCOREBOARDS) {
                scoreboard.onWorldTick(level);
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

    private int tick = 0;
    private int autoRefreshInterval = 0;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<HypixelScoreboardLine, String> entries = new EnumMap<>(HypixelScoreboardLine.class);

    public HypixelScoreboard() {
        LISTENING_SCOREBOARDS.add(this);
    }

    /**
     * Get the text on the given line of the scoreboard (from the top {@literal ->} down).
     * <p>
     * The returned text will be formatted with legacy text as is done by Hypixel's scoreboard. If
     * a stripped version of the text is required, use {@link ChatFormatting#stripFormatting(String)}.
     *
     * @param lineNumber the line number to get where 1 is the top line and 15 is the bottom line
     *
     * @return the line contents, or null if no text is currently known for the given line
     *
     * @throws IllegalArgumentException if {@code lineNumber} is {@literal <=} 0 or exceeds
     * {@link #getMaxLine()}
     */
    @Nullable
    public String getLine(int lineNumber) {
        HypixelScoreboardLine line = HypixelScoreboardLine.getByLineNumber(lineNumber);
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

    /**
     * Get the maximum line that can be fetched from this scoreboard without throwing an exception.
     * The returned number is inclusive and may be passed directly to {@link #getLine(int)}.
     *
     * @return the max line number
     */
    public int getMaxLine() {
        return entries.size();
    }

    /**
     * Set the interval (in ticks) between which the scoreboard's data will be automatically scraped
     * and cached.
     *
     * @param refreshIntervalTicks the amount of time (in ticks) between refreshes, or 0 (or less) to
     * require a manual refresh with {@link #refresh(ClientLevel)}
     */
    public void setAutoRefreshInterval(int refreshIntervalTicks) {
        this.autoRefreshInterval = Math.max(refreshIntervalTicks, 0);
    }

    /**
     * Get the interval (in ticks) between which the scoreboard's data will be automatically scraped
     * and cached.
     *
     * @return the auto refresh interval, or 0 if the scoreboard must be manually refreshed
     */
    public int getAutoRefreshInterval() {
        return autoRefreshInterval;
    }

    /**
     * Refresh this scoreboard's cached data.
     * <p>
     * This method will clear any existing cached entries, scrape all text data from the scoreboard,
     * and cache it for easy access with {@link #getLine(int)}.
     *
     * @param level the client level
     */
    public void refresh(ClientLevel level) {
        this.lock.writeLock().lock();
        try {
            this.entries.clear();

            Scoreboard scoreboard = level.getScoreboard();
            Objective objective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);
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
                HypixelScoreboardLine line = HypixelScoreboardLine.getByCharacter(character);
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

    /**
     * Dispose of this scoreboard. This must be called before this object is destroyed.
     */
    public void dispose() {
        LISTENING_SCOREBOARDS.remove(this);
    }

    private void onWorldTick(ClientLevel level) {
        if (autoRefreshInterval <= 0 || level == null) {
            return;
        }

        if (tick++ % autoRefreshInterval != 0) {
            return;
        }

        this.refresh(level);
    }

}
