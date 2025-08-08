package net.wzz.starsource.item;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.starsource.creativetab.TabStarSource;
import net.wzz.starsource.load.specialAttacks.AstralShadowSpecialAttacks;
import net.wzz.starsource.load.specialAttacks.SuperAstralShadowSpecialAttacks;

import javax.annotation.Nonnull;
import java.util.Random;

public class ItemSuperAstralShadowSlashBlade extends ItemStarSourceSlashBlade {

    public ItemSuperAstralShadowSlashBlade() {
        super("super_astral_shadow");
        ItemStack itemStack = new ItemStack(this);
        NBTTagCompound tag = new NBTTagCompound();
        itemStack.setTagCompound(tag);
        itemStack.addEnchantment(Enchantments.INFINITY, 5);
        CurrentItemName.set(tag, "super_astral_shadow");
        IsDefaultBewitched.set(tag, true);
        BaseAttackModifier.set(tag, 15f);
        AttackAmplifier.set(tag, 20f);
        KillCount.set(tag, 1);
        ProudSoul.set(tag, 1);
        RepairCount.set(tag, 1);
        SummonedSwordColor.set(tag, new Random().nextInt(256));
        SlashBlade.registerCustomItemStack("super_astral_shadow", itemStack);
        this.setMaxDamage(4000);
        this.stack = itemStack;
        setCreativeTab(TabStarSource.tab);
    }

    @Override
    public float getAttackDamage() {
        return 8;
    }

    @SideOnly(Side.CLIENT)
    public void addInformationSwordClass(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, java.util.List par3List, boolean par4) {
    }

    @SideOnly(Side.CLIENT)
    public void addInformationMaxAttack(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, java.util.List list, boolean par4) {
        NBTTagCompound tag = getItemTagCompound(par1ItemStack);
        RepairCount.get(tag);
        getSwordType(par1ItemStack);
        list.add("");
        list.add("§7真·万箭穿心");
    }

    private static ResourceLocationRaw texture = new ResourceLocationRaw("flammpfeil.slashblade", "model/named/starsource/super_astral_shadow.png");

    public ResourceLocationRaw getModelTexture() {
        return texture;
    }

    private final ResourceLocationRaw model = new ResourceLocationRaw("flammpfeil.slashblade", "model/named/starsource/at.obj");

    public ResourceLocationRaw getModel() {
        return this.model;
    }

    @Override
    public void onUpdate(ItemStack sitem, World world, Entity entity, int indexOfMainSlot, boolean isCurrent) {
        super.onUpdate(sitem, world, entity, indexOfMainSlot, isCurrent);
        if (!sitem.isItemEnchanted()) {
            sitem.addEnchantment(Enchantments.INFINITY, 5);
        }
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return "§d超级星影拔刀剑";
    }

    @Override
    public SpecialAttackBase getSpecialAttack(ItemStack stack) {
        return SuperAstralShadowSpecialAttacks.theSa;
    }
}
