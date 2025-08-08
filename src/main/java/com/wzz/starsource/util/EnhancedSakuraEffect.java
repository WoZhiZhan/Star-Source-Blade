package com.wzz.starsource.util;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class EnhancedSakuraEffect {

    public static void createAdvancedTrail(LivingEntity entity) {
        if (entity.level().isClientSide) return;

        ServerLevel serverLevel = (ServerLevel) entity.level();

        Vec3 entityPos = entity.position().add(0, entity.getEyeHeight() * 0.8, 0);
        Vec3 lookVec = entity.getLookAngle();
        Vec3 rightVec = lookVec.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 upVec = lookVec.cross(rightVec).normalize();

        createSwordSlashEffect(serverLevel, entityPos, lookVec, rightVec, upVec);
    }

    private static void createSwordSlashEffect(ServerLevel level, Vec3 center, Vec3 forward, Vec3 right, Vec3 up) {
        int trailPoints = 15;
        double slashRadius = 2.0;
        double slashAngle = Math.PI * 0.8; // 144度的弧

        for (int i = 0; i < trailPoints; i++) {
            double progress = (double) i / (trailPoints - 1);
            double currentAngle = -slashAngle/2 + slashAngle * progress;
            double x = Math.cos(currentAngle) * slashRadius;
            double y = Math.sin(currentAngle) * slashRadius * 0.7;
            Vec3 slashOffset = right.scale(x).add(up.scale(y)).add(forward.scale(0.5));
            Vec3 particlePos = center.add(slashOffset);
            for (int j = 0; j < 3; j++) {
                double offsetX = (Math.random() - 0.5) * 0.15;
                double offsetY = (Math.random() - 0.5) * 0.15;
                double offsetZ = (Math.random() - 0.5) * 0.15;
                level.sendParticles(
                        ParticleTypes.ENCHANTED_HIT,
                        particlePos.x + offsetX,
                        particlePos.y + offsetY,
                        particlePos.z + offsetZ,
                        1, // 粒子数量
                        0, 0, 0, // 偏移
                        0 // 速度
                );
            }
            if (i % 2 == 0) {
                level.sendParticles(
                        ParticleTypes.CHERRY_LEAVES,
                        particlePos.x,
                        particlePos.y,
                        particlePos.z,
                        2,
                        0.1, 0.1, 0.1,
                        0.02
                );
            }
            if (i % 4 == 0) {
                level.sendParticles(
                        ParticleTypes.DRAGON_BREATH,
                        particlePos.x,
                        particlePos.y,
                        particlePos.z,
                        1,
                        0.05, 0.05, 0.05,
                        0.01
                );
            }
        }
        Vec3 centerPos = center.add(forward.scale(0.5));
        for (int i = 0; i < 8; i++) {
            double angle = (i / 8.0) * Math.PI * 2;
            double distance = 1.0;
            Vec3 spreadPos = centerPos.add(
                    right.scale(Math.cos(angle) * distance).add(up.scale(Math.sin(angle) * distance))
            );

            level.sendParticles(
                    ParticleTypes.CRIT,
                    spreadPos.x,
                    spreadPos.y,
                    spreadPos.z,
                    1,
                    0, 0, 0,
                    0.1
            );
        }
    }

    public static void createComboTrail(LivingEntity entity, int comboStage) {
        if (entity.level().isClientSide) return;

        ServerLevel serverLevel = (ServerLevel) entity.level();
        Vec3 entityPos = entity.position().add(-1.8, entity.getEyeHeight() * 0.8, 0);
        Vec3 lookVec = entity.getLookAngle();
        Vec3 rightVec = lookVec.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 upVec = lookVec.cross(rightVec).normalize();

        switch (comboStage) {
            case 1: // 横斩
                createHorizontalSlash(serverLevel, entityPos, lookVec, rightVec, upVec);
                break;
            case 2: // 竖斩
                createVerticalSlash(serverLevel, entityPos, lookVec, rightVec, upVec);
                break;
            case 3: // 突刺
                createThrustAttack(serverLevel, entityPos, lookVec, rightVec, upVec);
                break;
            default:
                createSwordSlashEffect(serverLevel, entityPos, lookVec, rightVec, upVec);
        }
    }

    private static void createHorizontalSlash(ServerLevel level, Vec3 center, Vec3 forward, Vec3 right, Vec3 up) {
        for (int i = 0; i < 12; i++) {
            double progress = (double) i / 11;
            double angle = -Math.PI/3 + (Math.PI*2/3) * progress;

            Vec3 pos = center.add(right.scale(Math.cos(angle) * 2.5))
                    .add(up.scale(Math.sin(angle) * 0.3))
                    .add(forward.scale(0.8));

            level.sendParticles(ParticleTypes.SWEEP_ATTACK, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
            if (i % 2 == 0) {
                level.sendParticles(ParticleTypes.ENCHANTED_HIT, pos.x, pos.y, pos.z, 2, 0.1, 0.1, 0.1, 0);
            }
        }
    }

    private static void createVerticalSlash(ServerLevel level, Vec3 center, Vec3 forward, Vec3 right, Vec3 up) {
        for (int i = 0; i < 10; i++) {
            double progress = (double) i / 9;
            double height = -1.5 + 3.0 * progress;

            Vec3 pos = center.add(up.scale(height))
                    .add(forward.scale(1.2))
                    .add(right.scale((Math.random() - 0.5) * 0.3));

            level.sendParticles(ParticleTypes.ENCHANTED_HIT, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
            if (i % 3 == 0) {
                level.sendParticles(ParticleTypes.CRIT, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0.05);
            }
        }
    }

    private static void createThrustAttack(ServerLevel level, Vec3 center, Vec3 forward, Vec3 right, Vec3 up) {
        for (int i = 0; i < 8; i++) {
            double distance = i * 0.4;
            Vec3 pos = center.add(forward.scale(distance));
            level.sendParticles(ParticleTypes.ENCHANTED_HIT, pos.x, pos.y, pos.z, 2, 0.1, 0.1, 0.1, 0);
            double spiralAngle = distance * 3;
            Vec3 spiralPos = pos.add(right.scale(Math.cos(spiralAngle) * 0.3))
                    .add(up.scale(Math.sin(spiralAngle) * 0.3));
            level.sendParticles(ParticleTypes.DRAGON_BREATH, spiralPos.x, spiralPos.y, spiralPos.z, 1, 0, 0, 0, 0);
        }
    }
}