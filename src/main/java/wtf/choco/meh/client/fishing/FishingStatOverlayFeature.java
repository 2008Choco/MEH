package wtf.choco.meh.client.fishing;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.List;
import java.util.Optional;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.ConfigFishingStatOverlay;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.HypixelServerEvents;
import wtf.choco.meh.client.event.MEHEvents;
import wtf.choco.meh.client.event.MenuEvents;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.gui.FishingStatsHudElement;
import wtf.choco.meh.client.scoreboard.HypixelScoreboard;
import wtf.choco.meh.client.server.HypixelServerType;

public final class FishingStatOverlayFeature extends Feature {

    // TODO: Maybe use /fishingstats output instead of requiring speaking to the Dock Master

    private static final String FISHING_MENU_TITLE = "Fishing Menu";
    private static final int STAT_ITEM_SLOT = 49;
    private static final int LORE_STATS_START_INDEX = 7;
    private static final CatchType[] LORE_STAT_ORDER = {
            CatchType.FISH,
            CatchType.JUNK,
            CatchType.TREASURE,
            CatchType.PLANTS,
            CatchType.CREATURES,
            CatchType.SPECIAL_FISH,
            CatchType.MYTHICAL_FISH
    };

    private boolean fishing = false;
    private final Object2IntMap<CatchType> catches = new Object2IntOpenHashMap<>(CatchType.values().length);

    private final FishingStatsHudElement fishingStatsElement = new FishingStatsHudElement(this);

    public FishingStatOverlayFeature(MEHClient mod) {
        super(mod);
    }

    @Override
    protected boolean isFeatureEnabled(MEHConfig config) {
        // Always render in a development environment
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            return true;
        }

        Optional<HypixelServerType> serverType = getMod().getHypixelServerState().getServerLocationProvider().getServerType();
        return serverType.isPresent() && serverType.get() == HypixelServerType.MAIN_LOBBY;
    }

    public boolean isDisplayConditionMet() {
        ConfigFishingStatOverlay overlayConfig = MEHClient.getConfig().getMainLobbyFishingConfig().getFishingStatOverlay();

        boolean fishing = overlayConfig.isEnabledWhenFishing() == this.fishing;
        boolean holdingRod = overlayConfig.isEnabledWhenHoldingRod() == isHoldingFishingRod();

        return overlayConfig.getOperator().apply(fishing, holdingRod);
    }

    private boolean isHoldingFishingRod() {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null && player.getInventory().getSelectedItem().getItem() == Items.FISHING_ROD;
    }

    public int getCaught(CatchType type) {
        return catches.getInt(type);
    }

    @Override
    protected void registerListeners() {
        HudElementRegistry.addLast(FishingStatsHudElement.ID, fishingStatsElement);

        MenuEvents.SLOT_ITEM_STACK_CHANGE.register(this::onSlotItemStackChange);
        HypixelServerEvents.FISHING_GENERIC_CATCH.register((catchType, name) -> onCatch(catchType));
        HypixelServerEvents.FISHING_SPECIAL_TREASURE_CATCH.register((catchType, name, quantity) -> onCatch(catchType));
        HypixelServerEvents.FISHING_MYTHICAL_FISH_CATCH.register((catchType, fishType, weight) -> onCatch(catchType));
        MEHEvents.HYPIXEL_SCOREBOARD_REFRESH.register(this::onScoreboardRefresh);
    }

    @SuppressWarnings("unused") // menu
    private void onSlotItemStackChange(AbstractContainerMenu menu, Slot slot, ItemStack itemStack) {
        if (slot.index != STAT_ITEM_SLOT) {
            return;
        }

        Screen screen = Minecraft.getInstance().screen;
        if (screen != null && screen.getTitle().getString().equals(FISHING_MENU_TITLE)) {
            this.extractStats(itemStack);
        }
    }

    private void onCatch(CatchType catchType) {
        this.catches.merge(catchType, 1, Integer::sum);
    }

    private void onScoreboardRefresh(HypixelScoreboard scoreboard) {
        this.fishing = scoreboard.containsTextOnAnyLine("Fish Caught");
    }

    private void extractStats(ItemStack itemStack) {
        ItemLore itemLore = itemStack.get(DataComponents.LORE);
        if (itemLore == null) {
            return;
        }

        List<Component> lines = itemLore.lines();
        for (int i = 0; i < LORE_STAT_ORDER.length; i++) {
            int lineIndex = LORE_STATS_START_INDEX + i;
            if (lineIndex >= lines.size()) {
                break;
            }

            CatchType catchType = LORE_STAT_ORDER[i];
            String line = lines.get(lineIndex++).getString();
            int lastSpace = line.lastIndexOf(' ');

            try {
                int catches = Integer.parseInt(line.substring(lastSpace + 1, line.length()).replace(",", "").replace(".", ""));
                FishingStatOverlayFeature.this.catches.put(catchType, catches);
            } catch (NumberFormatException e) {
                Minecraft.getInstance().player.displayClientMessage(Component.literal("Failed to parse \"" + line.substring(lastSpace + 1, line.length()) + "\"! This is a bug!").withStyle(ChatFormatting.RED), false);
            }
        }
    }

}
