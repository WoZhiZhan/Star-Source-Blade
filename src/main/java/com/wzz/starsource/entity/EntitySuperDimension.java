package com.wzz.starsource.entity;

import java.util.ArrayList;
import java.util.List;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntitySuperDimension extends Entity {
    protected Entity thrower;
    protected ItemStack blade;
    protected List<Entity> alreadyHitEntity;
    protected float attackLevel;
    
    private static final EntityDataAccessor<Integer> LIFETIME = 
        SynchedEntityData.defineId(EntitySuperDimension.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> THROWER_ENTITY_ID = 
        SynchedEntityData.defineId(EntitySuperDimension.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COLOR = 
        SynchedEntityData.defineId(EntitySuperDimension.class, EntityDataSerializers.INT);

    public EntitySuperDimension(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blade = ItemStack.EMPTY;
        this.alreadyHitEntity = new ArrayList<>();
        this.attackLevel = 0.0F;
        this.tickCount = 0;
        this.getPersistentData().putInt("seed", this.random.nextInt(50));
    }

    public EntitySuperDimension(EntityType<?> entityType, Level level, LivingEntity entityLiving, float attackLevel, boolean multiHit) {
        this(entityType, level, entityLiving, attackLevel);
    }

    public EntitySuperDimension(EntityType<?> entityType, Level level, LivingEntity entityLiving, float attackLevel) {
        this(entityType, level);
        this.attackLevel = attackLevel;
        this.thrower = entityLiving;
        this.blade = entityLiving.getMainHandItem();
        
        if (!this.blade.isEmpty() && !(this.blade.getItem() instanceof ItemSlashBlade)) {
            this.blade = ItemStack.EMPTY;
        }

        this.alreadyHitEntity.clear();
        this.alreadyHitEntity.add(this.thrower);
        if (this.thrower.getVehicle() != null) {
            this.alreadyHitEntity.add(this.thrower.getVehicle());
        }
        this.alreadyHitEntity.addAll(this.thrower.getPassengers());
        this.tickCount = 0;
        this.setBoundingBox(this.getBoundingBox().inflate(4.0F));
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(LIFETIME, 20);
        this.getEntityData().define(THROWER_ENTITY_ID, 0);
        this.getEntityData().define(COLOR, 3355647);
    }

    public int getLifeTime() {
        return this.getEntityData().get(LIFETIME);
    }

    public void setLifeTime(int lifetime) {
        this.getEntityData().set(LIFETIME, lifetime);
    }

    public int getColor() {
        return this.getEntityData().get(COLOR);
    }

    public void setColor(int value) {
        this.getEntityData().set(COLOR, value);
    }

    public int getThrowerEntityId() {
        return this.getEntityData().get(THROWER_ENTITY_ID);
    }

    public void setThrowerEntityId(int entityid) {
        this.getEntityData().set(THROWER_ENTITY_ID, entityid);
    }

    @Override
    public void tick() {
        super.tick();
        this.xOld = this.getX();
        this.yOld = this.getY();
        this.zOld = this.getZ();
        if (!this.level().isClientSide) {
            if (this.tickCount < 8 && this.tickCount % 2 == 0) {
                this.playSound(SoundEvents.WITHER_HURT, 0.2F, 0.5F + 0.25F * this.random.nextFloat());
            }
            for (Entity e : level().getEntitiesOfClass(Entity.class, 
                    this.getBoundingBox().inflate(10))) {
                if (e != null && e != this && !(e instanceof Player)) {
                    e.setSecondsOnFire(10);
                    Entity owner = getThrower();
                    if (owner instanceof LivingEntity living) {
                        e.hurt(this.damageSources().mobAttack(living), 15);
                    } else e.hurt(this.damageSources().fellOutOfWorld(), 15);
                }
            }
        }

        if (this.tickCount >= this.getLifeTime()) {
            this.alreadyHitEntity.clear();
            this.alreadyHitEntity = null;
            this.discard();
        }
    }

    public RandomSource getRandom() {
        return this.random;
    }

    @Override
    public void move(MoverType moverType, Vec3 movement) {
    }

    @Override
    protected void handleNetherPortal() {
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public int getTeamColor() {
        float f1 = 0.5F;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }
        if (f1 > 1.0F) {
            f1 = 1.0F;
        }

        int i = super.getTeamColor();
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f1 * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    public Entity getThrower() {
        if (this.thrower == null) {
            int id = this.getThrowerEntityId();
            if (id != 0) {
                this.thrower = this.level().getEntity(id);
            }
        }
        return this.thrower;
    }

    public void setThrower(Entity entity) {
        if (entity != null) {
            this.setThrowerEntityId(entity.getId());
        }
        this.thrower = entity;
    }
}