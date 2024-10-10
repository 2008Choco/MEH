package wtf.choco.meh.client.screen.widgets;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

import wtf.choco.meh.client.chat.EmoteSelectorFeature;
import wtf.choco.meh.client.chat.emote.ChatEmote;
import wtf.choco.meh.client.chat.emote.HypixelChatEmote;
import wtf.choco.meh.client.keybind.MEHKeybinds;
import wtf.choco.meh.client.mixin.ChatScreenAccessor;

public final class EmoteSelectorWidget implements Renderable {

    // Widget constants
    private static final int WIDGET_WIDTH = 98;
    private static final int WIDGET_HEIGHT = 130;
    private static final int WIDGET_HEADER_HEIGHT = 16;
    private static final int WIDGET_BACKGROUND_COLOR = 0x80 << 24;
    private static final int WIDGET_HEADER_COLOR = 0xAA << 24;

    // Cell constants
    private static final int CELLS_PER_ROW = 3;
    private static final int ROWS_PER_PAGE = 4;
    private static final int EMOTES_PER_PAGE = (CELLS_PER_ROW * ROWS_PER_PAGE);
    private static final int CELL_PADDING = 3; // The amount of padding to use between cells
    private static final int TOTAL_CELL_X_PADDING = (CELL_PADDING * (CELLS_PER_ROW - 1)); // Amount of padding used between cells (x), excluding left/right
    private static final int TOTAL_CELL_Y_PADDING = (CELL_PADDING * (ROWS_PER_PAGE - 1)); // Amount of padding used between cells (x), excluding top/bottom
    private static final int CELL_SIZE = 28; // The size of an individual cell
    private static final int TOTAL_X = TOTAL_CELL_X_PADDING + (CELLS_PER_ROW * CELL_SIZE); // The total amount of x space used by all cells and their padding
    private static final int TOTAL_Y = TOTAL_CELL_Y_PADDING + (ROWS_PER_PAGE * CELL_SIZE); // The total amount of y space used by all cells and their padding
    private static final int LEFT_RIGHT_PADDING = (WIDGET_WIDTH - TOTAL_X) / 2; // The amount of padding required on the left and right side to fill evenly
    private static final int TOP_BOTTOM_PADDING = (WIDGET_HEIGHT - TOTAL_Y) / 2; // The amount of padding required on the top and bottom side to fill evenly

    private static final int CELL_COLOR = 0x60 << 24;
    private static final int CELL_SELECTED_COLOR = 0x90 << 24;

    private static final int MINIMUM_X_PADDING = 15; // The minimum amount of padding between the chat box and the widget when docked

    private boolean focused = false;
    private ChatScreen chatScreen = null;
    private int selectedEmoteIndex = 0;
    private int scrollOffset = 0;
    private List<ChatEmote> knownEmotes;

    private final EmoteSelectorFeature feature;

