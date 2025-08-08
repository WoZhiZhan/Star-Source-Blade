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
import net.wzz.starsource.load.specialAttacks.StarDragonSpecialAttacks;

import javax.annotation.Nonnull;
import java.util.Random;

public class ItemStarDragonSlashBlade extends ItemStarSourceSlashBlade {

    public ItemStarDragonSlashBlade() {
        super("star_dragon");
        ItemStack itemStack = new ItemStack(this);
        NBTTagCompound tag = new NBTTagCompound();
        itemStack.setTagCompound(tag);
        itemStack.addEnchantment(Enchantments.INFINITY, 100);
        CurrentItemName.set(tag, "star_dragon");
        IsDefaultBewitched.set(tag, true);
        BaseAttackModifier.set(tag, 15f);
        AttackAmplifier.set(tag, 100f);
        KillCount.set(tag, 100);
        ProudSoul.set(tag, 100);
        RepairCount.set(tag, 100);
        SummonedSwordColor.set(tag, new Random().nextInt(256));
        SlashBlade.registerCustomItemStack("star_dragon", itemStack);
        this.setMaxDamage(10000);
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
        list.add("§e万古神龙的残躯寄宿在此剑中");
        list.add("§d它随时可能苏醒");
        list.add("§b记住！不要吵醒它");
        list.add("§aDragon Power");
    }

    private static ResourceLocationRaw texture = new ResourceLocationRaw("flammpfeil.slashblade", "model/named/starsource/star_dragon.png");

    public ResourceLocationRaw getModelTexture() {
        return texture;
    }

    private final ResourceLocationRaw model = new ResourceLocationRaw("flammpfeil.slashblade", "model/named/starsource/dragon.obj");

    public ResourceLocationRaw getModel() {
        return this.model;
    }

    @Override
    public void onUpdate(ItemStack sitem, World world, Entity entity, int indexOfMainSlot, boolean isCurrent) {
        super.onUpdate(sitem, world, entity, indexOfMainSlot, isCurrent);
        if (!sitem.isItemEnchanted()) {
            sitem.addEnchantment(Enchantments.INFINITY, 100);
        }
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return "§l§e星神龙拔刀剑";
    }

    @Override
    public SpecialAttackBase getSpecialAttack(ItemStack stack) {
        return StarDragonSpecialAttacks.theSa;
    }
}
