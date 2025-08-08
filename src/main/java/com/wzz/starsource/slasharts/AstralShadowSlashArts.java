package com.wzz.starsource.slasharts;

import com.wzz.starsource.entity.EntityTrackingStormSwords;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.entity.EntityStormSwords;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Random;
import java.util.function.Function;

public class AstralShadowSlashArts extends SlashArts {
    public AstralShadowSlashArts(Function<LivingEntity, ResourceLocation> state) {
        super(state);
    }

    @Override
    public ResourceLocation doArts(ArtsType type, LivingEntity user) {
        Level world = user.level();
        ItemStack stack = user.getMainHandItem();
        final int b = new Random().nextInt(2);
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
                int count = 10 + Math.min(state.getKillCount() / 50, 10);

                if (!world.isClientSide()) {
                    for (int i = 0; i < count; i++) {
                        if (b == 0) {
                            float magicDamage = 10;
                            EntityStormSwords entityStormSwords = new EntityStormSwords(SlashBlade.RegistryEvents.StormSwords, world);
                            entityStormSwords.setOwner(player);
                            entityStormSwords.setDamage(magicDamage);
                            Vec3 playerLook = player.getLookAngle();
                            float horizontalSpread = 30.0F; // 水平扩散角度
                            float verticalSpread = 15.0F;   // 垂直扩散角度
                            float horizontalStep = count > 1 ? horizontalSpread / (count - 1) : 0;
                            float verticalStep = count > 1 ? verticalSpread / (count - 1) : 0;
                            float currentHorizontalAngle = -horizontalSpread / 2 + horizontalStep * i;
                            float currentVerticalAngle = -verticalSpread / 2 + verticalStep * i;
                            Vec3 direction = calculate3DDirection(playerLook, currentHorizontalAngle, currentVerticalAngle);
                            Vec3 spawnPos = player.position().add(direction.scale(1.5));
                            entityStormSwords.setPos(spawnPos.x, spawnPos.y + 1, spawnPos.z);
                            entityStormSwords.setDelay(i * 2);
                            int color = new Random().nextInt();
                            entityStormSwords.setColor(color);
                            entityStormSwords.shoot(direction.x, direction.y, direction.z, 3.0F, 1.0F);
                            world.addFreshEntity(entityStormSwords);
                        } else createAdvanced3DSwordArray(player, world, count);
                    }
                    EntityTrackingStormSwords entityStormSwords = new EntityTrackingStormSwords(SlashBlade.RegistryEvents.StormSwords, world);
                    entityStormSwords.setOwner(player);
                    entityStormSwords.setDamage(20f);
                    Vec3 spawnPos = player.position();
                    entityStormSwords.setPos(spawnPos.x, spawnPos.y + 1, spawnPos.z);
                    entityStormSwords.doFire();
                    world.addFreshEntity(entityStormSwords);
                }
            }
        });

        return super.doArts(type, user);
    }

    private static void createAdvanced3DSwordArray(Player player, Level world, int count) {
        Vec3 playerLook = player.getLookAngle();
        Vec3 playerPos = player.position().add(0, player.getEyeHeight(), 0);
        float baseFanAngle = 45.0F;
        float maxFanAngle = 150.0F; // 最大不超过150度
        float fanAngle = Math.min(maxFanAngle, baseFanAngle + count * 3.0F); // 每多一把剑增加3度
        float baseFanRadius = 2.0F;
        float fanRadius = baseFanRadius + Math.min(count * 0.15F, 3.0F); // 半径随数量增加，但有上限
        int layerCount = Math.max(1, count / 12); // 每12把剑一层
        int swordsPerLayer = Math.max(1, count / layerCount);
        for (int i = 0; i < count; i++) {
            int currentLayer = i / swordsPerLayer;
            int indexInLayer = i % swordsPerLayer;
            int actualSwordsInLayer = Math.min(swordsPerLayer, count - currentLayer * swordsPerLayer);
            float layerAngleOffset = currentLayer * 15.0F; // 每层偏移15度
            float currentFanAngle = fanAngle + currentLayer * 10.0F; // 外层角度更大
            float angle = -currentFanAngle/2 + (currentFanAngle / Math.max(1, actualSwordsInLayer-1)) * indexInLayer + layerAngleOffset;
            Vec3 right = playerLook.cross(new Vec3(0, 1, 0)).normalize();
            float currentRadius = fanRadius + currentLayer * 0.8F;
            Vec3 offsetDir = playerLook.scale(Math.cos(Math.toRadians(angle)))
                    .add(right.scale(Math.sin(Math.toRadians(angle))));
            float verticalOffset = (float) Math.sin(Math.toRadians(angle * 2)) * 0.5F + currentLayer * 0.3F;
            Vec3 spawnPos = playerPos.add(offsetDir.scale(currentRadius)).add(0, verticalOffset, 0);
            EntityStormSwords sword = new EntityStormSwords(SlashBlade.RegistryEvents.StormSwords, world);
            sword.setOwner(player);
            sword.setDamage(10f);
            sword.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            sword.setDelay(i * 2);
            int baseColor = 0x4B8BFF;
            int colorVariation = (currentLayer * 0x202020) + (indexInLayer * 0x050505);
            sword.setColor(baseColor + colorVariation);
            Vec3 shootDir = playerLook.add(right.scale(Math.sin(Math.toRadians(angle)) * 0.3));
            sword.shoot(shootDir.x, shootDir.y, shootDir.z, 3.5F, 0.3F);
            world.addFreshEntity(sword);
        }
    }

    private static Vec3 calculate3DDirection(Vec3 baseLook, float horizontalOffset, float verticalOffset) {
        // 将基础朝向转换为球坐标
        double baseYaw = Math.atan2(baseLook.z, baseLook.x);
        double basePitch = Math.asin(-baseLook.y);
        double newYaw = baseYaw + Math.toRadians(horizontalOffset);
        double newPitch = basePitch + Math.toRadians(verticalOffset);
        // 限制俯仰角范围
        newPitch = Math.max(-Math.PI/2, Math.min(Math.PI/2, newPitch));
        // 转换回笛卡尔坐标
        double x = Math.cos(newPitch) * Math.cos(newYaw);
        double y = -Math.sin(newPitch);
        double z = Math.cos(newPitch) * Math.sin(newYaw);
        return new Vec3(x, y, z).normalize();
    }
}