    public EmoteSelectorWidget(EmoteSelectorFeature feature) {
        this.feature = feature;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setChatScreen(ChatScreen chatScreen) {
        this.chatScreen = chatScreen;
    }

    private void refreshEmotes() {
        HypixelChatEmote[] hypixelChatEmotes = HypixelChatEmote.values();
        if (knownEmotes == null) {
            this.knownEmotes = new ArrayList<>(hypixelChatEmotes.length);
        } else {
            this.knownEmotes.clear();
        }

        Collections.addAll(knownEmotes, hypixelChatEmotes);
    }

    private List<ChatEmote> getKnownEmotes() {
        if (knownEmotes == null) {
            this.refreshEmotes();
        }

        return knownEmotes;
    }

    private ChatEmote getSelectedEmote() {
        return getKnownEmotes().get(selectedEmoteIndex);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (!feature.isEnabled() || !isFocused() || chatScreen == null) {
            return;
        }

        List<ChatEmote> knownEmotes = getKnownEmotes();
        Minecraft minecraft = Minecraft.getInstance();

        int x = getX(true); // TODO: Only dock to chat if necessary
        int dx = x + WIDGET_WIDTH;
        int y = getY(true) - WIDGET_HEIGHT; // TODO: Only dock to chat if necessary
        int dy = y + WIDGET_HEIGHT;
        int headerY = y - WIDGET_HEADER_HEIGHT;

        minecraft.getProfiler().push("emoteSelectorWidget");

        graphics.fill(x, y, dx, dy, WIDGET_BACKGROUND_COLOR);
        graphics.fill(x, headerY, dx, y, WIDGET_HEADER_COLOR);

        PoseStack stack = graphics.pose();

        // Stack frame
        stack.pushPose();

        stack.translate(x, headerY, 0); // Move to the top left corner of the widget header
        stack.translate(WIDGET_WIDTH / 2, WIDGET_HEADER_HEIGHT / 2, 0); // Move to the center of the widget
        stack.scale(0.75F, 0.75F, 0.75F); // Scale the text down (from the middle)
        ChatEmote selectedEmote = knownEmotes.get(selectedEmoteIndex);
        graphics.drawCenteredString(minecraft.font, selectedEmote.getDisplayName(), 0, -minecraft.font.lineHeight / 2, 0xFFFFFFFF);

        stack.popPose();

        // Stack frame
        stack.pushPose();

        stack.translate(x, y, 0); // Move to the top left corner of the widget

        stack.translate(LEFT_RIGHT_PADDING, TOP_BOTTOM_PADDING, 0);
        cellLoop:
        for (int gridY = 0; gridY < ROWS_PER_PAGE; gridY++) {
            for (int gridX = 0; gridX < CELLS_PER_ROW; gridX++) {
                int cellIndex = (gridY * CELLS_PER_ROW) + gridX;
                int emoteIndex = (scrollOffset * CELLS_PER_ROW) + cellIndex;
                if (emoteIndex >= knownEmotes.size()) {
                    break cellLoop;
                }

                this.renderEmoteCell(graphics, stack, knownEmotes.get(emoteIndex), emoteIndex == selectedEmoteIndex);
                stack.translate(CELL_SIZE + CELL_PADDING, 0, 0);
            }
            stack.translate(-(CELL_SIZE + CELL_PADDING) * CELLS_PER_ROW, CELL_SIZE + CELL_PADDING, 0);
        }

        stack.popPose();

        minecraft.getProfiler().pop();
    }

    private void renderEmoteCell(GuiGraphics graphics, PoseStack stack, ChatEmote emote, boolean selected) {
        int color = (selected ? CELL_SELECTED_COLOR : CELL_COLOR);
        graphics.fill(0, 0, CELL_SIZE, CELL_SIZE, color);

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getProfiler().push("emoteCell");
        stack.pushPose();
        stack.translate(CELL_SIZE / 2, CELL_SIZE / 2, 0);

        int textWidth = minecraft.font.width(emote.getEmoteDisplayText());
        if (textWidth >= CELL_SIZE) {
            float factor = Math.min((float) textWidth / (float) CELL_SIZE, 1.45F);
            float scale = (float) Math.log(-factor + 2.2F) + 0.7F;
            stack.scale(scale, scale, scale);
        }

        graphics.drawCenteredString(minecraft.font, emote.getEmoteDisplayText(), 0, -minecraft.font.lineHeight / 2, 0xFFFFFFFF);
        stack.popPose();

        // Gold selection border
        if (selected) {
            stack.pushPose();
            stack.scale(0.5F, 0.5F, 0.5F);

            graphics.fill(0, 0, CELL_SIZE * 2, 1, 0xFFD09E3D);
            graphics.fill(0, 0, 1, CELL_SIZE * 2, 0xFFD09E3D);
            graphics.fill(0, CELL_SIZE * 2 - 1, CELL_SIZE * 2, CELL_SIZE * 2, 0xFFD09E3D);
            graphics.fill(CELL_SIZE * 2 - 1, 0, CELL_SIZE * 2, CELL_SIZE * 2, 0xFFD09E3D);

            stack.popPose();
        }

        minecraft.getProfiler().pop();
    }

    @SuppressWarnings("unused")
    public void onKeyPress(int key, int scancode, int modifiers) {
        if (key == InputConstants.KEY_ESCAPE) {
            this.returnFocusToChatBox();
            return;
        }

        if (!MEHKeybinds.isAmecsLoaded() && (Screen.hasControlDown() && key == MEHKeybinds.KEY_EMOTE_SELECTOR)) {
            this.returnFocusToChatBox();
            return;
        }

        if (key == InputConstants.KEY_RETURN) {
            boolean keepOpen = Screen.hasShiftDown();
            String text = getSelectedEmote().getInputText();
            if (keepOpen) {
                text += " ";
            }

            ((ChatScreenAccessor) chatScreen).getInput().insertText(text);

            if (!keepOpen) {
                this.returnFocusToChatBox();
            }

            return;
        }

        int minEmoteIndex = (scrollOffset * CELLS_PER_ROW);
        int maxEmoteIndex = minEmoteIndex + EMOTES_PER_PAGE;
        if (key == InputConstants.KEY_LEFT) {
            this.selectedEmoteIndex = Math.max(selectedEmoteIndex - 1, 0);
        } else if (key == InputConstants.KEY_RIGHT) {
            this.selectedEmoteIndex = Math.min(selectedEmoteIndex + 1, getKnownEmotes().size() - 1);
        } else if (key == InputConstants.KEY_UP) {
            this.selectedEmoteIndex = Math.max(selectedEmoteIndex - CELLS_PER_ROW, 0);
        } else if (key == InputConstants.KEY_DOWN) {
            this.selectedEmoteIndex = Math.min(selectedEmoteIndex + CELLS_PER_ROW, getKnownEmotes().size() - 1);
        }

        if (selectedEmoteIndex < minEmoteIndex) {
            this.scrollOffset--;
        } else if (selectedEmoteIndex >= maxEmoteIndex) {
            this.scrollOffset++;
        }
    }

    /**
     * Take focus from a chat screen's input box. The provided chat screen will have its edit box
     * lose focus and the emote selector will take focus in its place, redirecting all inputs to
     * this widget.
     * <p>
     * This is equivalent to doing the following:
     * <pre>
     * emoteSelectorWidget.setChatScreen(chatScreen);
     * chatScreen.setFocused(null);
     * emoteSelectorWidget.setFocused(true);
     * </pre>
     *
     * @param chatScreen the chat screen from which to take focus
     */
    public void takeFocus(ChatScreen chatScreen) {
        this.setChatScreen(chatScreen);
        chatScreen.setFocused(null);
        this.setFocused(true);
    }

    // Relinquish focus from this emote selector to the chat screen's edit box
    public void returnFocusToChatBox() {
        if (!focused || chatScreen == null) {
            return;
        }

        this.chatScreen.setFocused(((ChatScreenAccessor) chatScreen).getInput());
        this.setFocused(false);
    }

    private int getX(@SuppressWarnings("unused") boolean docked) {
        return getMinimumX(); // TODO: Base x position on chat cursor position
    }

    private int getY(boolean docked) {
        return chatScreen.height - (docked ? 40 : 18);
    }

    private static int getMinimumX() {
        Minecraft minecraft = Minecraft.getInstance();
        return Mth.floor(ChatComponent.getWidth(minecraft.options.chatWidth().get())) + MINIMUM_X_PADDING;
    }

}
