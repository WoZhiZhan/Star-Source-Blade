package com.wzz.starsource.slasharts;

import com.wzz.starsource.entity.EntityMovingDrive;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.Function;

public class StarSourceSlashArts extends SlashArts {
    public StarSourceSlashArts(Function<LivingEntity, ResourceLocation> state) {
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
                int count = 5 + Math.min(state.getKillCount() / 50, 10);
                for (int i = 0; i < count; i++) {
                    if (!world.isClientSide()) {
                        EntityMovingDrive drive = new EntityMovingDrive(world, player, 1.5F);
                        drive.setPos(player.xOld, player.yOld + 1, player.zOld);
                        float angle = (float) (i * Math.PI * 2 / count);
                        Vec3 offset = new Vec3(
                                Math.cos(angle) * 0.2,
                                0,
                                Math.sin(angle) * 0.2
                        );
                        Vec3 direction = player.getLookAngle().add(offset).normalize();
                        drive.setDirection(direction);
                        drive.setDamage(10.0);
                        drive.setColor(0x3355FF);
                        drive.setMaxLifetime(80);
                        world.addFreshEntity(drive);
                    }
                }
            }
        });
        return super.doArts(type, user);
    }
}
