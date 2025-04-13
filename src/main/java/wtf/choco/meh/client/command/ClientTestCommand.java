package wtf.choco.meh.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.feature.Features;
import wtf.choco.meh.client.server.HypixelServerState;

public final class ClientTestCommand {

    private ClientTestCommand() { }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("mehtest")
            .then(ClientCommandManager.literal("party")
                .then(ClientCommandManager.literal("refresh").executes(ClientTestCommand::refreshParty))
                .then(ClientCommandManager.literal("delete").executes(ClientTestCommand::deleteParty))
            )
        );
    }

    private static int refreshParty(CommandContext<FabricClientCommandSource> context) {
        HypixelServerState serverState = MEHClient.getInstance().getHypixelServerState();
        if (!serverState.isConnectedToHypixel()) {
            context.getSource().sendError(Component.literal("You are not connected to Hypixel!").withStyle(ChatFormatting.RED));
            return 0;
        }

        if (!Features.PARTY_MANAGER.isEnabled()) {
            context.getSource().sendError(Component.literal("The party manager feature is currently disabled. Can't query for party.").withStyle(ChatFormatting.RED));
            return 0;
        }

        context.getSource().sendFeedback(Component.literal("Refreshing your party..."));
        Features.PARTY_MANAGER.refreshParty().whenComplete((ignore, e) -> {
            if (e != null) {
                context.getSource().sendError(Component.literal("Couldn't fetch party data. Check client logs for more information!").withStyle(ChatFormatting.RED));
                e.printStackTrace();
                return;
            }

            context.getSource().sendFeedback(Component.literal("Done!").withStyle(ChatFormatting.GREEN));
        });

        return 1;
    }

    private static int deleteParty(CommandContext<FabricClientCommandSource> context) {
        HypixelServerState serverState = MEHClient.getInstance().getHypixelServerState();
        if (!serverState.isConnectedToHypixel()) {
            context.getSource().sendError(Component.literal("You are not connected to Hypixel!").withStyle(ChatFormatting.RED));
            return 0;
        }

        if (!Features.PARTY_MANAGER.isEnabled()) {
            context.getSource().sendError(Component.literal("The party manager feature is currently disabled. Can't delete party.").withStyle(ChatFormatting.RED));
            return 0;
        }

        if (Features.PARTY_MANAGER.deleteCachedParty()) {
            context.getSource().sendFeedback(Component.literal("Local party cache successfully deleted.").withStyle(ChatFormatting.GREEN));
        } else {
            context.getSource().sendFeedback(Component.literal("No party in the local cache.").withStyle(ChatFormatting.RED));
        }

        return 1;
    }

}
