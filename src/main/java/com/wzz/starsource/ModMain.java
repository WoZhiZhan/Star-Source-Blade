package com.wzz.starsource;

import com.mojang.logging.LogUtils;
import com.wzz.starsource.api.CustomBladeModel;
import com.wzz.starsource.init.*;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.Objects;

@Mod(ModMain.MODID)
public class ModMain {

    public static final String MODID = "starsource";
    private static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings({"deprecation", "removal"})
    public ModMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.REGISTRY.register(modEventBus);
        ModSlashArts.REGISTRY.register(modEventBus);
        ModItems.REGISTRY.register(modEventBus);
        ModTabs.REGISTRY.register(modEventBus);
        ModNetwork.register();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        DistExecutor.runWhenOn(Dist.CLIENT,()->()-> modEventBus.addListener(this::Baked));
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @OnlyIn(Dist.CLIENT)
    private void Baked(final ModelEvent.ModifyBakingResult event) {
        Field[] fields = ModItems.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(CustomBladeModel.class)) {
                try {
                    RegistryObject<? extends Item> item = (RegistryObject<? extends Item>) field.get(null);
                    ModelResourceLocation loc = new ModelResourceLocation(
                            Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.get())),
                            "inventory"
                    );
                    event.getModels().put(loc, new BladeModel(event.getModels().get(loc), event.getModelBakery()));
                } catch (IllegalAccessException ignored) {
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
