package wtf.choco.meh.client.screen.widgets;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry.Translatable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.party.Party;
import wtf.choco.meh.client.party.PartyManagerFeature;
import wtf.choco.meh.client.party.PartyMember;

public final class PartyListWidget {

    public static enum Position implements Translatable {

        TOP_LEFT,
        TOP_RIGHT;

        @NotNull
        @Override
        public String getKey() {
            return "meh.party_manager.list.position." + name().toLowerCase();
        }

    }

    private static final int ENTRY_HORIZONTAL_PADDING = 2;
    private static final int ENTRY_VERTICAL_PADDING = 2;
    private static final int SKIN_SIZE = 8;
    private static final float TEXT_SCALE = 0.75F;

    private final PartyManagerFeature feature;

    public PartyListWidget(PartyManagerFeature feature) {
        this.feature = feature;
    }

    public void render(GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance();
        Window window = minecraft.getWindow();
        /*
        int mouseX = Mth.floor(minecraft.mouseHandler.xpos() * window.getGuiScaledWidth() / window.getScreenWidth());
        int mouseY = Mth.floor(minecraft.mouseHandler.ypos() * window.getGuiScaledHeight() / window.getScreenHeight());
        */

        Party party = feature.getParty();
        if (party == null) {
            return;
        }

        Position position = MEHClient.getConfig().getPartyManager().getPartyListPosition();

        List<PartyMember> members = new ArrayList<>(party.getMembers());
        Collections.sort(members);

        for (int i = 0, y = ENTRY_VERTICAL_PADDING; i < members.size(); i++, y += SKIN_SIZE + ENTRY_VERTICAL_PADDING) {
            PartyMember member = members.get(i);
            PlayerInfo playerInfo = member.getPlayerInfo();

            int faceX = ENTRY_HORIZONTAL_PADDING;
            if (position == Position.TOP_RIGHT) {
                faceX = window.getGuiScaledWidth() - SKIN_SIZE - ENTRY_HORIZONTAL_PADDING;
            }

            PlayerFaceRenderer.draw(graphics, playerInfo.getSkin(), faceX, y, SKIN_SIZE);

            Component text;
            if (position == Position.TOP_LEFT) {
                text = Component.literal(playerInfo.getProfile().getName())
                    .append(" ")
                    .append(Component.literal("(" + member.getRole().getPrettyName() + ")").withStyle(ChatFormatting.GRAY));
            } else {
                text = Component.literal("(" + member.getRole().getPrettyName() + ")").withStyle(ChatFormatting.GRAY)
                    .append(" ")
                    .append(Component.literal(playerInfo.getProfile().getName()).withStyle(ChatFormatting.WHITE));
            }

            int textOffset = 0;
            int textX = (ENTRY_HORIZONTAL_PADDING * 2) + SKIN_SIZE;
            if (position == Position.TOP_RIGHT) {
                textOffset = minecraft.font.width(text);
                textX = window.getGuiScaledWidth() - (ENTRY_HORIZONTAL_PADDING * 2) - SKIN_SIZE - textOffset;
            }

            PoseStack stack = graphics.pose();
            stack.pushPose();

            int textY = y + (SKIN_SIZE / 2);
            stack.translate(textX + textOffset, textY, 0);
            stack.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);

            graphics.drawString(minecraft.font, text, -textOffset, -(minecraft.font.lineHeight / 2), 0xFFFFFFFF);

            stack.popPose();
        }
    }

}
