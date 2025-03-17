package wtf.choco.meh.client.game.housing;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import java.util.function.Predicate;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.HypixelServerEvents;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.server.HypixelServerType;

public final class HousingAutoNightVisionFeature extends Feature {

    private static final String[] COMMON_NIGHT_VISION_COMMANDS = {
            "nv",
            "nvn",
            "nvis",
            "nightvision",
            "see"
    };

    public HousingAutoNightVisionFeature(MEHClient mod) {
        super(mod, (Predicate<MEHConfig>) config -> config.getHousingConfig().isAutoNightVision());
    }

    @Override
    protected void registerListeners() {
        HypixelServerEvents.SERVER_LOCATION_CHANGED.register(this::onServerLocationChange);
    }

    @SuppressWarnings("unused")
    private void onServerLocationChange(HypixelServerType serverType, boolean lobby, @Nullable HypixelServerType fromServerType, boolean fromLobby) {
        if (!isEnabled()) {
            return;
        }

        if (serverType != HypixelServerType.HOUSING || lobby) {
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        ClientPacketListener connection = player.connection;
        RootCommandNode<SharedSuggestionProvider> rootCommandNode = connection.getCommands().getRoot();

        for (String nightVisionCommandName : COMMON_NIGHT_VISION_COMMANDS) {
            CommandNode<SharedSuggestionProvider> nightVisionCommand = rootCommandNode.getChild(nightVisionCommandName);
            if (nightVisionCommand == null) {
                continue;
            }

            connection.sendCommand(nightVisionCommandName);
            player.displayClientMessage(Component.translatable("meh.housing.auto_night_vision.success", "/" + nightVisionCommandName).withStyle(ChatFormatting.GREEN), false);
            break;
        }
    }

}
