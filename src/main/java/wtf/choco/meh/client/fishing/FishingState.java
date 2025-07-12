package wtf.choco.meh.client.fishing;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

import wtf.choco.meh.client.event.HypixelServerEvents;
import wtf.choco.meh.client.event.MEHEvents;
import wtf.choco.meh.client.event.MenuEvents;
import wtf.choco.meh.client.scoreboard.HypixelScoreboard;

public final class FishingState {

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

    private final Object2IntMap<CatchType> catches = new Object2IntOpenHashMap<>(CatchType.values().length);

    private boolean fishing = false;
    private boolean spokenToDockMaster = false;

    public void initialize() {
        MenuEvents.SLOT_ITEM_STACK_CHANGE.register(this::onSlotItemStackChange);
        HypixelServerEvents.FISHING_CATCH.register(this::onCatch);
        MEHEvents.HYPIXEL_SCOREBOARD_REFRESH.register(this::onScoreboardRefresh);
    }

    public int getCaughtCount(CatchType type) {
        return catches.getInt(type);
    }

    public boolean isFishing() {
        return fishing;
    }

    public boolean hasSpokenToDockMaster() {
        return spokenToDockMaster;
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

    private void onCatch(FishingCatch fishingCatch) {
        this.catches.merge(fishingCatch.getType(), 1, Integer::sum);
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
                this.catches.put(catchType, catches);
            } catch (NumberFormatException e) {
                Minecraft.getInstance().player.displayClientMessage(Component.literal("Failed to parse \"" + line.substring(lastSpace + 1, line.length()) + "\"! This is a bug!").withStyle(ChatFormatting.RED), false);
            }
        }

        this.spokenToDockMaster = true;
    }

}
