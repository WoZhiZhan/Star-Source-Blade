package com.wzz.starsource.init;

import com.wzz.starsource.ModMain;
import com.wzz.starsource.entity.EntityDyingDimension;
import com.wzz.starsource.entity.EntityMovingDrive;
import com.wzz.starsource.entity.EntitySuperDimension;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ModMain.MODID);

    public static final RegistryObject<EntityType<EntityMovingDrive>> MOVING_DRIVE =
            REGISTRY.register("moving_drive", () ->
                    EntityType.Builder.<EntityMovingDrive>of(EntityMovingDrive::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("moving_drive"));
    public static final RegistryObject<EntityType<EntitySuperDimension>> SUPER_DIMENSION =
            REGISTRY.register("super_dimension", () -> EntityType.Builder.<EntitySuperDimension>of(
                            EntitySuperDimension::new, MobCategory.MISC)
                    .sized(4.0F, 4.0F)
                    .build("super_dimension"));
    public static final RegistryObject<EntityType<EntityDyingDimension>> DYING_DIMENSION =
            REGISTRY.register("dying_dimension", () -> EntityType.Builder.<EntityDyingDimension>of(
                            EntityDyingDimension::new, MobCategory.MISC)
                    .sized(2.0F, 2.0F)
                    .build("dying_dimension"));
    public static final RegistryObject<EntityType<EntityMovingDrive>> TRACKING_STORM_SWORDS =
            REGISTRY.register("tracking_storm_swords", () ->
                    EntityType.Builder.<EntityMovingDrive>of(EntityMovingDrive::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("tracking_storm_swords"));
}
