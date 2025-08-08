package net.wzz.starsource.item;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.starsource.RainbowText;
import net.wzz.starsource.creativetab.TabStarSource;
import net.wzz.starsource.load.specialAttacks.AnnihilationSpecialAttacks;

import java.util.Random;

public class ItemAnnihilateSlashBlade extends ItemStarSourceSlashBlade {
    public static float scale;
    private boolean start = false;
    public static boolean secondStart = false;
    public static boolean boom = false;
    public ItemAnnihilateSlashBlade() {
        super("annihilate");
        ItemStack itemStack = new ItemStack(this);
        NBTTagCompound tag = new NBTTagCompound();
        itemStack.setTagCompound(tag);
        itemStack.addEnchantment(Enchantments.INFINITY, 100);
        CurrentItemName.set(tag, "annihilate");
        IsDefaultBewitched.set(tag, true);
        BaseAttackModifier.set(tag, 15f);
        AttackAmplifier.set(tag, 80f);
        KillCount.set(tag, 1);
        ProudSoul.set(tag, 1);
        RepairCount.set(tag, 1);
        SummonedSwordColor.set(tag, new Random().nextInt(256));
        SlashBlade.registerCustomItemStack("annihilate", itemStack);
        this.setMaxDamage(3200);
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
        list.add(RainbowText.makeColour("万物湮灭！"));
        list.add(RainbowText.makeColour("By 湮灭之力..."));
    }

    private static ResourceLocationRaw texture = new ResourceLocationRaw("flammpfeil.slashblade", "model/named/starsource/annihilate.png");

    public ResourceLocationRaw getModelTexture() {
        return texture;
    }

    private final ResourceLocationRaw model = new ResourceLocationRaw("flammpfeil.slashblade", "model/named/starsource/at.obj");

    public ResourceLocationRaw getModel() {
        return this.model;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        scale = 0.0001f;
        start = true;
        secondStart = false;
        boom = false;
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void onUpdate(ItemStack sitem, World world, Entity entity, int indexOfMainSlot, boolean isCurrent) {
        super.onUpdate(sitem, world, entity, indexOfMainSlot, isCurrent);
        if (!sitem.isItemEnchanted()) {
            sitem.addEnchantment(Enchantments.INFINITY, 100);
        }
        if (start) {
            scale += 0.001f;
            if (scale >= 0.1f) {
                secondStart = true;
                start = false;
            }
        }
        if (secondStart) {
            scale -= 0.001f;
            if (scale <= 0.0001f) {
                boom = true;
                secondStart = false;
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return "§l§e湮灭拔刀剑";
    }

    @Override
    public SpecialAttackBase getSpecialAttack(ItemStack stack) {
        return AnnihilationSpecialAttacks.theSa;
    }
}
