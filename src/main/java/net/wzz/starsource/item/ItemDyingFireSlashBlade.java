package net.wzz.starsource.item;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.starsource.creativetab.TabStarSource;
import net.wzz.starsource.load.specialAttacks.DyingFireSpecialAttacks;

import javax.annotation.Nonnull;
import java.util.Random;

public class ItemDyingFireSlashBlade extends ItemStarSourceSlashBlade {
    public static float tick = 0.0001f;
    public static boolean addTick = false;

    public ItemDyingFireSlashBlade() {
        super("dying_fire");
        ItemStack itemStack = new ItemStack(this);
        NBTTagCompound tag = new NBTTagCompound();
        itemStack.setTagCompound(tag);
        itemStack.addEnchantment(Enchantments.INFINITY, 10);
        CurrentItemName.set(tag, "dying_fire");
        IsDefaultBewitched.set(tag, true);
        BaseAttackModifier.set(tag, 15f);
        AttackAmplifier.set(tag, 19f);
        KillCount.set(tag, 1);
        ProudSoul.set(tag, 1);
        RepairCount.set(tag, 1);
        SummonedSwordColor.set(tag, new Random().nextInt(256));
        SlashBlade.registerCustomItemStack("dying_fire", itemStack);
        this.setMaxDamage(4000);
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
        list.add("§eSSS/SSS/SSS");
        list.add("§c我们是天火！");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        addTick = false;
        tick = 0;
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
        addTick = true;
    }

    private static ResourceLocationRaw texture = new ResourceLocationRaw("flammpfeil.slashblade", "model/named/starsource/dying_fire.png");

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
            sitem.addEnchantment(Enchantments.INFINITY, 10);
        }
        if (addTick)
            tick += 0.0001;
        if (tick > 1)
            tick = 0;
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return "§e终火拔刀剑";
    }

    @Override
    public SpecialAttackBase getSpecialAttack(ItemStack stack) {
        return DyingFireSpecialAttacks.theSa;
    }
}
