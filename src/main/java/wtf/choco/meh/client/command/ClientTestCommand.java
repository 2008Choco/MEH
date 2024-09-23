package wtf.choco.meh.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import wtf.choco.meh.client.event.ContainerEvents;

public final class ClientTestCommand {

    private static boolean dumpInventoryClicks = false;

    private ClientTestCommand() { }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("mehtest")
            .then(ClientCommandManager.literal("dumpitem").executes(ClientTestCommand::dumpItem))
            .then(ClientCommandManager.literal("dumpinventory").executes(ClientTestCommand::dumpInventory))
        );

        ContainerEvents.PICKUP_ITEM.register((menu, player, slot, itemStack) -> {
            if (!dumpInventoryClicks || Screen.hasAltDown()) {
                return true;
            }

            itemStack.getComponents().forEach(component -> {
                player.sendSystemMessage(Component.literal(component.type().toString()).append(": ").append(component.value().toString()));
            });

            return false;
        });
    }

    private static int dumpItem(CommandContext<FabricClientCommandSource> context) {
        LocalPlayer player = context.getSource().getPlayer();
        ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (itemStack.isEmpty()) {
            context.getSource().sendError(Component.literal("You are not holding an item in hand!"));
            return 0;
        }

        itemStack.getComponents().forEach(component -> {
            context.getSource().sendFeedback(Component.literal(component.type().toString()).append(": ").append(component.value().toString()));
        });
        return 1;
    }

    private static int dumpInventory(CommandContext<FabricClientCommandSource> context) {
        dumpInventoryClicks = !dumpInventoryClicks;

        if (dumpInventoryClicks) {
            context.getSource().sendFeedback(Component.literal("Dump inventory clicks has been ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("ENABLED").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD))
                .append(". Any item you click in an inventory will now dump its contents in chat. Use ")
                .append(Component.literal("/mehtest dumpinventory").withStyle(style -> style
                    .applyFormat(ChatFormatting.YELLOW)
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mehtest dumpinventory"))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to execute.")))
                ))
                .append(" to toggle it off.")
            );
        } else {
            context.getSource().sendFeedback(Component.literal("Dump inventory clicks has been ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal("DISABLED").withStyle(ChatFormatting.RED, ChatFormatting.BOLD))
                .append(".")
            );
        }

        return 1;
    }

}
