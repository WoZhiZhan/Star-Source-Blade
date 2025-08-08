package com.wzz.starsource.entity;

import com.wzz.starsource.util.ModUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityDyingDimension extends EntitySuperDimension{
    public EntityDyingDimension(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public EntityDyingDimension(EntityType<?> entityType, Level level, LivingEntity entityLiving, float attackLevel) {
        super(entityType, level, entityLiving, attackLevel);
    }

    public EntityDyingDimension(EntityType<?> entityType, Level level, LivingEntity entityLiving, float attackLevel, boolean multiHit) {
        super(entityType, level, entityLiving, attackLevel, multiHit);
    }

    @Override
    public void tick() {
        super.tick();
        for (Entity e : ModUtils.findAllEntities(this, this.tickCount * 0.1f)) {
            if (!(e instanceof Player) && e instanceof LivingEntity living) {
                e.setSecondsOnFire(10);
                if (getThrower() instanceof LivingEntity livingEntity) {
                    living.hurt(living.damageSources().mobAttack(livingEntity), 15f);
                }
            }
        }
        for (Entity e : ModUtils.findAllEntities(this, this.tickCount * 0.3f)) {
            if (!(e instanceof Player) && e instanceof LivingEntity) {
                Vec3 pullDirection = this.position()
                        .subtract(e.position())
                        .normalize()
                        .scale(0.2);
                e.setDeltaMovement(e.getDeltaMovement().add(pullDirection));
            }
        }
    }
}
