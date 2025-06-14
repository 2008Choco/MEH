package wtf.choco.meh.client.game.skyblock;

import java.util.Optional;
import java.util.regex.Pattern;

import me.shedaniel.autoconfig.AutoConfig;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;

import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.HypixelServerEvents;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.mixinapi.GuiExtensions;
import wtf.choco.meh.client.server.HypixelServerType;

public final class SkyBlockPrettyHudFeature extends Feature {

    private static final char SECTION_CHAR = ChatFormatting.PREFIX_CODE;
    private static final char ICON_HEART = '\u2764';
    private static final char ICON_DEFENSE = '\u2748';
    private static final char ICON_MANA = '\u270e';

    private static final Pattern PATTERN_HEALTH = Pattern.compile("\\s*" + SECTION_CHAR + "c(?<current>\\d+)\\/(?<max>\\d+)\\s*" + ICON_HEART);
    private static final Pattern PATTERN_DEFENSE = Pattern.compile("\\s*" + SECTION_CHAR + "a(?<value>\\d+)" + SECTION_CHAR + "a" + ICON_DEFENSE + " Defense\\s*");
    private static final Pattern PATTERN_MANA = Pattern.compile("\\s*" + SECTION_CHAR + "b(?<current>\\d+)\\/(?<max>\\d+)" + ICON_MANA + " Mana\\s*");

    private int currentHealth;
    private int maxHealth;
    private int currentDefense;
    private int currentMana;
    private int maxMana;

    public SkyBlockPrettyHudFeature(MEHClient mod) {
        super(mod);
    }

    @Override
    protected boolean isFeatureEnabled(MEHConfig config) {
        if (!config.getSkyBlock().isPrettyHud()) {
            return false;
        }

        // Always render in a development environment
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            return true;
        }

        Optional<HypixelServerType> serverType = getMod().getHypixelServerState().getServerLocationProvider().getServerType();
        return serverType.isPresent() && serverType.get() == HypixelServerType.SKYBLOCK;
    }

    @Override
    protected void registerListeners() {
        HudLayerRegistrationCallback.EVENT.register(drawer -> drawer.attachLayerAfter(IdentifiedLayer.HOTBAR_AND_BARS, new PrettySkyBlockHudLayer(this)));
        ClientReceiveMessageEvents.MODIFY_GAME.register(this::onModifyMessage);
        HypixelServerEvents.SERVER_LOCATION_CHANGED.register(this::onServerLocationChange);
        AutoConfig.getConfigHolder(MEHConfig.class).registerSaveListener((holder, config) -> {
            this.refreshState(getMod().getHypixelServerState().isConnectedToHypixel() && isFeatureEnabled(config));
            return InteractionResult.SUCCESS;
        });
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentDefense() {
        return currentDefense;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    private Component onModifyMessage(Component message, boolean overlay) {
        if (!isEnabled() || !overlay) {
            return message;
        }

        String messageAsString = message.getString();

        messageAsString = PATTERN_HEALTH.matcher(messageAsString).replaceFirst(result -> {
            this.currentHealth = NumberUtils.toInt(result.group("current"), 0);
            this.maxHealth = NumberUtils.toInt(result.group("max"), 0);
            return "";
        });

        messageAsString = PATTERN_DEFENSE.matcher(messageAsString).replaceFirst(result -> {
            this.currentDefense = NumberUtils.toInt(result.group("value"));
            return "";
        });

        messageAsString = PATTERN_MANA.matcher(messageAsString).replaceFirst(result -> {
            this.currentMana = NumberUtils.toInt(result.group("current"), 0);
            this.maxMana = NumberUtils.toInt(result.group("max"), 0);
            return "";
        });

        return Component.literal(messageAsString);
    }

    @SuppressWarnings("unused") // lobby, fromLobby
    private void onServerLocationChange(HypixelServerType serverType, boolean lobby, @Nullable HypixelServerType fromServerType, boolean fromLobby) {
        if (serverType != fromServerType) {
            this.refreshState(serverType == HypixelServerType.SKYBLOCK);
        }
    }

    private void refreshState(boolean hideHealthInformation) {
        GuiExtensions.get(Minecraft.getInstance().gui).setHideHealthInformation(hideHealthInformation);
    }

}
