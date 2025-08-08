package net.wzz.starsource.item;

import mods.flammpfeil.slashblade.ItemSlashBladeNamed;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.starsource.load.LoadedSlash;

import java.util.List;

public class ItemStarSourceSlashBlade extends ItemSlashBladeNamed {

    public final String registryName;
    public ItemStack stack;

    public ItemStarSourceSlashBlade(String registryName) {
        super(ToolMaterial.IRON, 4.0f);
        this.registryName = registryName;
        this.setUnlocalizedName(registryName);
        this.setCreativeTab(SlashBlade.tab);
        this.setRegistryName(registryName);
        ForgeRegistries.ITEMS.register(this);
        LoadedSlash.allSlashblade.add(this);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return getMaxDamage();
    }

    @Override
    public String getUnlocalizedName() {
        return super.getUnlocalizedName();
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return getUnlocalizedName();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab))
        {
            items.add(stack.copy());
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformationSwordClass(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {

    }

    @Override
    public void addInformation(ItemStack par1ItemStack, World world, List par3List, ITooltipFlag inFlag) {
        super.addInformation(par1ItemStack, world, par3List, inFlag);
    }
}