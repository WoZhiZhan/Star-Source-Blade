package com.wzz.starsource.mixin;

import com.wzz.starsource.item.base.BaseBladeItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapelessRecipe.class)
public class ShapelessRecipeMixin {

    @Inject(method = "assemble(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"), cancellable = true)
    private void modifyAssemble(CraftingContainer container, RegistryAccess access, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = cir.getReturnValue();
        if (result.getItem() instanceof BaseBladeItem bladeItem) {
            ItemStack configuredStack = bladeItem.createConfiguredStack();
            configuredStack.setCount(result.getCount());
            cir.setReturnValue(configuredStack);
        }
    }
}
