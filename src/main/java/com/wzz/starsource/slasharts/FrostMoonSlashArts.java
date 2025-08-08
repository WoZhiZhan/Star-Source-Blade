package com.wzz.starsource.slasharts;

import com.wzz.starsource.util.StormSwordSpawner;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.Function;

public class FrostMoonSlashArts extends SlashArts {
    public FrostMoonSlashArts(Function<LivingEntity, ResourceLocation> state) {
        super(state);
    }

    @Override
    public ResourceLocation doArts(ArtsType type, LivingEntity user) {
        Level world = user.level();
        ItemStack stack = user.getMainHandItem();
        LazyOptional<ISlashBladeState> stateOpt = stack.getCapability(ItemSlashBlade.BLADESTATE);
        stateOpt.ifPresent(state -> {
            final int cost = 10;
            if (state.getProudSoulCount() >= cost) {
                state.setProudSoulCount(state.getProudSoulCount() - cost);
            } else {
                stack.hurtAndBreak(10, user, (entity) -> entity.broadcastBreakEvent(entity.getUsedItemHand()));
            }
            if (user instanceof Player player) {
                int count = 20 + Math.min(state.getKillCount() / 50, 10);
                for (int i = 0; i < count; i++) {
                    if (!world.isClientSide()) {
                        StormSwordSpawner.executeIceSwordWave(player, world, 1, 50);
                    }
                }
            }
        });
        return super.doArts(type, user);
    }
}
