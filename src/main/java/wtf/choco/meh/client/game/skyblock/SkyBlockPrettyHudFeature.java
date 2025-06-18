package wtf.choco.meh.client.game.skyblock;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import me.shedaniel.autoconfig.AutoConfig;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
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

    private static final record PatternStripper(Pattern pattern, BiConsumer<MatchResult, SkyBlockPrettyHudFeature> onMatch) { }

    private static final PatternStripper[] PATTERNS = {
            new PatternStripper(Pattern.compile("\\s*" + SECTION_CHAR + "c(?<current>\\d+)\\/(?<max>\\d+)\\s*" + ICON_HEART), (result, feature) -> {
                feature.currentHealth = NumberUtils.toInt(result.group("current"), 0);
                feature.maxHealth = NumberUtils.toInt(result.group("max"), 0);
            }),
            new PatternStripper(Pattern.compile("\\s*" + SECTION_CHAR + "a(?<value>\\d+)" + SECTION_CHAR + "a" + ICON_DEFENSE + " Defense\\s*"), (result, feature) -> {
                feature.currentDefense = NumberUtils.toInt(result.group("value"));
            }),
            new PatternStripper(Pattern.compile("\\s*" + SECTION_CHAR + "b(?<current>\\d+)\\/(?<max>\\d+)" + ICON_MANA + " Mana\\s*"), (result, feature) -> {
                feature.currentMana = NumberUtils.toInt(result.group("current"), 0);
                feature.maxMana = NumberUtils.toInt(result.group("max"), 0);
            })
    };

    private boolean ignoreNextUpdate = false;

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
        HudElementRegistry.attachElementAfter(VanillaHudElements.HEALTH_BAR, SkyBlockPrettyHudElement.ID, new SkyBlockPrettyHudElement(this));

        ClientReceiveMessageEvents.MODIFY_GAME.register(this::onModifyMessage);
        HypixelServerEvents.SERVER_LOCATION_CHANGED.register(this::onServerLocationChange);
        ClientPlayConnectionEvents.JOIN.register(this::onJoin);
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

        for (PatternStripper pattern : PATTERNS) {
            messageAsString = pattern.pattern().matcher(messageAsString).replaceAll(result -> {
                if (!ignoreNextUpdate) {
                    pattern.onMatch().accept(result, this);
                }

                return "";
            });
        }

        if (ignoreNextUpdate) {
            this.ignoreNextUpdate = false;
        }

        return Component.literal(messageAsString);
    }

    @SuppressWarnings("unused") // lobby, fromLobby
    private void onServerLocationChange(HypixelServerType serverType, boolean lobby, @Nullable HypixelServerType fromServerType, boolean fromLobby) {
        if (serverType != fromServerType) {
            this.refreshState(serverType == HypixelServerType.SKYBLOCK);
        }
    }

    @SuppressWarnings("unused") // listener, sender, minecraft
    private void onJoin(ClientPacketListener listener, PacketSender sender, Minecraft minecraft) {
        if (isEnabled()) {
            // On first join, attributes aren't yet calculated, so Hypixel sends some preliminary numbers (like default health, defense of only the equipped armor, and reduced mana)
            // We'll just ignore the first message sent to us so it doesn't flicker from high to low between server changes
            this.ignoreNextUpdate = true;
        }
    }

    private void refreshState(boolean hideHealthInformation) {
        GuiExtensions.get(Minecraft.getInstance().gui).setHideHealthInformation(hideHealthInformation);
    }

}
