package com.wzz.starsource.init;

import com.wzz.starsource.renderer.RenderDyingDimension;
import com.wzz.starsource.renderer.RenderSuperDimension;
import mods.flammpfeil.slashblade.client.renderer.entity.DriveRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.MOVING_DRIVE.get(), DriveRenderer::new);
		event.registerEntityRenderer(ModEntities.SUPER_DIMENSION.get(), RenderSuperDimension::new);
		event.registerEntityRenderer(ModEntities.DYING_DIMENSION.get(), RenderDyingDimension::new);
	}
}
