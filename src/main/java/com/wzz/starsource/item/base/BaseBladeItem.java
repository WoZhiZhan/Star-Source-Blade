package com.wzz.starsource.item.base;

import com.wzz.starsource.ModMain;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.item.ItemTierSlashBlade;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class BaseBladeItem extends ItemSlashBlade {
	protected int useTick;
	private final float baseDamage;
	private final float attackDamage;
	private final int uses;
	private boolean setModel;
	private final ResourceLocation slashArts;
	public BaseBladeItem(int uses, float damage, int attackDamage) {
		this(uses, damage, attackDamage, SlashArtsRegistry.VOID_SLASH.getId());
	}

	public BaseBladeItem(int uses, float damage, int attackDamage, ResourceLocation slashArts) {
		this(uses, damage, attackDamage, slashArts, Rarity.COMMON);
	}

	public BaseBladeItem(int uses, float damage, int attackDamage, ResourceLocation slashArts, Rarity rarity) {
		super(new ItemTierSlashBlade(uses, damage),
				attackDamage,-2.4f,new Item.Properties().stacksTo(1).rarity(rarity));
		this.uses = uses;
		this.baseDamage = damage;
		this.attackDamage = attackDamage;
		this.slashArts = slashArts;
	}

	public abstract String getModelName();
	public abstract String getTextureName();

	public ResourceLocation getModel() {
		return ResourceLocation.tryBuild(ModMain.MODID,"model/"+getModelName()+".obj");
	}

	public ResourceLocation getTexture() {
		return ResourceLocation.tryBuild(ModMain.MODID,"model/"+getTextureName()+".png");
	}

	public ItemStack createConfiguredStack() {
		ItemStack stack = new ItemStack(this);
		stack.enchant(Enchantments.SHARPNESS, 1);
		stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> {
			s.setModel(this.getModel());
			s.setTexture(this.getTexture());
			s.setBaseAttackModifier(this.baseDamage);
			s.setAttackAmplifier(this.attackDamage);
			s.setMaxDamage(this.uses);
			s.setSlashArtsKey(this.slashArts);
			s.setDefaultBewitched(true);
		});
		return stack;
	}

	@Override
	public @NotNull ItemStack getDefaultInstance() {
		return this.createConfiguredStack();
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
		if (!this.setModel) {
			stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> {
				s.setModel(this.getModel());
				s.setTexture(this.getTexture());
			});
			this.setModel = true;
		}
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		this.useTick = 0;
		return super.use(worldIn, playerIn, handIn);
	}

	@Override
	public void onUseTick(Level level, LivingEntity player, ItemStack stack, int count) {
		super.onUseTick(level, player, stack, count);
		this.useTick++;
	}

	@Override
	public int getBarColor(ItemStack stack) {
		float durability = (float)(getMaxDamage(stack) - stack.getDamageValue()) / getMaxDamage(stack);
		return Mth.hsvToRgb(0.6F, 1.0F, 0.7F + durability * 0.3F);
	}
}
