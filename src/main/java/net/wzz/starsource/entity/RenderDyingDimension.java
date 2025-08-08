//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.wzz.starsource.entity;

import mods.flammpfeil.slashblade.client.model.obj.Face;
import mods.flammpfeil.slashblade.client.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.entity.EntitySlashDimension;
import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.starsource.item.ItemDyingFireSlashBlade;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class RenderDyingDimension extends Render {
    public static WavefrontObject model = null;
    public static ResourceLocationRaw modelLocation = new ResourceLocationRaw("flammpfeil.slashblade", "model/util/slashdim.obj");
    public static ResourceLocationRaw textureLocation = new ResourceLocationRaw("flammpfeil.slashblade", "model/util/slashdim.png");

    public RenderDyingDimension(RenderManager renderManager) {
        super(renderManager);
    }

    private TextureManager engine() {
        return this.renderManager.renderEngine;
    }

    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialRenderTick) {
        if (this.renderOutlines) {
            GlStateManager.disableLighting();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableColorMaterial();
            float cycleTicks = 40.0F;
            float b = Math.abs((float)entity.ticksExisted % cycleTicks / cycleTicks - 0.5F) + 0.5F;
            GlStateManager.enableOutlineMode(Color.getHSBColor(0.0F, 0.0F, b).getRGB());
        }

        this.renderModel(entity, x, y, z, yaw, partialRenderTick);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
            GlStateManager.enableLighting();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }

    }

    public void renderModel(Entity entity, double x, double y, double z, float yaw, float partialRenderTick) {
        if (model == null) {
            model = new WavefrontObject(modelLocation);
        }
        this.bindEntityTexture(entity);
        GL11.glPushMatrix();
        GL11.glPushAttrib(1048575);
        GL11.glShadeModel(7425);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        int color = 0xFF0000;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int darkenAmount = 50;
        r = Math.min(r + darkenAmount, 255);
        g = Math.min(g + darkenAmount, 255);
        b = Math.min(b + darkenAmount, 255);
        color = (r << 16) | (g << 8) | b;
        int lifetime = 20;
        if (entity instanceof EntityDyingDimension) {
            color = ((EntityDyingDimension)entity).getColor();
            lifetime = ((EntityDyingDimension)entity).getLifeTime();
        }
        boolean inverse = color < 0;
        double deathTime = (double)lifetime;
        double baseAlpha = Math.sin(1.5707963267948966 * (Math.min(deathTime, (double)((float)(lifetime - entity.ticksExisted) - partialRenderTick)) / deathTime));
        int seed = entity.getEntityData().getInteger("seed");
        Color col = new Color(color);
        float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), (float[])null);
        int baseColor = Color.getHSBColor(0.5F + hsb[0], hsb[1], 0.2F).getRGB() & 16777215;
        baseColor |= (int)(102.0 * baseAlpha) << 24;
        GL11.glTranslatef((float)x, (float)y, (float)z);
        float rotParTicks = 200.0F;
        float rot = (float)entity.ticksExisted % rotParTicks / rotParTicks * 360.0F + partialRenderTick * (360.0F / rotParTicks);
        float scale = ItemDyingFireSlashBlade.tick;
        GL11.glScalef(scale, scale, scale);
        float lastx = OpenGlHelper.lastBrightnessX;
        float lasty = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        Face.setColor(baseColor);
        OpenGlHelper.glBlendFunc(770, 1, 1, 0);
        GlStateManager.glBlendEquation(32779);
        GL11.glPushMatrix();
        for(int i = 0; i < 5; ++i) {
            GL11.glScaled(0.95, 0.95, 0.95);
            model.renderPart("base");
        }
        GL11.glPopMatrix();
        int loop = 3;
        for(int i = 0; i < loop; ++i) {
            GL11.glPushMatrix();
            float ticks = 15.0F;
            float wave = ((float)entity.ticksExisted + ticks / (float)loop * (float)i + partialRenderTick) % ticks;
            double waveScale = 1.0 + 0.03 * (double)wave;
            GL11.glScaled(waveScale, waveScale, waveScale);
            Face.setColor(baseColor & 16777215 | (int)(136.0F * ((ticks - wave) / ticks)) << 24);
            model.renderPart("base");
            GL11.glPopMatrix();
        }
        GlStateManager.glBlendEquation(32774);
        Face.resetColor();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastx, lasty);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glShadeModel(7424);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    protected ResourceLocationRaw getEntityTexture(Entity entity) {
        return textureLocation;
    }
}
