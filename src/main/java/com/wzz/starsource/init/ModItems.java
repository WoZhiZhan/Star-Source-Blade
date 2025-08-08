package com.wzz.starsource.init;

import com.wzz.starsource.ModMain;
import com.wzz.starsource.api.CustomBladeModel;
import com.wzz.starsource.api.RegisterToCreativeTab;
import com.wzz.starsource.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ModMain.MODID);

	@RegisterToCreativeTab
	public static final RegistryObject<Item> BLESSING_STARS = REGISTRY.register("blessing_stars", BlessingStarsItem::new);

	@RegisterToCreativeTab
	public static final RegistryObject<Item> STAR_SOURCE_INGOT = REGISTRY.register("star_source_ingot", ()
			-> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

	@CustomBladeModel
	@RegisterToCreativeTab
	public static final RegistryObject<Item> STAR_SOURCE_BLADE = REGISTRY.register("star_source_blade", StarSourceBladeItem::new);

	@CustomBladeModel
	@RegisterToCreativeTab
	public static final RegistryObject<Item> ASTRAL_SHADOW_BLADE = REGISTRY.register("astral_shadow_blade", AstralShadowBladeItem::new);

	@CustomBladeModel
	@RegisterToCreativeTab
	public static final RegistryObject<Item> SPARK_BLADE = REGISTRY.register("spark_blade", SparkBladeItem::new);

	@CustomBladeModel
	@RegisterToCreativeTab
	public static final RegistryObject<Item> STAR_BURST = REGISTRY.register("star_burst_blade", StarBurstBladeItem::new);

	@CustomBladeModel
	@RegisterToCreativeTab
	public static final RegistryObject<Item> DYING_FIRE = REGISTRY.register("dying_fire_blade", DyingFireBladeItem::new);

	@CustomBladeModel
	@RegisterToCreativeTab
	public static final RegistryObject<Item> SUPER_ASTRAL_SHADOW_BLADE = REGISTRY.register("super_astral_shadow_blade", SuperAstralShadowBladeItem::new);

	@CustomBladeModel
	@RegisterToCreativeTab
	public static final RegistryObject<Item> ULTIMATE_ASTRAL_SHADOW_BLADE = REGISTRY.register("ultimate_astral_shadow_blade", UltimateAstralShadowBladeItem::new);

	@CustomBladeModel
	@RegisterToCreativeTab
	public static final RegistryObject<Item> ULTIMATELY_ASTRAL_SHADOW_BLADE = REGISTRY.register("ultimately_astral_shadow_blade", UltimatelyAstralShadowBladeItem::new);

	@CustomBladeModel
	@RegisterToCreativeTab
	public static final RegistryObject<Item> FROST_MOON_BLADE = REGISTRY.register("frost_moon_blade", FrostMoonBladeItem::new);

	@CustomBladeModel
	@RegisterToCreativeTab
	public static final RegistryObject<Item> SCHRODINGER_BLADE = REGISTRY.register("schrodinger_blade", SchrodingerBladeItem::new);

	@CustomBladeModel
	@RegisterToCreativeTab
	public static final RegistryObject<Item> STAR_DRAGON_BLADE = REGISTRY.register("star_dragon_blade", StarDragonBladeItem::new);
}