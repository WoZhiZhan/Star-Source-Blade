package com.wzz.starsource.init;

import com.wzz.starsource.ModMain;
import com.wzz.starsource.slasharts.*;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModSlashArts {
    public static final DeferredRegister<SlashArts> REGISTRY = DeferredRegister.create(SlashArts.REGISTRY_KEY, ModMain.MODID);

    public static final RegistryObject<SlashArts> STAR_SOURCE = REGISTRY.register("starsource",
            () -> new StarSourceSlashArts((e) -> ComboStateRegistry.NONE.getId()));

    public static final RegistryObject<SlashArts> ASTRAL_SHADOW = REGISTRY.register("astral_shadow",
            () -> new AstralShadowSlashArts((e) -> ComboStateRegistry.NONE.getId()));

    public static final RegistryObject<SlashArts> SPARK = REGISTRY.register("spark",
            () -> new SparkSlashArts((e) -> ComboStateRegistry.NONE.getId()));

    public static final RegistryObject<SlashArts> STAR_BURST = REGISTRY.register("star_burst",
            () -> new StarBurstSlashArts((e) -> ComboStateRegistry.NONE.getId()));

    public static final RegistryObject<SlashArts> DYING_FIRE = REGISTRY.register("dying_fire",
            () -> new DyingFireSlashArts((e) -> ComboStateRegistry.NONE.getId()));

    public static final RegistryObject<SlashArts> SUPER_ASTRAL_SHADOW = REGISTRY.register("super_astral_shadow",
            () -> new SuperAstralShadowSlashArts((e) -> ComboStateRegistry.NONE.getId()));

    public static final RegistryObject<SlashArts> ULTIMATE_ASTRAL_SHADOW = REGISTRY.register("ultimate_astral_shadow",
            () -> new UltimateAstralShadowSlashArts((e) -> ComboStateRegistry.NONE.getId()));

    public static final RegistryObject<SlashArts> ULTIMATELY_ASTRAL_SHADOW = REGISTRY.register("ultimately_astral_shadow",
            () -> new UltimatelyAstralShadowSlashArts((e) -> ComboStateRegistry.NONE.getId()));

    public static final RegistryObject<SlashArts> FROST_MOON = REGISTRY.register("frost_moon",
            () -> new FrostMoonSlashArts((e) -> ComboStateRegistry.NONE.getId()));

    public static final RegistryObject<SlashArts> SCHRODINGER = REGISTRY.register("schrodinger",
            () -> new SchrodingerSlashArts((e) -> ComboStateRegistry.NONE.getId()));

    public static final RegistryObject<SlashArts> STAR_DRAGON = REGISTRY.register("star_dragon",
            () -> new StarDragonSlashArts((e) -> ComboStateRegistry.NONE.getId()));
}
