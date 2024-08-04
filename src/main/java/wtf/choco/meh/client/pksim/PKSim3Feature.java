package wtf.choco.meh.client.pksim;

import it.unimi.dsi.fastutil.ints.IntIntPair;

import java.text.NumberFormat;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.event.ClientScoreboardEvents;
import wtf.choco.meh.client.feature.Feature;

public class PKSim3Feature implements Feature {

    private int level = -1;
    private int prestige = -1;
    private int gems = -1;
    private int coins = -1;
    private int currentExperience = -1;
    private int requiredExperience = -1;
    private boolean allDataExtracted = false;

    private boolean pkSim = false;

    public PKSim3Feature() {
        /*
         * Hypixel is extremely smart with their scoreboards...
         *
         * For 1.13 clients they abuse teams' prefixes and suffixes for scoreboard entries as the entries for their
         * scoreboards. By adding "empty" entries into the scoreboard (really, hidden entries with different format
         * codes), they can assign a team to each entry and set the team's prefix and suffix instead of trying to
         * remove and re-add a new entry when they want to update the scoreboard. This prevents any flickering because
         * the entry never leaves the scoreboard, but the team prefix and suffix updates in real time!
         *
         * Albeit this is a pain in the ass to work with on the client, but extremely smart on their part!
         */
        ClientScoreboardEvents.ADD_TEAM.register((scoreboard, teamName, displayName, prefix, suffix, visibility, collisionRule, color) -> onTeamUpdate(join(prefix, suffix)));
        ClientScoreboardEvents.UPDATE_TEAM.register((scoreboard, team, displayName, prefix, suffix, visibility, collisionRule, color) -> onTeamUpdate(join(prefix, suffix)));
        ClientPlayConnectionEvents.JOIN.register(this::onJoinServer);
    }

    private boolean isOnPKSim3() {
        return pkSim;
    }

    private void onTeamUpdate(Component textComponent) {
        String text = ChatFormatting.stripFormatting(textComponent.getString());
        if (text == null || text.isBlank()) {
            return;
        }

        if (!pkSim && text.equals(PKSimUtils.SCOREBOARD_IDENTIFIER)) {
            this.pkSim = true;

            Minecraft client = Minecraft.getInstance();
            client.player.sendSystemMessage(Component.literal("Welcome to Parkour Simulator v3.5!").withStyle(ChatFormatting.GREEN));
            this.checkIsAllExtractedAndMessageOnChange();
            return;
        }

        int level = PKSimUtils.extractLevel(text);
        if (level > -1) {
            this.level = level;
            MEHClient.LOGGER.info("Found level! " + level);
            this.checkIsAllExtractedAndMessageOnChange();
            return;
        }

        int prestige = PKSimUtils.extractPrestige(text);
        if (prestige > -1) {
            this.prestige = prestige;
            MEHClient.LOGGER.info("Found prestige! " + prestige);
            this.checkIsAllExtractedAndMessageOnChange();
            return;
        }

        int gems = PKSimUtils.extractGems(text);
        if (gems > -1) {
            this.gems = gems;
            MEHClient.LOGGER.info("Found gems! " + gems);
            this.checkIsAllExtractedAndMessageOnChange();
            return;
        }

        int coins = PKSimUtils.extractCoins(text);
        if (coins > -1) {
            this.coins = coins;
            MEHClient.LOGGER.info("Found coins! " + coins);
            this.checkIsAllExtractedAndMessageOnChange();
            return;
        }

        IntIntPair experience = PKSimUtils.extractExperience(text);
        if (experience != null) {
            this.currentExperience = experience.firstInt();
            this.requiredExperience = experience.secondInt();
            MEHClient.LOGGER.info("Found experience! " + currentExperience + "/" + requiredExperience);
            this.checkIsAllExtractedAndMessageOnChange();
            return;
        }
    }

    // Just assume that you're always joining a new server when switching proxied servers and reset the data. It can be re-parsed
    @SuppressWarnings("unused")
    private void onJoinServer(ClientPacketListener handler, PacketSender sender, Minecraft client) {
        this.pkSim = false;
        this.level = -1;
        this.prestige = -1;
        this.gems = -1;
        this.coins = -1;
        this.currentExperience = -1;
        this.requiredExperience = -1;
        this.allDataExtracted = false;
    }

    private void checkIsAllExtractedAndMessageOnChange() {
        if (allDataExtracted) {
            return;
        }

        boolean before = allDataExtracted;
        this.allDataExtracted = (level >= 0 && prestige >= 0 && gems >= 0 && coins >= 0 && currentExperience >= 0 && requiredExperience >= 0);
        if (!before && allDataExtracted) {
            Minecraft client = Minecraft.getInstance();
            client.player.sendSystemMessage(Component.literal("You are currently ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("Level " + level).withStyle(ChatFormatting.AQUA))
                    .append(Component.literal(" (").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(NumberFormat.getNumberInstance().format(currentExperience) + "/" + NumberFormat.getNumberInstance().format(requiredExperience)).withStyle(ChatFormatting.AQUA))
                    .append(Component.literal("), at Prestige " + prestige + " with ").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(NumberFormat.getNumberInstance().format(coins) + " Coins").withStyle(ChatFormatting.YELLOW))
                    .append(Component.literal(" and ").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(NumberFormat.getNumberInstance().format(gems) + " Gems").withStyle(ChatFormatting.RED))
                    .append(Component.literal("!").withStyle(ChatFormatting.GRAY))
            );
        }
    }

    @Override
    public boolean isEnabled() {
        return true; // TODO: Config option
    }

    private Component join(Component... components) {
        if (components.length == 1) {
            return components[0] != null ? components[0] : Component.empty();
        }

        Component firstComponent = components[0];
        if (firstComponent == null) {
            return Component.empty();
        }

        MutableComponent result = firstComponent.copy();

        for (int i = 1; i < components.length; i++) {
            Component component = components[i];
            if (component == null) {
                continue;
            }

            result = result.append(component);
        }

        return result;
    }

}
