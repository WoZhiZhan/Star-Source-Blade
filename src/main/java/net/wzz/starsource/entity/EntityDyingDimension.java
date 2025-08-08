//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.wzz.starsource.entity;

import mods.flammpfeil.slashblade.ability.ArmorPiercing;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.ability.StylishRankManager.AttackTypes;
import mods.flammpfeil.slashblade.ability.TeleportCanceller;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorDestructable;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.ReflectionAccessHelper;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IThrowableEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wzz.starsource.item.ItemDyingFireSlashBlade;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EntityDyingDimension extends Entity implements IThrowableEntity {
    protected Entity thrower;
    protected ItemStack blade;
    protected List<Entity> alreadyHitEntity;
    protected float AttackLevel;
    private static final DataParameter<Integer> LIFETIME;
    private static final DataParameter<Boolean> SINGLE_HIT;
    private static final DataParameter<Boolean> IS_SLASH_DIMENSION;
    private static final DataParameter<Integer> THROWER_ENTITY_ID;
    private static final DataParameter<Integer> INTERVAL;
    private static final DataParameter<Integer> COLOR;

    public EntityDyingDimension(World par1World) {
        super(par1World);
        this.blade = ItemStack.EMPTY;
        this.alreadyHitEntity = new ArrayList();
        this.AttackLevel = 0.0F;
        this.ticksExisted = 0;
        this.getEntityData().setInteger("seed", this.rand.nextInt(50));
    }

    public EntityDyingDimension(World par1World, EntityLivingBase entityLiving, float AttackLevel, boolean multiHit) {
        this(par1World, entityLiving, AttackLevel);
        this.setIsSingleHit(multiHit);
    }

    public EntityDyingDimension(World par1World, EntityLivingBase entityLiving, float AttackLevel) {
        this(par1World);
        this.AttackLevel = AttackLevel;
        this.thrower = entityLiving;
        this.blade = entityLiving.getHeldItemMainhand();
        if (!this.blade.isEmpty() && !(this.blade.getItem() instanceof ItemSlashBlade)) {
            this.blade = ItemStack.EMPTY;
        }

        this.alreadyHitEntity.clear();
        this.alreadyHitEntity.add(this.thrower);
        this.alreadyHitEntity.add(this.thrower.getRidingEntity());
        this.alreadyHitEntity.addAll(this.thrower.getPassengers());
        this.ticksExisted = 0;
        this.setSize(4.0F, 4.0F);
    }

    protected void entityInit() {
        this.getDataManager().register(LIFETIME, 20);
        this.getDataManager().register(SINGLE_HIT, false);
        this.getDataManager().register(IS_SLASH_DIMENSION, false);
        this.getDataManager().register(THROWER_ENTITY_ID, 0);
        this.getDataManager().register(INTERVAL, 7);
        this.getDataManager().register(COLOR, 3355647);
    }

    public boolean getIsSingleHit() {
        return (Boolean)this.getDataManager().get(SINGLE_HIT);
    }

    public void setIsSingleHit(boolean isSingleHit) {
        this.getDataManager().set(SINGLE_HIT, isSingleHit);
    }

    public int getLifeTime() {
        return (Integer)this.getDataManager().get(LIFETIME);
    }

    public void setLifeTime(int lifetime) {
        this.getDataManager().set(LIFETIME, lifetime);
    }

    public boolean getIsSlashDimension() {
        return (Boolean)this.getDataManager().get(IS_SLASH_DIMENSION);
    }

    public void setIsSlashDimension(boolean isSlashDimension) {
        this.getDataManager().set(IS_SLASH_DIMENSION, isSlashDimension);
    }

    public int getInterval() {
        return (Integer)this.getDataManager().get(INTERVAL);
    }

    public void setInterval(int value) {
        this.getDataManager().set(INTERVAL, value);
    }

    public int getColor() {
        return (Integer)this.getDataManager().get(COLOR);
    }

    public void setColor(int value) {
        this.getDataManager().set(COLOR, value);
    }

    public int getThrowerEntityId() {
        return (Integer)this.getDataManager().get(THROWER_ENTITY_ID);
    }

    public void setThrowerEntityId(int entityid) {
        this.getDataManager().set(THROWER_ENTITY_ID, entityid);
    }

    public void onUpdate() {
        super.onUpdate();
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        if (!this.world.isRemote) {
            if (this.ticksExisted < 8 && this.ticksExisted % 2 == 0) {
                this.playSound(SoundEvents.ENTITY_WITHER_HURT, 0.2F, 0.5F + 0.25F * this.rand.nextFloat());
            }
            for (Entity e : world.getEntitiesWithinAABBExcludingEntity(null,
                    getEntityBoundingBox().grow(ItemDyingFireSlashBlade.tick*1000))) {
                if (e != null && e != this && !(e instanceof EntityPlayer)) {
                    double attractStrength = 0.1;
                    double dx = posX - e.posX;
                    double dy = posY - e.posY;
                    double dz = posZ - e.posZ;
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    double forceX = attractStrength * dx / distance;
                    double forceY = attractStrength * dy / distance;
                    double forceZ = attractStrength * dz / distance;
                    e.motionX += forceX;
                    e.motionY += forceY;
                    e.motionZ += forceZ;
                    e.setFire(10);
                    e.attackEntityFrom(DamageSource.OUT_OF_WORLD,10);
                    if (e instanceof EntityLivingBase)
                        ((EntityLivingBase) e).hurtTime = 0;
                }
            }
            if (this.getThrower() != null) {
                AxisAlignedBB bb = this.getEntityBoundingBox();
                Iterator var41;
                Entity curEntity;
                if (this.getThrower() instanceof EntityLivingBase) {
                    EntityLivingBase entityLiving = (EntityLivingBase)this.getThrower();
                    List<Entity> list = this.world.getEntitiesInAABBexcluding(this.getThrower(), bb, EntitySelectorDestructable.getInstance());
                    StylishRankManager.setNextAttackType(this.thrower, AttackTypes.DestructObject);
                    list.removeAll(this.alreadyHitEntity);
                    this.alreadyHitEntity.addAll(list);
                    var41 = list.iterator();

                    label131:
                    while(true) {
                        boolean isDestruction;
                        do {
                            if (!var41.hasNext()) {
                                break label131;
                            }

                            curEntity = (Entity)var41.next();
                            if (this.blade.isEmpty()) {
                                break label131;
                            }

                            isDestruction = true;
                            if (curEntity instanceof EntityFireball) {
                                if (((EntityFireball)curEntity).shootingEntity != null && ((EntityFireball)curEntity).shootingEntity.getEntityId() == entityLiving.getEntityId()) {
                                    isDestruction = false;
                                } else {
                                    isDestruction = !curEntity.attackEntityFrom(DamageSource.causeMobDamage(entityLiving), this.AttackLevel);
                                }
                            } else if (curEntity instanceof EntityArrow) {
                                if (((EntityArrow)curEntity).shootingEntity != null && ((EntityArrow)curEntity).shootingEntity.getEntityId() == entityLiving.getEntityId()) {
                                    isDestruction = false;
                                }
                            } else if (curEntity instanceof IThrowableEntity) {
                                if (((IThrowableEntity)curEntity).getThrower() != null && ((IThrowableEntity)curEntity).getThrower().getEntityId() == entityLiving.getEntityId()) {
                                    isDestruction = false;
                                }
                            } else if (curEntity instanceof EntityThrowable && ((EntityThrowable)curEntity).getThrower() != null && ((EntityThrowable)curEntity).getThrower().getEntityId() == entityLiving.getEntityId()) {
                                isDestruction = false;
                            }
                        } while(!isDestruction);

                        ReflectionAccessHelper.setVelocity(curEntity, 0.0, 0.0, 0.0);
                        curEntity.setDead();

                        for(int var1 = 0; var1 < 10; ++var1) {
                            Random rand = this.getRand();
                            double var2 = rand.nextGaussian() * 0.02;
                            double var4 = rand.nextGaussian() * 0.02;
                            double var6 = rand.nextGaussian() * 0.02;
                            double var8 = 10.0;
                            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, curEntity.posX + (double)(rand.nextFloat() * curEntity.width * 2.0F) - (double)curEntity.width - var2 * var8, curEntity.posY + (double)(rand.nextFloat() * curEntity.height) - var4 * var8, curEntity.posZ + (double)(rand.nextFloat() * curEntity.width * 2.0F) - (double)curEntity.width - var6 * var8, var2, var4, var6, new int[0]);
                        }

                        StylishRankManager.doAttack(this.thrower);
                    }
                }

                if (this.getIsSingleHit() || this.ticksExisted % 2 == 0) {
                    List<Entity> list = this.world.getEntitiesInAABBexcluding(this.getThrower(), bb, EntitySelectorAttackable.getInstance());
                    list.removeAll(this.alreadyHitEntity);
                    if (this.getIsSingleHit()) {
                        this.alreadyHitEntity.addAll(list);
                    }

                    float magicDamage = Math.max(1.0F, this.AttackLevel);
                    StylishRankManager.setNextAttackType(this.thrower, AttackTypes.SlashDimMagic);
                    var41 = list.iterator();

                    while(var41.hasNext()) {
                        curEntity = (Entity)var41.next();
                        if (this.blade.isEmpty()) {
                            break;
                        }

                        if (this.getIsSlashDimension()) {
                            ArmorPiercing.doAPAttack(curEntity, magicDamage);
                        }

                        Vec3d pos = curEntity.getPositionVector();
                        TeleportCanceller.setCancel(curEntity);
                        curEntity.hurtResistantTime = 0;
                        DamageSource ds = (new EntityDamageSource("directMagic", this.getThrower())).setDamageBypassesArmor().setMagicDamage().setProjectile();
                        if (!this.blade.isEmpty() && curEntity instanceof EntityLivingBase) {
                            ((ItemSlashBlade)this.blade.getItem()).hitEntity(this.blade, (EntityLivingBase)curEntity, (EntityLivingBase)this.thrower);
                        }

                        if (!curEntity.getPositionVector().equals(pos)) {
                            curEntity.setPositionAndUpdate(pos.x, pos.y, pos.z);
                        }

                        curEntity.motionX = 0.0;
                        curEntity.motionY = 0.0;
                        curEntity.motionZ = 0.0;
                        if (3 < this.ticksExisted && !this.blade.isEmpty() && curEntity instanceof EntityLivingBase) {
                            if (this.getIsSlashDimension()) {
                                curEntity.addVelocity(0.0, 0.5, 0.0);
                            } else {
                                int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, this.blade);
                                if (0 < level) {
                                    curEntity.addVelocity(Math.sin((double)(this.getThrower().rotationYaw * 3.1415927F / 180.0F)) * (double)((float)level) * 0.5, 0.2, -Math.cos((double)(this.getThrower().rotationYaw * 3.1415927F / 180.0F)) * (double)((float)level) * 0.5);
                                } else {
                                    curEntity.addVelocity(-Math.sin((double)(this.getThrower().rotationYaw * 3.1415927F / 180.0F)) * 0.5, 0.2, Math.cos((double)(this.getThrower().rotationYaw * 3.1415927F / 180.0F)) * 0.5);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (this.ticksExisted >= this.getLifeTime()) {
            this.alreadyHitEntity.clear();
            this.alreadyHitEntity = null;
            this.setDead();
        }

    }

    public Random getRand() {
        return this.rand;
    }

    public boolean isOffsetPositionInLiquid(double par1, double par3, double par5) {
        return false;
    }

    public void move(MoverType moverType, double par1, double par3, double par5) {
    }

    protected void dealFireDamage(int par1) {
    }

    public boolean handleWaterMovement() {
        return false;
    }

    public boolean isInsideOfMaterial(Material par1Material) {
        return false;
    }

    public boolean isInLava() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        float f1 = 0.5F;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f1 > 1.0F) {
            f1 = 1.0F;
        }

        int i = super.getBrightnessForRender();
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f1 * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    public float getBrightness() {
        float f1 = super.getBrightness();
        float f2 = 0.9F;
        f2 = f2 * f2 * f2 * f2;
        return f1 * (1.0F - f2) + f2;
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
    }

    public void setPortal(BlockPos pos) {
    }

    public boolean isBurning() {
        return false;
    }

    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    public void setInWeb() {
    }

    public Entity getThrower() {
        if (this.thrower == null) {
            int id = this.getThrowerEntityId();
            if (id != 0) {
                this.thrower = this.getEntityWorld().getEntityByID(id);
            }
        }

        return this.thrower;
    }

    public void setThrower(Entity entity) {
        if (entity != null) {
            this.setThrowerEntityId(entity.getEntityId());
        }

        this.thrower = entity;
    }

    static {
        LIFETIME = EntityDataManager.createKey(EntityDyingDimension.class, DataSerializers.VARINT);
        SINGLE_HIT = EntityDataManager.createKey(EntityDyingDimension.class, DataSerializers.BOOLEAN);
        IS_SLASH_DIMENSION = EntityDataManager.createKey(EntityDyingDimension.class, DataSerializers.BOOLEAN);
        THROWER_ENTITY_ID = EntityDataManager.createKey(EntityDyingDimension.class, DataSerializers.VARINT);
        INTERVAL = EntityDataManager.createKey(EntityDyingDimension.class, DataSerializers.VARINT);
        COLOR = EntityDataManager.createKey(EntityDyingDimension.class, DataSerializers.VARINT);
    }
}
