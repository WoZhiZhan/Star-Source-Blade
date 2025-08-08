package com.wzz.starsource.init;

import com.wzz.starsource.ModMain;
import com.wzz.starsource.api.RegisterToCreativeTab;
import com.wzz.starsource.item.base.BaseBladeItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModMain.MODID);

	public static final RegistryObject<CreativeModeTab> starsource = REGISTRY.register("starsource",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.starsource.starsource")).
					icon(() -> ModItems.STAR_SOURCE_BLADE.get().getDefaultInstance()).displayItems((parameters, tabData) -> {
						safeGetRegisteredItems().forEach(item -> {
							ItemStack stack = item.get().getDefaultInstance();
							if (stack.getItem() instanceof BaseBladeItem bladeItem)
								stack = bladeItem.createConfiguredStack();
							tabData.accept(stack);
						});
					})

					.build());

	private static List<Supplier<? extends Item>> safeGetRegisteredItems() {
		List<Supplier<? extends Item>> validItems = new ArrayList<>();
		for (Field field : ModItems.class.getDeclaredFields()) {
			if (field.isAnnotationPresent(RegisterToCreativeTab.class)) {
				try {
					RegistryObject<?> regObj = (RegistryObject<?>) field.get(null);
					if (regObj != null && regObj.isPresent() && regObj.get() instanceof Item) {
						validItems.add(() -> (Item) regObj.get());
					}
				} catch (IllegalAccessException ignored) {
				}
			}
		}
		return validItems;
	}
}
