package wtf.choco.meh.client.fishing;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
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

    public MythicalFishHelperFeature(MEHClient mod) {
        super(mod);
    }

    @Override
    protected boolean isFeatureEnabled(MEHConfig config) {
        return true; // TODO
    }

    @Override
    protected void registerListeners() {
        WorldRenderEvents.AFTER_ENTITIES.register(this::renderHeatBar);
    }

    private void renderHeatBar(WorldRenderContext context) {
        /*
        MythicalFishEntity mythicalFish = MEHClient.getInstance().getHypixelServerState().getFishingState().getMythicalFishEntity();
        if (mythicalFish == null) {
            return;
        }
        */
        FishingHook fishingHook = MEHClient.getInstance().getHypixelServerState().getFishingState().getActiveFishingHook().get();
        if (fishingHook == null) {
            return;
        }

        MultiBufferSource consumers = context.consumers();
        PoseStack stack = context.matrixStack();
        if (consumers == null || stack == null) {
            return;
        }

        Camera camera = context.camera();
        Vec3 cameraPos = camera.position();
        Vec3 targetPos = fishingHook.position();

        Vec3 targetToCamera = cameraPos.subtract(targetPos);
        Vec3 perpendicularToUp = targetToCamera.cross(Direction.UP.getUnitVec3()).normalize().multiply(-1, -1, -1);

        stack.pushPose();
        stack.translate(targetPos.subtract(cameraPos).add(perpendicularToUp).add(0, 0.5, 0));
        stack.mulPose(context.positionMatrix().invert(new Matrix4f()));

        Pose pose = stack.last();
        VertexConsumer buffer = consumers.getBuffer(MEHRenderTypes.worldPositionedGui(TEXTURE_LOCATION));
        buffer.addVertex(pose, -0.5F, -0.5F, 0).setColor(0xFFFFFFFF).setUv(0, 1)
            .addVertex(pose, 0.5F, -0.5F, 0).setColor(0xFFFFFFFF).setUv(1, 1)
            .addVertex(pose, 0.5F, 0.5F, 0).setColor(0xFFFFFFFF).setUv(1, 0)
            .addVertex(pose, -0.5F, 0.5F, 0).setColor(0xFFFFFFFF).setUv(0, 0);
        stack.popPose();
    }

}
