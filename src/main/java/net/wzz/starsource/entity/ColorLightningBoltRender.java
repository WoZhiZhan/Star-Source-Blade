package net.wzz.starsource.entity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class ColorLightningBoltRender
        extends Render<EntityColorLightningBolt>
{
    public ColorLightningBoltRender(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }
    public void doRender(EntityColorLightningBolt entity, double x, double y, double z, float entityYaw, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        double[] adouble = new double[8];
        double[] adouble1 = new double[8];
        double d0 = 0.0D;
        double d1 = 0.0D;
        Random random = new Random(entity.boltVertex);
        for (int i = 7; i >= 0; --i) {
            adouble[i] = d0;
            adouble1[i] = d1;
            d0 += (double) (random.nextInt(11) - 5);
            d1 += (double) (random.nextInt(11) - 5);
        }
        for (int k1 = 0; k1 < 4; k1++)
        {
            Random random2 = new Random(entity.boltVertex);
            for (int j = 0; j < 3; j++)
            {
                int k = 7;
                int l = 0;
                if (j > 0) {
                    k = 7 - j;
                }
                if (j > 0) {
                    l = k - 2;
                }
                double d2 = adouble[k] - d0;
                double d3 = adouble1[k] - d1;
                for (int i1 = k; i1 >= l; i1--)
                {
                    double d4 = d2;
                    double d5 = d3;
                    if (j == 0)
                    {
                        d2 += random2.nextInt(11) - 5;
                        d3 += random2.nextInt(11) - 5;
                    }
                    else
                    {
                        d2 += random2.nextInt(31) - 15;
                        d3 += random2.nextInt(31) - 15;
                    }
                    bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
                    float f = random.nextFloat();
                    float f1 = random.nextFloat();
                    float f2 = random.nextFloat();
                    double d6 = 0.1D + k1 * 0.2D;
                    if (j == 0) {
                        d6 *= (i1 * 0.1D + 1.0D);
                    }
                    double d7 = 0.1D + k1 * 0.2D;
                    if (j == 0) {
                        d7 *= ((i1 - 1) * 0.1D + 1.0D);
                    }
                    for (int j1 = 0; j1 < 5; j1++)
                    {
                        double d8 = 0.5D + x - d6;
                        double d9 = 0.5D + z - d6;
                        if ((j1 == 1) || (j1 == 2)) {
                            d8 += 2.0D * d6;
                        }
                        if ((j1 == 2) || (j1 == 3)) {
                            d9 += 2.0D * d6;
                        }
                        double d10 = 0.5D + x - d7;
                        double d11 = 0.5D + z - d7;
                        if ((j1 == 1) || (j1 == 2)) {
                            d10 += 2.0D * d7;
                        }
                        if ((j1 == 2) || (j1 == 3)) {
                            d11 += 2.0D * d7;
                        }
                        Random randCol = new Random();
                        bufferbuilder.pos(d10 + d2, i1 * 16 + y, d11 + d3).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        bufferbuilder.pos(d8 + d4, (i1 + 1) * 16 + y, d9 + d5).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                    }
                    tessellator.draw();
                }
            }
        }
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
    }

    protected ResourceLocation getEntityTexture(EntityColorLightningBolt entity)
    {
        return null;
    }

    public Render createRenderFor(RenderManager manager)
    {
        return this;
    }
}

