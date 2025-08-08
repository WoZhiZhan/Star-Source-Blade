package com.wzz.starsource.entity;

import mods.flammpfeil.slashblade.SlashBlade.RegistryEvents;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.entity.EntityAbstractSummonedSword;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.PlayMessages;

import java.util.List;
import java.util.Optional;

public class EntityTrackingStormSwords extends EntityAbstractSummonedSword {
    private static final EntityDataAccessor<Boolean> IT_FIRED;
    private static final EntityDataAccessor<Integer> TARGET_ID; // 追踪目标的ID

    private static final double TRACKING_RANGE = 16.0D; // 追踪范围
    private static final double TRACKING_SPEED = 0.8D; // 追踪时的移动速度调整
    private static final int TRACKING_UPDATE_INTERVAL = 10; // 目标更新间隔（tick）
    private static final double MAX_TRACKING_DISTANCE = 32.0D; // 最大追踪距离
    
    private int trackingUpdateCounter = 0;
    private LivingEntity cachedTarget = null;

    public EntityTrackingStormSwords(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.setPierce((byte)1);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IT_FIRED, false);
        this.entityData.define(TARGET_ID, -1);
    }

    public void doFire() {
        this.getEntityData().set(IT_FIRED, true);
    }

    public boolean itFired() {
        return (Boolean)this.getEntityData().get(IT_FIRED);
    }

    public void setTarget(LivingEntity target) {
        if (target != null) {
            this.getEntityData().set(TARGET_ID, target.getId());
            this.cachedTarget = target;
        } else {
            this.getEntityData().set(TARGET_ID, -1);
            this.cachedTarget = null;
        }
    }

    public LivingEntity getTarget() {
        int targetId = this.getEntityData().get(TARGET_ID);
        if (targetId == -1) {
            return null;
        }
        if (cachedTarget != null && cachedTarget.getId() == targetId && cachedTarget.isAlive()) {
            return cachedTarget;
        }
        Entity entity = this.level().getEntity(targetId);
        if (entity instanceof LivingEntity && entity.isAlive()) {
            cachedTarget = (LivingEntity) entity;
            return cachedTarget;
        }
        setTarget(null);
        return null;
    }

    public static EntityTrackingStormSwords createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new EntityTrackingStormSwords(RegistryEvents.StormSwords, worldIn);
    }

    @Override
    public void tick() {
        super.tick();
        this.handleFiredState();
    }
    
    private void handleFiredState() {
        LivingEntity target = this.getTarget();
        if (++trackingUpdateCounter >= TRACKING_UPDATE_INTERVAL) {
            trackingUpdateCounter = 0;
            target = findBestTarget();
            setTarget(target);
        }
        if (target != null && target.isAlive()) {
            double distanceToTarget = this.distanceTo(target);
            if (distanceToTarget > MAX_TRACKING_DISTANCE) {
                setTarget(null);
                this.performDefaultMovement();
                return;
            }
            Vec3 targetPos = target.position().add(0, target.getEyeHeight() * 0.5, 0);
            Vec3 currentPos = this.position();
            Vec3 trackingDir = targetPos.subtract(currentPos).normalize();
            this.shoot(trackingDir.x, trackingDir.y, trackingDir.z, 
                      (float)(3.0F * TRACKING_SPEED), 0.5F);
            this.lookAt(EntityAnchorArgument.Anchor.FEET, target.position().add(0, target.getEyeHeight() * 0.5, 0));
        } else {
            this.performDefaultMovement();
        }
        if (!this.level().isClientSide()) {
            this.hitCheck();
        }
    }

    private void performDefaultMovement() {
        Entity target = this.getVehicle();
        if (target != null) {
            this.stopRiding();
        }
        this.tickCount = 0;
        Vec3 dir = this.getViewVector(1.0F);
        if (target != null) {
            dir = target.position().subtract(this.position())
                    .multiply(1.0F, 0.0F, 1.0F).normalize();
        }
        this.shoot(dir.x, dir.y, dir.z, 3.0F, 1.0F);
    }

    private LivingEntity findBestTarget() {
        Entity shooter = this.getShooter();
        if (!(shooter instanceof Player player)) {
            return null;
        }
        Vec3 searchPos = this.position();
        AABB searchArea = new AABB(searchPos.subtract(TRACKING_RANGE, TRACKING_RANGE, TRACKING_RANGE),
                                  searchPos.add(TRACKING_RANGE, TRACKING_RANGE, TRACKING_RANGE));
        List<LivingEntity> potentialTargets = this.level().getEntitiesOfClass(LivingEntity.class, searchArea, 
            entity -> isValidTarget(entity, player));
        
        if (potentialTargets.isEmpty()) {
            return null;
        }
        Optional<LivingEntity> closestTarget = potentialTargets.stream()
            .min((e1, e2) -> Double.compare(this.distanceToSqr(e1), this.distanceToSqr(e2)));
            
        return closestTarget.orElse(null);
    }

    private boolean isValidTarget(LivingEntity entity, Player shooter) {
        if (entity == null || !entity.isAlive() || entity == shooter) {
            return false;
        }
        if (entity instanceof Monster) {
            return true;
        }
        if (entity instanceof Mob mob) {
            return mob.getTarget() == shooter || mob.isAggressive();
        }
        return !(entity instanceof Player);
    }

    private void hitCheck() {
        Vec3 positionVec = this.position();
        Vec3 dirVec = this.getViewVector(1.0F);
        EntityHitResult raytraceresult = null;
        EntityHitResult entityraytraceresult = this.getRayTrace(positionVec, dirVec);
        if (entityraytraceresult != null) {
            raytraceresult = entityraytraceresult;
        }

        if (raytraceresult != null && raytraceresult.getType() == Type.ENTITY) {
            Entity entity = raytraceresult.getEntity();
            Entity entity1 = this.getShooter();
            if (entity instanceof Player && entity1 instanceof Player && !((Player)entity1).canHarmPlayer((Player)entity)) {
                raytraceresult = null;
            }
        }

        if (raytraceresult != null && raytraceresult.getType() == Type.ENTITY && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
            this.resetAlreadyHits();
            this.hasImpulse = true;
        }
    }

    private void faceEntityStandby() {
        long cycle = (long)(5 + this.tickCount);
        long tickOffset = 0L;
        if (this.level().isClientSide()) {
            tickOffset = 1L;
        }

        int ticks = (int)(((long)this.tickCount + tickOffset) % cycle);
        double rotParTick = (double)360.0F / (double)cycle;
        double offset = (double)this.getDelay();
        double degYaw = ((double)ticks * rotParTick + offset) % (double)360.0F;
        double yaw = Math.toRadians(degYaw);
        Vec3 dir = new Vec3((double)0.0F, (double)0.0F, (double)1.0F);
        dir = dir.yRot((float)(-yaw));
        dir = dir.normalize().scale((double)4.0F);
        if (this.getVehicle() != null) {
            dir = dir.add(this.getVehicle().position());
            dir = dir.add((double)0.0F, (double)this.getVehicle().getEyeHeight() / (double)2.0F, (double)0.0F);
        }

        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
        this.setPos(dir);
        this.setRot((float)(-degYaw) - 180.0F, 0.0F);
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity targetEntity = entityHitResult.getEntity();
        if (targetEntity instanceof LivingEntity) {
            KnockBacks.toss.action.accept((LivingEntity)targetEntity);
            StunManager.setStun((LivingEntity)targetEntity);
            if (targetEntity == this.getTarget()) {
                setTarget(null);
            }
        }

        super.onHitEntity(entityHitResult);
    }

    protected void onHitBlock(BlockHitResult blockraytraceresult) {
        this.burst();
    }

    static {
        IT_FIRED = SynchedEntityData.defineId(EntityTrackingStormSwords.class, EntityDataSerializers.BOOLEAN);
        TARGET_ID = SynchedEntityData.defineId(EntityTrackingStormSwords.class, EntityDataSerializers.INT);
    }
}