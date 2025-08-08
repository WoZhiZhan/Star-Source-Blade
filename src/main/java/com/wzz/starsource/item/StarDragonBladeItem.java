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

public class StarDragonBladeItem extends BaseBladeItem {
    public StarDragonBladeItem() {
        super(2000,100f, 30, ModSlashArts.STAR_DRAGON.getId(), Rarity.EPIC);
    }

    @Override
    public String getModelName() {
        return "base2";
    }

    @Override
    public String getTextureName() {
        return "star_dragon";
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable("star_dragon.text.1"));
        tooltip.add(Component.translatable("star_dragon.text.2"));
        tooltip.add(Component.translatable("star_dragon.text.3"));
    }
}
