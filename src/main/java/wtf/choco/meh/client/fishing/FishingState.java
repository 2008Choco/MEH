package wtf.choco.meh.client.fishing;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult.Error;
import com.mojang.serialization.JsonOps;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.event.GuiEvents;
import wtf.choco.meh.client.event.HypixelServerEvents;
import wtf.choco.meh.client.event.MEHClientEntityEvents;
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

    private static final String SUBTITLE_MYTHICAL_FISH_APPEARED = ChatFormatting.GRAY + "A " + ChatFormatting.YELLOW + "Mythical Fish " + ChatFormatting.GRAY + "emerges from the depths!!";
    private static final Pattern PATTERN_MYTHICAL_FISH_NAME = Pattern.compile("^\\[(\\|)+\\]$");

    private final Object2IntMap<CatchType> catches = new Object2IntOpenHashMap<>(CatchType.values().length);

    private boolean fishing = false;
    private boolean spokenToDockMaster = false;
    private Reference<FishingHook> activeFishingHook = new WeakReference<>(null);
    private MythicalFishEntity mythicalFishEntity;

    public void initialize() {
        MenuEvents.SLOT_ITEM_STACK_CHANGE.register(this::onSlotItemStackChange);
        HypixelServerEvents.FISHING_CATCH.register(this::onCatch);
        MEHEvents.HYPIXEL_SCOREBOARD_REFRESH.register(this::onScoreboardRefresh);
        MEHClientEntityEvents.ENTITY_ADD.register(this::onFishingHookAddedToWorld);
        MEHClientEntityEvents.ENTITY_REMOVE.register(this::onFishingHookRemovedFromWorld);
        GuiEvents.TITLE_RENDER.register(this::onReceiveMythicalFishTitle);
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

    public Reference<FishingHook> getActiveFishingHook() {
        return activeFishingHook;
    }

    @Nullable
    public MythicalFishEntity getMythicalFishEntity() {
        return mythicalFishEntity;
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

    private void onFishingHookAddedToWorld(Entity entity) {
        if (entity instanceof FishingHook hook && hook.getOwner() == Minecraft.getInstance().player) {
            this.activeFishingHook = new WeakReference<>(hook);
        }
    }

    @SuppressWarnings("unused") // reason
    private void onFishingHookRemovedFromWorld(Entity entity, RemovalReason reason) {
        if (entity instanceof FishingHook fishingHook && activeFishingHook.refersTo(fishingHook)) {
            this.activeFishingHook.clear();
        }

        if (mythicalFishEntity != null && entity instanceof ArmorStand armorStand && mythicalFishEntity.armorStand().refersTo(armorStand)) {
            HypixelServerEvents.FISHING_MYTHICAL_FISH_DISAPPEAR.invoker().onDisappear(mythicalFishEntity);
            this.mythicalFishEntity = null;
        }
    }

    @SuppressWarnings("unused") // fadeInTicks, stayTicks, fadeOutTicks, titleTicks
    private boolean onReceiveMythicalFishTitle(Component title, @Nullable Component subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks, int titleTicks) {
        if (mythicalFishEntity != null) {
            return true;
        }

        if (subtitle == null || !subtitle.getString().equals(SUBTITLE_MYTHICAL_FISH_APPEARED)) {
            return true;
        }

        MythicalFishEntity entity = findNearbyMythicalFish();
        if (entity == null) {
            return true;
        }

        this.mythicalFishEntity = entity;
        HypixelServerEvents.FISHING_MYTHICAL_FISH_APPEAR.invoker().onAppear(entity);
        return true;
    }

    private MythicalFishEntity findNearbyMythicalFish() {
        if (activeFishingHook.refersTo(null)) {
            return null;
        }

        FishingHook fishingHook = activeFishingHook.get();
        List<ArmorStand> entities = fishingHook.level().getEntitiesOfClass(ArmorStand.class, fishingHook.getBoundingBox().inflate(5), entity -> {
            Component name = entity.getCustomName();
            return name != null && PATTERN_MYTHICAL_FISH_NAME.matcher(name.getString()).matches();
        });
        entities.sort(Comparator.comparing(entity -> entity.distanceToSqr(fishingHook)));

        if (entities.isEmpty()) {
            return null;
        }

        ArmorStand mythicalFishArmorStand = entities.get(0);
        ItemStack itemStack = mythicalFishArmorStand.getItemBySlot(EquipmentSlot.HEAD);
        MythicalFishType mythicalFishType = MythicalFishType.getByItemStack(itemStack);
        if (mythicalFishType == null) {
            String serializedItemStack = ItemStack.CODEC.encode(itemStack, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).mapOrElse(JsonElement::toString, Error::message);
            Minecraft.getInstance().player.displayClientMessage(Component.literal("Couldn't determine the MythicalFishType of the active Mythical Fish! ItemStack: " + serializedItemStack), false);
        }

        return new MythicalFishEntity(new WeakReference<>(mythicalFishArmorStand), mythicalFishType);
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
