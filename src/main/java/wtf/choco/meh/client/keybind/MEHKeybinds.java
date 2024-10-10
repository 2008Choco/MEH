package wtf.choco.meh.client.keybind;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.InputConstants;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.chat.ChatChannelsFeature;
import wtf.choco.meh.client.chat.EmoteSelectorFeature;

public final class MEHKeybinds {

    public static final String CATEGORY_MEH = "key.categories.meh";

    public static final int KEY_SWITCH_CHANNEL = InputConstants.KEY_TAB;
    public static final int KEY_DELETE_CHANNEL = InputConstants.KEY_MINUS;
    public static final int KEY_EMOTE_SELECTOR = InputConstants.KEY_E;

    public static final KeyMapping SWITCH_CHANNEL;
    public static final KeyMapping SWITCH_CHANNEL_PREVIOUS;
    public static final KeyMapping DELETE_CHANNEL;
    public static final KeyMapping EMOTE_SELECTOR;

    private static final Supplier<Boolean> AMECS_LOADED = Suppliers.memoize(() -> {
        FabricLoader loader = FabricLoader.getInstance();
        return loader.isModLoaded("too_many_shortcuts") || loader.isModLoaded("amecs");
    });

    static {
        SWITCH_CHANNEL = registerPriorityKeybind("switch_channel", InputConstants.Type.KEYSYM, KEY_SWITCH_CHANNEL, true, chatChannelKeybind(ChatChannelsFeature::keybindSwitchChannelNext));
        SWITCH_CHANNEL_PREVIOUS = registerPriorityKeybind("switch_channel_previous", InputConstants.Type.KEYSYM, KEY_SWITCH_CHANNEL, true, true, chatChannelKeybind(ChatChannelsFeature::keybindSwitchChannelPrevious));
        DELETE_CHANNEL = registerPriorityKeybind("delete_channel", InputConstants.Type.KEYSYM, KEY_DELETE_CHANNEL, true, chatChannelKeybind(ChatChannelsFeature::keybindDeleteChannel));
        EMOTE_SELECTOR = registerPriorityKeybind("emote_selector", InputConstants.Type.KEYSYM, KEY_EMOTE_SELECTOR, true, emoteSelectorKeybind(EmoteSelectorFeature::keybindOnToggleEmoteSelector));
    }

    private MEHKeybinds() { }

    public static void init() { }

    public static boolean isAmecsLoaded() {
        return AMECS_LOADED.get();
    }

    @Nullable
    private static KeyMapping registerPriorityKeybind(String keyId, InputConstants.Type type, int key, boolean control, boolean shift, BooleanSupplier onPress) {
        if (!isAmecsLoaded()) {
            return null;
        }

        try {
            ResourceLocation mappingId = ResourceLocation.tryBuild(MEHClient.MOD_ID, keyId);
            KeyMapping keybind = AmecsHook.createAmecsKeybinding(mappingId, type, key, CATEGORY_MEH, control, shift, onPress);
            KeyBindingHelper.registerKeyBinding(keybind);
            return keybind;
        } catch (NoClassDefFoundError e) {
            MEHClient.LOGGER.warn("Tried to register keybind (" + keyId + ") via Amecs API, but Amecs classes were not found?");
            return null;
        }
    }

    private static KeyMapping registerPriorityKeybind(String keyId, InputConstants.Type type, int key, boolean control, BooleanSupplier onPress) {
        return registerPriorityKeybind(keyId, type, key, control, false, onPress);
    }

    private static BooleanSupplier chatChannelKeybind(Predicate<ChatChannelsFeature> keybindCallback) {
        ChatChannelsFeature feature = MEHClient.getInstance().getFeature(ChatChannelsFeature.class);
        return () -> keybindCallback.test(feature);
    }

    private static BooleanSupplier emoteSelectorKeybind(Predicate<EmoteSelectorFeature> keybindCallback) {
        EmoteSelectorFeature feature = MEHClient.getInstance().getFeature(EmoteSelectorFeature.class);
        return () -> keybindCallback.test(feature);
    }

}
