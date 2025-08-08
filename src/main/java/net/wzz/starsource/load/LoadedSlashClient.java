package net.wzz.starsource.load;

import mods.flammpfeil.slashblade.tileentity.DummyTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.starsource.entity.*;
import net.wzz.starsource.item.*;

import static net.wzz.starsource.load.LoadedSlash.allSlashblade;

public class LoadedSlashClient {

    @SideOnly(Side.CLIENT)
    public static void registerSlashBlades() {
        for (ItemStarSourceSlashBlade itemStarSourceSlashBlade : allSlashblade) {
            ModelLoader.setCustomModelResourceLocation(itemStarSourceSlashBlade, 0, new ModelResourceLocation("flammpfeil.slashblade:model/named/blade.obj"));
            ForgeHooksClient.registerTESRItemStack(itemStarSourceSlashBlade, 0, DummyTileEntity.class);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerEntities() {
        RenderingRegistry.registerEntityRenderingHandler(EntitySuperDimension.class,(Render<? extends Entity>)new RenderSuperDimension(
                new RenderManager(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().getRenderItem()))
        );
        RenderingRegistry.registerEntityRenderingHandler(EntityDyingDimension.class,(Render<? extends Entity>)new RenderDyingDimension(
                new RenderManager(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().getRenderItem()))
        );
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonDimension.class,(Render<? extends Entity>)new RenderDragonDimension(
                new RenderManager(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().getRenderItem()))
        );
        RenderingRegistry.registerEntityRenderingHandler(EntityColorLightningBolt.class,
                new ColorLightningBoltRender(new RenderManager(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().getRenderItem()))
        );
        RenderingRegistry.registerEntityRenderingHandler(EntityAnnihilationDimension.class,(Render<? extends Entity>)new RenderAnnihilationDimension(
                new RenderManager(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().getRenderItem()))
        );
    }
}
