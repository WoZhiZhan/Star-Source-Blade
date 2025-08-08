package com.wzz.starsource.renderer;

import com.wzz.starsource.entity.EntitySuperDimension;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderDyingDimension extends RenderSuperDimension{
    public RenderDyingDimension(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public float scale(EntitySuperDimension superDimension) {
        return superDimension.tickCount * 0.001f;
    }

    @Override
    public float layerScale(EntitySuperDimension superDimension, int i) {
        return superDimension.tickCount * 0.001f;
    }
}
