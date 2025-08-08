package com.wzz.starsource.renderer;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;

import com.wzz.starsource.ModMain;
import com.wzz.starsource.entity.EntitySuperDimension;
import mods.flammpfeil.slashblade.client.renderer.model.obj.Face;
import mods.flammpfeil.slashblade.client.renderer.model.obj.GroupObject;
import mods.flammpfeil.slashblade.client.renderer.model.obj.Vertex;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderSuperDimension extends EntityRenderer<EntitySuperDimension> {
    private static WavefrontObject model = null;
    private static final ResourceLocation MODEL_LOCATION =
        ResourceLocation.tryBuild(ModMain.MODID, "model/slashdim.obj");
    private static final ResourceLocation TEXTURE_LOCATION =
        ResourceLocation.tryBuild(ModMain.MODID, "model/slashdim.png");

    private static final RenderType SUPER_DIMENSION_RENDER_TYPE = RenderType.create(
        "super_dimension",
        DefaultVertexFormat.POSITION_COLOR,
        VertexFormat.Mode.TRIANGLES,
        256,
        false,
        true,
        RenderType.CompositeState.builder()
            .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
            .setCullState(RenderStateShard.NO_CULL)
            .setLightmapState(RenderStateShard.LIGHTMAP)
            .setOverlayState(RenderStateShard.OVERLAY)
            .createCompositeState(false)
    );

    public RenderSuperDimension(EntityRendererProvider.Context context) {
        super(context);
    }

    public float scale(EntitySuperDimension entitySuperDimension) {
        return 0.1f;
    }

    @Override
    public void render(EntitySuperDimension entity, float entityYaw, float partialTick,
                      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        if (model == null) {
            if (MODEL_LOCATION != null) {
                model = new WavefrontObject(MODEL_LOCATION);
            }
        }
        poseStack.pushPose();
        int color = entity.getColor();
        int lifetime = entity.getLifeTime();
        int seed = entity.getPersistentData().getInt("seed");
        double deathTime = (double) lifetime;
        double remainingTime = Math.max(0, lifetime - entity.tickCount - partialTick);
        double baseAlpha = Math.sin(Math.PI / 2 * Math.min(1.0, remainingTime / deathTime));
        RandomSource random = RandomSource.create(seed);
        List<Integer> colors = Arrays.asList(0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00, 0xFF00FF, 0x00FFFF);
        if (color == 3355647) {
            color = colors.get(random.nextInt(colors.size()));
        }
        float rotParTicks = 200.0F;
        float rotation = (entity.tickCount % rotParTicks / rotParTicks * 360.0F) +
                        (partialTick * (360.0F / rotParTicks));
        float scale = scale(entity);
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        renderMainBody(poseStack, bufferSource, entity, color, baseAlpha, partialTick);
        renderWaveEffect(poseStack, bufferSource, entity, color, baseAlpha, partialTick);
        poseStack.popPose();
    }

    public float layerScale(EntitySuperDimension superDimension, int i) {
        return (float) Math.pow(0.95, i);
    }

    private void renderMainBody(PoseStack poseStack, MultiBufferSource bufferSource,
                               EntitySuperDimension entity, int color, double alpha, float partialTick) {

        VertexConsumer vertexConsumer = bufferSource.getBuffer(SUPER_DIMENSION_RENDER_TYPE);
        for (int i = 0; i < 5; i++) {
            poseStack.pushPose();
            float layerScale = layerScale(entity, i);
            poseStack.scale(layerScale, layerScale, layerScale);

            float layerAlpha = (float) (alpha * (1.0 - i * 0.1) * 0.4);
            renderModelPart(poseStack, vertexConsumer, "base", color, layerAlpha);

            poseStack.popPose();
        }
    }

    private void renderWaveEffect(PoseStack poseStack, MultiBufferSource bufferSource,
                                 EntitySuperDimension entity, int color, double alpha, float partialTick) {

        VertexConsumer vertexConsumer = bufferSource.getBuffer(SUPER_DIMENSION_RENDER_TYPE);

        Color col = new Color(color);
        float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);

        int loop = 6;
        for (int i = 0; i < loop; i++) {
            poseStack.pushPose();
            float ticks = 15.0F;
            float wave = ((entity.tickCount + ticks / loop * i + partialTick) % ticks);
            double waveScale = 1.0 + 0.03 * wave;
            poseStack.scale((float)waveScale, (float)waveScale, (float)waveScale);
            float[] hsbModified = Arrays.copyOf(hsb, hsb.length);
            hsbModified[0] = (hsbModified[0] + (float)i / loop) % 1.0F;
            int modifiedColor = Color.HSBtoRGB(hsbModified[0], hsbModified[1], hsbModified[2]);
            float waveAlpha = (float) (alpha * 0.7);
            renderModelPart(poseStack, vertexConsumer, "base", modifiedColor, waveAlpha);
            poseStack.popPose();
        }
    }

    private void renderModelPart(PoseStack poseStack, VertexConsumer consumer, String partName,
                                 int color, float alpha) {
        if (model == null) {
            float red = ((color >> 16) & 0xFF) / 255.0F;
            float green = ((color >> 8) & 0xFF) / 255.0F;
            float blue = (color & 0xFF) / 255.0F;
            renderFallbackCube(poseStack, consumer, red, green, blue, alpha);
            return;
        }
        float red = ((color >> 16) & 0xFF) / 255.0F;
        float green = ((color >> 8) & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        PoseStack.Pose pose = poseStack.last();
        for (GroupObject group : model.groupObjects) {
            if (partName.equals("base") || group.name.equals(partName)) {
                for (Face face : group.faces) {
                    if (face.vertices.length >= 3) {
                        for (int i = 0; i < face.vertices.length - 2; i++) {
                            renderVertex(consumer, pose, face.vertices[0], red, green, blue, alpha);
                            renderVertex(consumer, pose, face.vertices[i + 1], red, green, blue, alpha);
                            renderVertex(consumer, pose, face.vertices[i + 2], red, green, blue, alpha);
                        }
                    }
                }
            }
        }
    }

    private void renderVertex(VertexConsumer consumer, PoseStack.Pose pose,
                              Vertex vertex, float red, float green, float blue, float alpha) {
        consumer.vertex(pose.pose(), vertex.x, vertex.y, vertex.z)
                .color(red, green, blue, alpha)
                .endVertex();
    }

    private void renderFallbackCube(PoseStack poseStack, VertexConsumer consumer,
                                   float red, float green, float blue, float alpha) {
        PoseStack.Pose pose = poseStack.last();
        float size = 2.0F;
        consumer.vertex(pose.pose(), -size, -size, size).color(red, green, blue, alpha).endVertex();
        consumer.vertex(pose.pose(), size, -size, size).color(red, green, blue, alpha).endVertex();
        consumer.vertex(pose.pose(), size, size, size).color(red, green, blue, alpha).endVertex();
        consumer.vertex(pose.pose(), size, size, size).color(red, green, blue, alpha).endVertex();
        consumer.vertex(pose.pose(), -size, size, size).color(red, green, blue, alpha).endVertex();
        consumer.vertex(pose.pose(), -size, -size, size).color(red, green, blue, alpha).endVertex();
        consumer.vertex(pose.pose(), -size, -size, -size).color(red, green, blue, alpha).endVertex();
        consumer.vertex(pose.pose(), -size, size, -size).color(red, green, blue, alpha).endVertex();
        consumer.vertex(pose.pose(), size, size, -size).color(red, green, blue, alpha).endVertex();
        consumer.vertex(pose.pose(), size, size, -size).color(red, green, blue, alpha).endVertex();
        consumer.vertex(pose.pose(), size, -size, -size).color(red, green, blue, alpha).endVertex();
        consumer.vertex(pose.pose(), -size, -size, -size).color(red, green, blue, alpha).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySuperDimension entity) {
        return TEXTURE_LOCATION;
    }

    @Override
    public boolean shouldRender(EntitySuperDimension entity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }
}