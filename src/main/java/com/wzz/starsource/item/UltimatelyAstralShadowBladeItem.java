package com.wzz.starsource.item;

import com.wzz.starsource.init.ModSlashArts;
import com.wzz.starsource.item.base.BaseBladeItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UltimatelyAstralShadowBladeItem extends BaseBladeItem {
    public UltimatelyAstralShadowBladeItem() {
        super(350,15f, 20, ModSlashArts.ULTIMATELY_ASTRAL_SHADOW.getId());
    }

    @Override
    public String getModelName() {
        return "base";
    }

    @Override
    public String getTextureName() {
        return "ultimately_astral_shadow";
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable("super_astral_shadow_blade.text.1"));
    }
}
