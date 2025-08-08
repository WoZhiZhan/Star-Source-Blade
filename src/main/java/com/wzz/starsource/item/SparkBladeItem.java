package com.wzz.starsource.item;

import com.wzz.starsource.init.ModSlashArts;
import com.wzz.starsource.item.base.BaseBladeItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SparkBladeItem extends BaseBladeItem {
    public SparkBladeItem() {
        super(300,17f, 20, ModSlashArts.SPARK.getId(), Rarity.UNCOMMON);
    }

    @Override
    public String getModelName() {
        return "base";
    }

    @Override
    public String getTextureName() {
        return "spark";
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable("spark_blade.text.1"));
    }
}
