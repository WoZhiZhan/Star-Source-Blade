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
import net.wzz.starsource.load.specialAttacks.StarSpecialAttacks;

import javax.annotation.Nonnull;
import java.util.Random;

public class ItemStarSlashBlade extends ItemStarSourceSlashBlade {

    public ItemStarSlashBlade() {
        super("star");
        ItemStack itemStack = new ItemStack(this);
        NBTTagCompound tag = new NBTTagCompound();
        itemStack.setTagCompound(tag);
        itemStack.addEnchantment(Enchantments.INFINITY, 1);
        CurrentItemName.set(tag, "star");
        IsDefaultBewitched.set(tag, true);
        BaseAttackModifier.set(tag, 15f);
        KillCount.set(tag, 1);
        ProudSoul.set(tag, 1);
        RepairCount.set(tag, 1);
        SummonedSwordColor.set(tag, new Random().nextInt(256));
        SlashBlade.registerCustomItemStack("star", itemStack);
        this.setMaxDamage(1500);
        this.stack = itemStack;
        setCreativeTab(TabStarSource.tab);
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
        list.add("§7星源拔刀剑的最基础");
    }

    private static ResourceLocationRaw texture = new ResourceLocationRaw("flammpfeil.slashblade", "model/named/starsource/star.png");

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
            sitem.addEnchantment(Enchantments.INFINITY, 1);
        }
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return "星源拔刀剑";
    }

    @Override
    public SpecialAttackBase getSpecialAttack(ItemStack stack) {
        return StarSpecialAttacks.theSa;
    }
}
