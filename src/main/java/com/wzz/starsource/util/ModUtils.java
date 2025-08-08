package com.wzz.starsource.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class ModUtils {
    public static void playSound(SoundEvent s, LivingEntity living) {
        Level level = living.level();
        if (!level.isClientSide()) {
            level.playSound(null, living, s, SoundSource.NEUTRAL, 1, 1);
        } else {
            level.playLocalSound(living.getX(), living.getY(), living.getZ(), s, SoundSource.NEUTRAL, 1, 1, false);
        }
    }

    public static void spawnParticles(Entity living, SimpleParticleType particleType, int number, double speed) {
        if (living.level() instanceof ServerLevel serverLevel) {
            for (int k = 0; k < number; ++k) {
                Random rand = new Random();
                serverLevel.sendParticles((ParticleOptions)particleType,
                        living.getX() + (double) (rand.nextFloat() * living.getBbWidth() * 2.0F) - (double) living.getBbWidth(),
                        living.getY() + (double) (rand.nextFloat() * living.getBbHeight()),
                        living.getZ() + (double) (rand.nextFloat() * living.getBbWidth() * 2.0F) - (double) living.getBbWidth(),
                        1, 0, 0, 0, speed);
            }
        }
    }

    public static List<LivingEntity> findEntitiesAlongRay(Entity entity, double maxDistance, double searchRadius) {
        Set<LivingEntity> entities = new HashSet<>();
        Vec3 start = entity.getEyePosition(1.0f);
        for (double d = 0; d <= maxDistance; d += searchRadius) {
            Vec3 point = start.add(entity.getViewVector(1.0f).scale(d));
            AABB area = new AABB(point, point).inflate(searchRadius);
            entities.addAll(entity.level().getEntitiesOfClass(LivingEntity.class, area, e -> e != entity));
        }
        return new ArrayList<>(entities);
    }

    public static Entity[] findAllEntities(Entity player, double range) {
        final Vec3 _center = new Vec3(player.getX(), player.getY(), player.getZ());
        AABB boundingBox = new AABB(_center.x - range, _center.y - range, _center.z - range,
                _center.x + range, _center.y + range, _center.z + range);
        List<LivingEntity> list = player.level().getEntitiesOfClass(LivingEntity.class, boundingBox, e -> true).stream()
                .filter(entity -> entity != player)
                .sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(_center)))
                .toList();
        return list.toArray(new Entity[0]);
    }

    public static void moveTo(LivingEntity living, double range) {
        Vec3 lookVec = living.getLookAngle();
        Vec3 newVelocity = lookVec.scale(range);
        living.setDeltaMovement(newVelocity);
        living.hurtMarked = true;
    }
}
