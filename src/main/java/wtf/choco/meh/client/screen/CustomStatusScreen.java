package wtf.choco.meh.client.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.server.CustomStatusStorage;
import wtf.choco.meh.client.util.Components;

public final class CustomStatusScreen extends Screen {

    private static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "textures/gui/custom_status_screen.png");

    private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "container/scroller");
    private static final ResourceLocation SCROLLER_DISABLED_SPRITE = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "container/scroller_disabled");
    private static final ResourceLocation SIDEBAR_TAB_SPRITE = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "container/sidebar_tab");
    private static final ResourceLocation SIDEBAR_TEXT_FIELD_SPRITE = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "container/sidebar_text_field");

    private static final int BACKGROUND_WIDTH = 217;
    private static final int BACKGROUND_HEIGHT = 146;
    private static final int SIDEBAR_TAB_X = 214;
    private static final int SIDEBAR_TAB_Y = 15;
    private static final int SIDEBAR_TAB_WIDTH = 26;
    private static final int SIDEBAR_TAB_HEIGHT = 89;

    private static final int SIDEBAR_TEXT_FIELD_BACKGROUND_X = 237;
    private static final int SIDEBAR_TEXT_FIELD_BACKGROUND_Y = 15;
    private static final int SIDEBAR_TEXT_FIELD_BACKGROUND_WIDTH = 130;
    private static final int SIDEBAR_TEXT_FIELD_BACKGROUND_HEIGHT = 29;
    private static final int SIDEBAR_TEXT_FIELD_WIDTH = 115;
    private static final int SIDEBAR_TEXT_FIELD_HEIGHT = 18;

    private static final int TITLE_OFFSET_X = 8;
    private static final int TITLE_OFFSET_Y = 6;

    private static final int PORTRAIT_START_X = 9;
    private static final int PORTRAIT_START_Y = 19;
    private static final int PORTRAIT_WIDTH = 66;
    private static final int PORTRAIT_HEIGHT = 120;
    private static final float PORTRAIT_NAMEPLATE_SCALE = 0.75F;

    private static final int SCROLL_BAR_X = 203;
    private static final int SCROLL_BAR_Y = 18;
    private static final int SCROLL_BAR_HEIGHT = 120;
    private static final int SCROLLER_WIDTH = 6;
    private static final int SCROLLER_HEIGHT = 27;
    private static final int SCROLLER_MAX_OFFSET = SCROLL_BAR_HEIGHT - SCROLLER_HEIGHT;

    private static final int STATUS_WIDGET_HEIGHT = 120;
    private static final int STATUS_BUTTON_X = 79;
    private static final int STATUS_BUTTON_START_Y = 18;
    private static final int STATUS_BUTTON_WIDTH = 123;
    private static final int STATUS_BUTTON_HEIGHT = 24;
    private static final int STATUS_BUTTON_COUNT = (STATUS_WIDGET_HEIGHT / STATUS_BUTTON_HEIGHT); // Visible at once, without scrolling

    private static final int INFO_X = 203;
    private static final int INFO_Y = 6;
    private static final int INFO_SIZE_X = 6;
    private static final int INFO_SIZE_Y = 7;

    private static final float EMPTY_STATUS_TEXT_SCALE = 0.75F;

    private int leftX;
    private int topY;
    private int scrollOffset;
    private boolean dragging = false;

    private Component statusDisplayOverride = null;

    private int selectedIndex;
    private CustomStatusButton[] customStatusButtons = new CustomStatusButton[STATUS_BUTTON_COUNT];
    private AddStatusButton addStatusButton;
    private Button deleteStatusButton;
    private EditStatusButton editStatusButton;
    private Button applyStatusButton;

    private EditBox addEditBox;
    private EditEditBox editEditBox;

    private final CustomStatusStorage statusStorage;

    public CustomStatusScreen(CustomStatusStorage statusStorage) {
        super(Component.translatable("gui.meh.custom_status.title"));

        this.statusStorage = statusStorage;
        this.selectedIndex = -1; // TODO: This should be pulled from the currently selected status, if possible
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        this.updateTopLeftCoordinates(width, height);

        int y = topY + STATUS_BUTTON_START_Y;
        for (int buttonIndex = 0; buttonIndex < STATUS_BUTTON_COUNT; buttonIndex++) {
            CustomStatusButton button = new CustomStatusButton(leftX + STATUS_BUTTON_X, y, buttonIndex);

            this.customStatusButtons[buttonIndex] = button;
            this.addRenderableWidget(button);
            y += STATUS_BUTTON_HEIGHT;
        }

        int buttonX = leftX + SIDEBAR_TAB_X + 2;
        int buttonY = topY + SIDEBAR_TAB_Y + 5;
        this.addStatusButton = new AddStatusButton(buttonX, buttonY, 18, 18);
        this.addRenderableWidget(addStatusButton);
        this.deleteStatusButton = new RemoveStatusButton(buttonX, buttonY + 20, 18, 18);
        this.addRenderableWidget(deleteStatusButton);
        this.editStatusButton = new EditStatusButton(buttonX, buttonY + 40, 18, 18);
        this.addRenderableWidget(editStatusButton);
        this.applyStatusButton = new ApplyStatusButton(buttonX, buttonY + 60, 18, 18);
        this.addRenderableWidget(applyStatusButton);

        int textX = leftX + SIDEBAR_TEXT_FIELD_BACKGROUND_X + 5;
        int textY = topY + SIDEBAR_TEXT_FIELD_BACKGROUND_Y + 10;
        this.addEditBox = new AddEditBox(font, textX, textY, SIDEBAR_TEXT_FIELD_WIDTH, SIDEBAR_TEXT_FIELD_HEIGHT);
        this.addEditBox.visible = false;
        this.addRenderableWidget(addEditBox);
        this.editEditBox = new EditEditBox(font, textX, textY + 40, SIDEBAR_TEXT_FIELD_WIDTH, SIDEBAR_TEXT_FIELD_HEIGHT);
        this.editEditBox.visible = false;
        this.addRenderableWidget(editEditBox);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        this.updateTopLeftCoordinates(width, height);
    }

    private void updateTopLeftCoordinates(int width, int height) {
        this.leftX = (width - BACKGROUND_WIDTH) / 2;
        this.topY = (height - BACKGROUND_HEIGHT) / 2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float tickDelta) {
        super.render(graphics, mouseX, mouseY, tickDelta);

        graphics.drawString(font, title, leftX + TITLE_OFFSET_X, topY + TITLE_OFFSET_Y, 0x040404, false);
        this.renderCustomStatusTextOnButtons(graphics);
        this.renderScrollBar(graphics);

        for (CustomStatusButton button : customStatusButtons) {
            button.visible = button.index < statusStorage.getStatusCount();
        }

        boolean hasSelectedStatus = (selectedIndex >= 0 && selectedIndex < statusStorage.getStatusCount());
        this.deleteStatusButton.active = hasSelectedStatus;
        this.editStatusButton.active = hasSelectedStatus;
        this.applyStatusButton.active = hasSelectedStatus;

        if (statusStorage.getStatusCount() == 0) {
            this.renderCenteredEmptyStatusMessage(graphics);
        }

        int infoX = leftX + INFO_X;
        int infoEndX = infoX + INFO_SIZE_X;
        int infoY = topY + INFO_Y;
        int infoEndY = infoY + INFO_SIZE_Y;
        if (mouseX >= infoX && mouseX <= infoEndX && mouseY >= infoY && mouseY <= infoEndY) {
            graphics.renderTooltip(font, Tooltip.splitTooltip(minecraft, Component.translatable("gui.meh.custom_status.info")), DefaultTooltipPositioner.INSTANCE, mouseX, mouseY);
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float tickDelta) {
        graphics.blit(RenderType::guiTextured, BACKGROUND_LOCATION, leftX, topY, 0.0F, 0.0F, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, 256, 256);
        graphics.blitSprite(RenderType::guiTextured, SIDEBAR_TAB_SPRITE, leftX + SIDEBAR_TAB_X, topY + SIDEBAR_TAB_Y, SIDEBAR_TAB_WIDTH, SIDEBAR_TAB_HEIGHT);

        if (addStatusButton.adding) {
            graphics.blitSprite(RenderType::guiTextured, SIDEBAR_TEXT_FIELD_SPRITE, leftX + SIDEBAR_TEXT_FIELD_BACKGROUND_X, topY + SIDEBAR_TEXT_FIELD_BACKGROUND_Y, SIDEBAR_TEXT_FIELD_BACKGROUND_WIDTH, SIDEBAR_TEXT_FIELD_BACKGROUND_HEIGHT);
        } else if (editStatusButton.editing) {
            graphics.blitSprite(RenderType::guiTextured, SIDEBAR_TEXT_FIELD_SPRITE, leftX + SIDEBAR_TEXT_FIELD_BACKGROUND_X, topY + SIDEBAR_TEXT_FIELD_BACKGROUND_Y + 40, SIDEBAR_TEXT_FIELD_BACKGROUND_WIDTH, SIDEBAR_TEXT_FIELD_BACKGROUND_HEIGHT);
        }

        Component selectedStatus;
        if (statusDisplayOverride != null) {
            selectedStatus = statusDisplayOverride;
        } else {
            selectedStatus = (selectedIndex >= 0 && selectedIndex < statusStorage.getStatusCount()) ? statusStorage.getStatus(selectedIndex) : null;
        }

        this.renderPortraitWithNameplateFollowsMouse(graphics, mouseX, mouseY, selectedStatus);
    }

    private void renderCenteredEmptyStatusMessage(GuiGraphics graphics) {
        PoseStack stack = graphics.pose();
        stack.pushPose();

        // Re-scale the text because the default scale is too large
        int startX = leftX + STATUS_BUTTON_X;
        int startY = topY + STATUS_BUTTON_START_Y;
        int centerX = startX + (STATUS_BUTTON_WIDTH / 2);
        int centerY = startY + (STATUS_WIDGET_HEIGHT / 2);
        stack.translate(centerX, centerY, 0);
        stack.scale(EMPTY_STATUS_TEXT_SCALE, EMPTY_STATUS_TEXT_SCALE, 0.0F);
        stack.translate(-centerX, -centerY, 0);

        Component header = Component.translatable("gui.meh.custom_status.empty.header");
        Component footer = Component.translatable("gui.meh.custom_status.empty.footer");
        int y = (height + font.lineHeight) / 2;
        int headerX = startX + ((STATUS_BUTTON_WIDTH - font.width(header)) / 2);
        int footerX = startX + ((STATUS_BUTTON_WIDTH - font.width(footer)) / 2);
        graphics.drawString(font, header, headerX, y - (font.lineHeight / 2) - 1, 0x3A3A3A, false);
        graphics.drawString(font, footer, footerX, y + (font.lineHeight / 2) + 1, 0x3A3A3A, false);

        stack.popPose();
    }

    private void renderCustomStatusTextOnButtons(GuiGraphics graphics) {
        int textX = leftX + STATUS_BUTTON_X + 6;
        int textY = topY + STATUS_BUTTON_START_Y + ((STATUS_BUTTON_HEIGHT - font.lineHeight) / 2) + 1;
        int clipX = leftX + STATUS_BUTTON_X + 1;
        int clipEndX = leftX + STATUS_BUTTON_X + STATUS_BUTTON_WIDTH - 2;

        for (int i = 0; i < Math.min(STATUS_BUTTON_COUNT, statusStorage.getStatusCount()); i++) {
            int statusIndex = i + scrollOffset;
            graphics.enableScissor(clipX, 0, clipEndX, graphics.guiHeight());
            graphics.drawString(font, statusStorage.getStatus(statusIndex), textX, textY, 0xFFFFFF);
            graphics.disableScissor();
            textY += STATUS_BUTTON_HEIGHT;
        }
    }

    private void renderScrollBar(GuiGraphics graphics) {
        int scrollerOffset = 0;
        int extraStatuses = statusStorage.getStatusCount() - STATUS_BUTTON_COUNT;

        if (extraStatuses > 0) {
            int pixelsPerScrollOffset = SCROLLER_MAX_OFFSET / extraStatuses;
            scrollerOffset = Math.min(SCROLLER_MAX_OFFSET, scrollOffset * pixelsPerScrollOffset);
            if (scrollOffset == extraStatuses) {
                scrollerOffset = SCROLLER_MAX_OFFSET;
            }
        }

        ResourceLocation sprite = isAllowedToScroll() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
        graphics.blitSprite(RenderType::guiTextured, sprite, leftX + SCROLL_BAR_X, topY + SCROLL_BAR_Y + scrollerOffset, SCROLLER_WIDTH, SCROLLER_HEIGHT);
    }

    private void renderPortraitWithNameplateFollowsMouse(GuiGraphics graphics, int mouseX, int mouseY, @Nullable Component customStatusText) {
        // Render entity
        int portraitX = (leftX + PORTRAIT_START_X);
        int portraitY = (topY + PORTRAIT_START_Y);
        int portraitEndX = portraitX + PORTRAIT_WIDTH;
        int portraitEndY = portraitY + PORTRAIT_HEIGHT;
        InventoryScreen.renderEntityInInventoryFollowsMouse(
                graphics,
                portraitX,
                portraitY,
                portraitEndX,
                portraitEndY,
                40, // Scale
                0.225F, // Offset Y
                mouseX, mouseY + 20, minecraft.player
        );

        // Render nameplates
        float nameplateX = (portraitX + portraitEndX) / 2.0F;
        float nameplateY = (portraitY + portraitEndY) / 2.0F;

        graphics.enableScissor(portraitX, portraitY, portraitEndX, portraitEndY);
        if (customStatusText != null) {
            this.renderNameplate(graphics, minecraft.player.getName(), nameplateX, nameplateY - 52, PORTRAIT_NAMEPLATE_SCALE);
            this.renderNameplate(graphics, customStatusText, nameplateX, nameplateY - 42, PORTRAIT_NAMEPLATE_SCALE);
        } else {
            this.renderNameplate(graphics, minecraft.player.getName(), nameplateX, nameplateY - 42, PORTRAIT_NAMEPLATE_SCALE);
        }
        graphics.disableScissor();
    }

    private void renderNameplate(GuiGraphics graphics, Component text, float x, float y, float scale) {
        Lighting.setupForEntityInInventory();
        PoseStack stack = graphics.pose();
        stack.pushPose();
        stack.translate(x, y, 50);
        stack.scale(scale, scale, 0.0F);

        float offsetX = -font.width(text) / 2.0F;
        int nameplateBackgroundAlpha = (int) (minecraft.options.getBackgroundOpacity(0.25F) * 255.0F) << 24;
        graphics.drawSpecial(buffer -> font.drawInBatch(
                text,
                offsetX, 0, // Offset x, y
                0xFFFFFFFF, // Text color
                false, // Draw shadow
                stack.last().pose(),
                buffer,
                DisplayMode.NORMAL,
                nameplateBackgroundAlpha | 0x9A9A9A,
                LightTexture.FULL_BRIGHT
        ));

        stack.popPose();
        Lighting.setupFor3DItems();
    }

    private boolean isAllowedToScroll() {
        return statusStorage.getStatusCount() > STATUS_BUTTON_COUNT;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double f, double scrollDirection) {
        if (super.mouseScrolled(mouseX, mouseY, f, scrollDirection)) {
            return true;
        }

        if (isAllowedToScroll()) {
            int offsetMax = (statusStorage.getStatusCount() - STATUS_BUTTON_COUNT);
            this.scrollOffset = Mth.clamp((int) (scrollOffset - scrollDirection), 0, offsetMax);
        }

        return true;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (!dragging) {
            return super.mouseDragged(mouseX, mouseY, button, dx, dy);
        }

        this.updateScrollOffset(mouseY);
        return true;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.dragging = false;

        int scrollBarX = leftX + SCROLL_BAR_X;
        int scrollBarMaxX = scrollBarX + SCROLLER_WIDTH;
        int scrollBarY = topY + SCROLL_BAR_Y;
        int scrollBarMaxY = scrollBarY + SCROLL_BAR_HEIGHT + 1;

        if (isAllowedToScroll() && mouseX > scrollBarX && mouseX < scrollBarMaxX && mouseY > scrollBarY && mouseY <= scrollBarMaxY) {
            this.dragging = true;
            this.updateScrollOffset(mouseY);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !addStatusButton.adding && !editStatusButton.editing;
    }

    @Override
    public void onClose() {
        super.onClose();
        this.statusStorage.saveToFile();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isAllowedToScroll()) {
            int focusedIndex = -1;
            if (getFocused() instanceof CustomStatusButton button) {
                focusedIndex = button.index;
            }
            
            if (keyCode == InputConstants.KEY_UP && focusedIndex == 0 && selectedIndex > 0) {
                this.selectedIndex--;
                this.scrollOffset--;
            } else if (keyCode == InputConstants.KEY_DOWN && focusedIndex == (STATUS_BUTTON_COUNT - 1) && selectedIndex < (statusStorage.getStatusCount() - 1)) {
                this.selectedIndex++;
                this.scrollOffset++;
            }
        }

        if (getFocused() instanceof CustomStatusButton) {
            if (keyCode == InputConstants.KEY_RETURN || keyCode == InputConstants.KEY_NUMPADENTER) {
                this.applyStatusButton.onPress();
            } else if (keyCode == InputConstants.KEY_DELETE) {
                this.deleteStatusButton.playDownSound(minecraft.getSoundManager());
                this.deleteStatusButton.onPress();
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void setFocused(@Nullable GuiEventListener target) {
        super.setFocused(target);

        if (target instanceof CustomStatusButton button) {
            this.selectedIndex = button.index + scrollOffset;
        }
    }

    private void updateScrollOffset(double mouseY) {
        int extraStatuses = statusStorage.getStatusCount() - STATUS_BUTTON_COUNT;

        int y = topY + SCROLL_BAR_Y;
        int maxY = y + SCROLL_BAR_HEIGHT;
        float newScrollOffset = ((float) mouseY - y - (SCROLLER_HEIGHT / 2)) / (maxY - y - SCROLLER_HEIGHT);
        newScrollOffset = (newScrollOffset * extraStatuses) + 0.5F;

        this.scrollOffset = Mth.clamp((int) newScrollOffset, 0, extraStatuses);
    }

    private final class CustomStatusButton extends Button {

        private final int index;

        public CustomStatusButton(int x, int y, int index) {
            super(x, y, STATUS_BUTTON_WIDTH, STATUS_BUTTON_HEIGHT, CommonComponents.EMPTY, null, DEFAULT_NARRATION);
            this.index = index;
        }

        @Override
        public void onPress() {
            CustomStatusScreen.this.selectedIndex = index + scrollOffset;
        }

    }

    private final class AddStatusButton extends Button {

        private boolean adding = false;

        protected AddStatusButton(int x, int y, int width, int height) {
            super(x, y, width, height, Component.literal("+"), null, text -> Component.translatable("gui.meh.custom_status.button.add_status.narration"));
            this.setTooltip(Tooltip.create(Component.translatable("gui.meh.custom_status.button.add_status.tooltip")));
        }

        @Override
        public void onPress() {
            // We don't want to allow adding and editing at the same time
            if (editStatusButton.editing) {
                CustomStatusScreen.this.editStatusButton.onPress();
            }

            CustomStatusScreen.this.selectedIndex = -1;
            this.adding = !adding;
            CustomStatusScreen.this.addEditBox.visible = adding;

            if (adding) {
                CustomStatusScreen.this.setFocused(addEditBox);
            } else {
                CustomStatusScreen.this.addEditBox.setValue("");
            }
        }

    }

    private final class RemoveStatusButton extends Button {

        protected RemoveStatusButton(int x, int y, int width, int height) {
            super(x, y, width, height, Component.literal("-"), null, text -> Component.translatable("gui.meh.custom_status.button.delete_status.narration"));
            this.setTooltip(Tooltip.create(Component.translatable("gui.meh.custom_status.button.delete_status.tooltip")));
        }

        @Override
        public void onPress() {
            int beforeStatusCount = statusStorage.getStatusCount();
            CustomStatusScreen.this.statusStorage.removeStatus(selectedIndex);

            int lastViewableIndex = STATUS_BUTTON_COUNT + scrollOffset;
            if (lastViewableIndex == beforeStatusCount) {
                CustomStatusScreen.this.scrollOffset = Math.max(scrollOffset - 1, 0);
            }

            if (selectedIndex == (beforeStatusCount - 1)) {
                CustomStatusScreen.this.selectedIndex--;
            }
        }

    }

    private final class EditStatusButton extends Button {

        private boolean editing = false;

        protected EditStatusButton(int x, int y, int width, int height) {
            super(x, y, width, height, Component.literal("✎"), null, text -> Component.translatable("gui.meh.custom_status.button.edit_status.narration"));
            this.setTooltip(Tooltip.create(Component.translatable("gui.meh.custom_status.button.edit_status.tooltip")));
        }

        @Override
        public void onPress() {
            if (selectedIndex < 0 || selectedIndex > statusStorage.getStatusCount()) {
                return;
            }

            // We don't want to allow editing and adding at the same time
            if (addStatusButton.adding) {
                CustomStatusScreen.this.addStatusButton.onPress();
            }

            this.editing = !editing;
            CustomStatusScreen.this.editEditBox.visible = editing;

            if (editing) {
                CustomStatusScreen.this.setFocused(editEditBox);
                CustomStatusScreen.this.editEditBox.editingIndex = CustomStatusScreen.this.selectedIndex;
                
                Component status = CustomStatusScreen.this.statusStorage.getStatus(selectedIndex);
                CustomStatusScreen.this.editEditBox.setValue(Components.toLegacyText(status, '&'));
            } else {
                CustomStatusScreen.this.editEditBox.editingIndex = -1;
                CustomStatusScreen.this.editEditBox.setValue("");
            }
        }

    }

    private final class ApplyStatusButton extends Button {

        public ApplyStatusButton(int x, int y, int width, int height) {
            super(x, y, width, height, Component.literal("✔"), null, text -> Component.translatable("gui.meh.custom_status.button.apply_status.narration"));
            this.setTooltip(Tooltip.create(Component.translatable("gui.meh.custom_status.button.apply_status.tooltip")));
        }

        @Override
        public void onPress() {
            if (selectedIndex < 0 || selectedIndex > statusStorage.getStatusCount()) {
                return;
            }

            Component status = statusStorage.getStatus(selectedIndex);
            String command = "customstatus " + Components.toLegacyText(status, '&');

            if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
                minecraft.player.connection.sendCommand(command);
            } else {
                minecraft.player.displayClientMessage(Component.literal("Would have sent command: " + command), false);
            }
        }

    }

    private final class AddEditBox extends EditBox {

        public AddEditBox(Font font, int x, int y, int width, int height) {
            super(font, x, y, width, height, Component.translatable("gui.meh.custom_status.edit_box.status_text.narration"));
            this.setBordered(false);
            this.setMaxLength(64);
            this.setHint(Component.translatable("gui.meh.custom_status.edit_box.status_text.hint"));
            this.setResponder(this::onValueChange);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            boolean confirm = (keyCode == InputConstants.KEY_RETURN || keyCode == InputConstants.KEY_NUMPADENTER);
            boolean cancel = (keyCode == InputConstants.KEY_ESCAPE);
            boolean close = (confirm || cancel);

            if (confirm) {
                if (getValue().isBlank()) {
                    return true;
                }

                CustomStatusScreen.this.statusStorage.addStatus(Components.fromLegacyText(getValue(), '&'));
                CustomStatusScreen.this.statusDisplayOverride = null;
                CustomStatusScreen.this.selectedIndex = (statusStorage.getStatusCount() - 1);
                CustomStatusScreen.this.scrollOffset = Math.max((statusStorage.getStatusCount() - STATUS_BUTTON_COUNT), 0);
                CustomStatusScreen.this.setFocused(customStatusButtons[customStatusButtons.length - 1]);
            }

            if (close) {
                CustomStatusScreen.this.addStatusButton.adding = false;
                this.visible = false;
                this.setValue("");
                return false;
            }

            return super.keyPressed(keyCode, scanCode, modifiers);
        }

        private void onValueChange(String value) {
            CustomStatusScreen.this.statusDisplayOverride = Components.fromLegacyText(value, '&');
        }

    }

    private final class EditEditBox extends EditBox {

        private int editingIndex = -1;

        public EditEditBox(Font font, int x, int y, int width, int height) {
            super(font, x, y, width, height, Component.translatable("gui.meh.custom_status.edit_box.status_text.narration"));
            this.setBordered(false);
            this.setMaxLength(64);
            this.setHint(Component.translatable("gui.meh.custom_status.edit_box.status_text.hint"));
            this.setResponder(this::onValueChange);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (editingIndex == -1) {
                return false;
            }

            boolean confirm = (keyCode == InputConstants.KEY_RETURN || keyCode == InputConstants.KEY_NUMPADENTER);
            boolean cancel = (keyCode == InputConstants.KEY_ESCAPE);
            boolean close = (confirm || cancel);

            if (confirm) {
                if (getValue().isBlank()) {
                    return true;
                }

                CustomStatusScreen.this.statusStorage.setStatus(editingIndex, Components.fromLegacyText(getValue(), '&'));
                CustomStatusScreen.this.statusDisplayOverride = null;
                CustomStatusScreen.this.selectedIndex = editingIndex;
                this.editingIndex = -1;
            }

            if (close) {
                CustomStatusScreen.this.editStatusButton.editing = false;
                this.visible = false;
                this.setValue("");
                return false;
            }

            return super.keyPressed(keyCode, scanCode, modifiers);
        }

        private void onValueChange(String value) {
            CustomStatusScreen.this.statusDisplayOverride = Components.fromLegacyText(value, '&');
        }

    }

}
