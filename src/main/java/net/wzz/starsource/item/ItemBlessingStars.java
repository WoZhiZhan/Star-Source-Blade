
package net.wzz.starsource.item;

import net.wzz.starsource.creativetab.TabStarSource;
import net.wzz.starsource.ElementsStarsourceMod;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;

import java.util.List;

@ElementsStarsourceMod.ModElement.Tag
public class ItemBlessingStars extends ElementsStarsourceMod.ModElement {
	@GameRegistry.ObjectHolder("starsource:blessing_stars")
	public static final Item block = null;
	public ItemBlessingStars(ElementsStarsourceMod instance) {
		super(instance, 3);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("starsource:blessing_stars", "inventory"));
	}
	public static class ItemCustom extends Item {
		public ItemCustom() {
			setMaxDamage(0);
			maxStackSize = 1;
			setUnlocalizedName("blessing_stars");
			setRegistryName("blessing_stars");
			setCreativeTab(TabStarSource.tab);
		}

		@Override
		public int getItemEnchantability() {
			return 0;
		}

		@Override
		public int getMaxItemUseDuration(ItemStack itemstack) {
			return 0;
		}

		@Override
		public float getDestroySpeed(ItemStack par1ItemStack, IBlockState par2Block) {
			return 1F;
		}

		@Override
		public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
			super.addInformation(itemstack, world, list, flag);
			list.add("\u00A77\u624B\u6301\u6B64\u7269\u54C1\u6B7B\u4EA1\u65F6\u5C06\u53D6\u6D88\u6B7B\u4EA1");
			list.add("\u00A77\u5E76\u4E14\u83B7\u5F97\u4E00\u4E2A\u661F\u6E90\u4E4B\u952D");
		}
	}
}
