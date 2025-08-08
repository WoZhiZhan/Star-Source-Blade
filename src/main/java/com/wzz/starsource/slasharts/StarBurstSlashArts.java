package com.wzz.starsource.slasharts;

import com.wzz.starsource.util.EnhancedSakuraEffect;
import com.wzz.starsource.util.ModUtils;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.Function;

public class StarBurstSlashArts extends SlashArts {
    public StarBurstSlashArts(Function<LivingEntity, ResourceLocation> state) {
        super(state);
    }

    @Override
    public ResourceLocation doArts(ArtsType type, LivingEntity user) {
        ItemStack stack = user.getMainHandItem();
        LazyOptional<ISlashBladeState> stateOpt = stack.getCapability(ItemSlashBlade.BLADESTATE);
        stateOpt.ifPresent(state -> {
            final int cost = 10;
            if (state.getProudSoulCount() >= cost) {
                state.setProudSoulCount(state.getProudSoulCount() - cost);
            } else {
                stack.hurtAndBreak(10, user, (entity) -> {
                    entity.broadcastBreakEvent(entity.getUsedItemHand());
                });
            }
            if (user instanceof Player player) {
                for (int i = 0; i < 3; i++) {
                    EnhancedSakuraEffect.createComboTrail(player, i);
                }
                for (Entity entity : ModUtils.findEntitiesAlongRay(player, 3, 2.5d)) {
                    if (entity instanceof LivingEntity living && living != player) {
                        living.hurt(living.damageSources().playerAttack(player), 40f);
                    }
                }
            }
        });
        return super.doArts(type, user);
    }
}
