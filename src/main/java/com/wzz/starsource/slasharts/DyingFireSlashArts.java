package com.wzz.starsource.slasharts;

import com.wzz.starsource.entity.EntityDyingDimension;
import com.wzz.starsource.entity.EntitySuperDimension;
import com.wzz.starsource.init.ModEntities;
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

public class DyingFireSlashArts extends SlashArts {
    public DyingFireSlashArts(Function<LivingEntity, ResourceLocation> state) {
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
                stack.hurtAndBreak(10, user, (entity) -> {
                    entity.broadcastBreakEvent(entity.getUsedItemHand());
                });
            }
            if (user instanceof Player player) {
                EntityDyingDimension entityDyingDimension = new EntityDyingDimension(ModEntities.DYING_DIMENSION.get(), world, player, cost);
                entityDyingDimension.setPos(player.getX(), player.getY(), player.getZ());
                entityDyingDimension.setLifeTime(200);
                world.addFreshEntity(entityDyingDimension);
            }
        });
        return super.doArts(type, user);
    }
}
