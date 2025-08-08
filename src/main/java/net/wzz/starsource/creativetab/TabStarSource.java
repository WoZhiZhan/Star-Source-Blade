
package net.wzz.starsource.creativetab;

import net.wzz.starsource.item.ItemStarSourceInght;
import net.wzz.starsource.ElementsStarsourceMod;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;

@ElementsStarsourceMod.ModElement.Tag
public class TabStarSource extends ElementsStarsourceMod.ModElement {
	public TabStarSource(ElementsStarsourceMod instance) {
		super(instance, 2);
	}

	@Override
	public void initElements() {
		tab = new CreativeTabs("tabstar_source") {
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(ItemStarSourceInght.block, (int) (1));
			}

			@SideOnly(Side.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
	}
	public static CreativeTabs tab;
}
