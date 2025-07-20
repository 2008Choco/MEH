package wtf.choco.meh.client.fishing;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.phys.Vec3;

import org.joml.Matrix4f;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.renderer.MEHRenderTypes;

public final class MythicalFishHelperFeature extends Feature {

    private static final ResourceLocation TEXTURE_LOCATION = MEHClient.key("textures/wat.png");

    private static final float GRAPHIC_DISTANCE_FROM_BOBBER = 1.0F;

    public MythicalFishHelperFeature(MEHClient mod) {
        super(mod);
    }

    @Override
    protected boolean isFeatureEnabled(MEHConfig config) {
        return true; // TODO
    }

    @Override
    protected void registerListeners() {
        WorldRenderEvents.AFTER_ENTITIES.register(this::renderInWorldHelperGraphic);
    }

    private void renderInWorldHelperGraphic(WorldRenderContext context) {
        FishingHook fishingHook = MEHClient.getInstance().getHypixelServerState().getFishingState().getActiveFishingHook().get();
        if (fishingHook == null) {
            return;
        }

        MultiBufferSource consumers = context.consumers();
        PoseStack stack = context.matrixStack();
        if (consumers == null || stack == null) {
            return;
        }

        // Translate and rotate stack for world-anchored rendering
        Camera camera = context.camera();
        Vec3 cameraPos = camera.position();
        Vec3 targetPos = fishingHook.position();

        Vec3 targetToCamera = cameraPos.subtract(targetPos);
        Vec3 perpendicularToUp = targetToCamera.cross(Direction.UP.getUnitVec3()).normalize().multiply(-GRAPHIC_DISTANCE_FROM_BOBBER, -GRAPHIC_DISTANCE_FROM_BOBBER, -GRAPHIC_DISTANCE_FROM_BOBBER);

        stack.pushPose();
        stack.translate(targetPos.subtract(cameraPos).add(perpendicularToUp));
        stack.mulPose(context.positionMatrix().invert(new Matrix4f()));

        this.renderHelperGraphic(stack, consumers);

        // Translate stack for text rendering
        Font font = Minecraft.getInstance().font;

        stack.last().pose().rotate((float) Math.PI, 0.0F, 1.0F, 0.0F);
        stack.scale(-0.025F, -0.025F, -0.025F);
        stack.translate(0, -font.lineHeight + 1, 0);

        this.renderHelperText(stack, consumers, font);

        stack.popPose();
    }

    private void renderHelperGraphic(PoseStack stack, MultiBufferSource bufferSource) {
        VertexConsumer buffer = bufferSource.getBuffer(MEHRenderTypes.worldPositionedGui(TEXTURE_LOCATION));

        Pose pose = stack.last();
        buffer.addVertex(pose, 0F, 0F, 0).setColor(0xFFFFFFFF).setUv(0, 1)
            .addVertex(pose, 1F, -0F, 0).setColor(0xFFFFFFFF).setUv(1, 1)
            .addVertex(pose, 1F, 1F, 0).setColor(0xFFFFFFFF).setUv(1, 0)
            .addVertex(pose, -0F, 1F, 0).setColor(0xFFFFFFFF).setUv(0, 0);
    }

    private void renderHelperText(PoseStack stack, MultiBufferSource bufferSource, Font font) {
        Matrix4f pose = stack.last().pose();
        font.drawInBatch(Component.literal("Example text!"), 0, 0, 0xFFFFFFFF, false, pose, bufferSource, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
    }

}
