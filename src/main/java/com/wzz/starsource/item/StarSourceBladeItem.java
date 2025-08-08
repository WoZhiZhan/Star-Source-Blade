package com.wzz.starsource.item;

import com.wzz.starsource.init.ModSlashArts;
import com.wzz.starsource.item.base.BaseBladeItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StarSourceBladeItem extends BaseBladeItem {
    public StarSourceBladeItem() {
        super(200,10f, 20, ModSlashArts.STAR_SOURCE.getId());
    }

    @Override
    public String getModelName() {
        return "base";
    }

    @Override
    public String getTextureName() {
        return "starsource";
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable("star_source_blade.text.1"));
    }
}
