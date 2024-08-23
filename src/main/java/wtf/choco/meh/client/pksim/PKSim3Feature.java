package wtf.choco.meh.client.pksim;

import it.unimi.dsi.fastutil.ints.IntIntPair;

import java.text.NumberFormat;
import java.time.Duration;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.event.MEHEvents;
import wtf.choco.meh.client.event.PKSimEvents;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.pksim.notification.TitleNotificationHandler;
import wtf.choco.meh.client.pksim.render.PKSimHUDOverlay;
import wtf.choco.meh.client.scoreboard.HypixelScoreboard;

public final class PKSim3Feature extends Feature {

    private int level = -1;
    private int prestige = -1;
    private int gems = -1;
    private int coins = -1;
    private int currentExperience = -1;
    private int requiredExperience = -1;

    private int playerId = -1;
    private Duration playTime = null;
    private int questProgress = -1;
    private int questRequirement = -1;

    private boolean pkSim = false;
    private boolean sentWelcomeMessage = false;

    private final MEHClient mod;
    private final PKSimHUDOverlay hudOverlay;

    @SuppressWarnings("resource") // Minecraft#getInstance()
    public PKSim3Feature(MEHClient mod) {
        this.mod = mod;

        new TitleNotificationHandler(mod, this);

        this.hudOverlay = new PKSimHUDOverlay(mod, this);
        HudRenderCallback.EVENT.register(hudOverlay::render);
        ClientTickEvents.END_CLIENT_TICK.register(hudOverlay::tick);

        ClientPlayConnectionEvents.JOIN.register(this::onJoinServer);
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> overlay ? onActionBar(message) : onChatMessage(message));
        MEHEvents.HYPIXEL_SCOREBOARD_REFRESH.register(this::onScoreboardRefresh);

        // Debug event listeners
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            PKSimEvents.PARKOUR_COMPLETE.register(() -> Minecraft.getInstance().player.sendSystemMessage(Component.literal("Congratulations on completing a parkour!")));
            PKSimEvents.EXPERIENCE_CHANGE.register((fromExperience, toExperience, fromRequiredExperience, toRequiredExperience, reason) -> Minecraft.getInstance().player.sendSystemMessage(
                Component.literal("Your experience was changed from " + NumberFormat.getNumberInstance().format(fromExperience) + " to " + NumberFormat.getNumberInstance().format(toExperience) + "! Reason: " + reason.name()))
            );
            PKSimEvents.LEVEL_CHANGE.register((fromLevel, toLevel, reason) -> Minecraft.getInstance().player.sendSystemMessage(
                Component.literal("Your level changed from " + fromLevel + " to " + toLevel + "! Reason: " + reason.name()))
            );
            PKSimEvents.COIN_CHANGE.register((fromCoins, toCoins, reason) -> Minecraft.getInstance().player.sendSystemMessage(
                Component.literal("Your coins have changed from " + fromCoins + " to " + toCoins + "! Reason: " + reason.name())
            ));
        }
    }

    public boolean isOnPKSim3() {
        return pkSim || FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    public int getPrestige() {
        return prestige;
    }

    public void setGems(int gems) {
        this.gems = gems;
    }

    public int getGems() {
        return gems;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }

    public void setCurrentExperience(int currentExperience) {
        this.currentExperience = currentExperience;
    }

    public int getCurrentExperience() {
        return currentExperience;
    }

    public void setRequiredExperience(int requiredExperience) {
        this.requiredExperience = requiredExperience;
    }

    public int getRequiredExperience() {
        return requiredExperience;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Duration getPlayTime() {
        return playTime;
    }

    public int getQuestProgress() {
        return questProgress;
    }

    public int getQuestRequirement() {
        return questRequirement;
    }

    public PKSimHUDOverlay getHUDOverlay() {
        return hudOverlay;
    }

    // Just assume that you're always joining a new server when switching proxied servers and reset the data. It can be re-parsed
    @SuppressWarnings("unused")
    private void onJoinServer(ClientPacketListener handler, PacketSender sender, Minecraft client) {
        if (!mod.isConnectedToHypixel() || !isEnabled()) {
            return;
        }

        this.level = -1;
        this.prestige = -1;
        this.gems = -1;
        this.coins = -1;
        this.currentExperience = -1;
        this.requiredExperience = -1;

        this.playerId = -1;
        this.playTime = null;
        this.questProgress = -1;
        this.questRequirement = -1;

        this.pkSim = false;
        this.sentWelcomeMessage = false;

        this.hudOverlay.clearPotionBuffDurations();
    }

    private void onScoreboardRefresh(HypixelScoreboard scoreboard) {
        if (!mod.isConnectedToHypixel() || !isEnabled()) {
            return;
        }

        if (!pkSim) {
            this.pkSim = PKSimUtils.isPKSimulator(scoreboard);

            // Clearly this isn't PK Sim v3, don't bother trying to parse anything further!
            if (!pkSim) {
                return;
            }
        }

        if (coins == -1) {
            int coins = PKSimUtils.extractCoins(scoreboard);
            if (coins > -1) {
                this.setCoins(coins);
            }
        }

        if (level == -1) {
            int level = PKSimUtils.extractLevel(scoreboard);
            if (level > -1) {
                this.setLevel(level);
            }
        }

        if (prestige == -1) {
            int prestige = PKSimUtils.extractPrestige(scoreboard);
            if (prestige > -1) {
                this.setPrestige(prestige);
            }
        }

        if (gems == -1) {
            int gems = PKSimUtils.extractGems(scoreboard);
            if (gems > -1) {
                this.setGems(gems);
            }
        }

        if (currentExperience == -1 || requiredExperience == -1) {
            IntIntPair experience = PKSimUtils.extractExperience(scoreboard);
            if (experience != null) {
                this.setCurrentExperience(experience.firstInt());
                this.setRequiredExperience(experience.secondInt());
            }
        }

        if (pkSim && !sentWelcomeMessage) {
            this.sentWelcomeMessage = true;

            Minecraft client = Minecraft.getInstance();
            client.player.sendSystemMessage(Component.literal("Welcome to Parkour Simulator v3.5!").withStyle(ChatFormatting.GREEN));
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

    private boolean onActionBar(Component message) {
        if (!mod.isConnectedToHypixel() || !isEnabled() || !isOnPKSim3()) {
            return true;
        }

        ActionBarData data = PKSimUtils.extractActionBarData(ChatFormatting.stripFormatting(message.getString()));
        if (data == null) {
            return true;
        }

        this.playerId = data.playerId();
        this.playTime = data.playTime();
        this.questProgress = data.questProgress();
        this.questRequirement = data.questRequirement();

        return false;
    }

    private boolean onChatMessage(Component message) {
        return true; // TODO: Hide unnecessary notifications (like level up notifications)
    }

    @Override
    public boolean isEnabled() {
        return MEHClient.getConfig().isParkourSimulatorEnabled();
    }

}
