package com.wzz.starsource.entity;

import com.wzz.starsource.init.ModEntities;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;

public class EntityMovingDrive extends EntityDrive {
    
    private Vec3 direction;
    private float moveSpeed = 1.0F;
    private int maxLifetime = 60;
    
    public EntityMovingDrive(EntityType<? extends EntityDrive> entityType, Level world) {
        super(entityType, world);
        this.setNoGravity(true);
    }

    public EntityMovingDrive(Level world, Player player, float speed) {
        this(ModEntities.MOVING_DRIVE.get(), world);
        this.setOwner(player);
        this.moveSpeed = speed;
        this.setLifetime(maxLifetime);
        this.direction = player.getLookAngle().normalize();
        this.setDeltaMovement(this.direction.scale(this.moveSpeed));
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (!this.level().isClientSide()) {
            if (this.direction != null) {
                Vec3 motion = this.direction.scale(this.moveSpeed);
                this.setDeltaMovement(motion);
                this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
            }
            if (this.tickCount > maxLifetime) {
                this.discard();
            }
        }
    }

    public void setDirection(Vec3 direction) {
        this.direction = direction.normalize();
        this.setDeltaMovement(this.direction.scale(this.moveSpeed));
    }

    public void setMoveSpeed(float speed) {
        this.moveSpeed = speed;
        if (this.direction != null) {
            this.setDeltaMovement(this.direction.scale(this.moveSpeed));
        }
    }

    public void setMaxLifetime(int ticks) {
        this.maxLifetime = ticks;
        this.setLifetime(ticks);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }
}