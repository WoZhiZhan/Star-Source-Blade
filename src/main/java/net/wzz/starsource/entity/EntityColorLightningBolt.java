package net.wzz.starsource.entity;

import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityColorLightningBolt
        extends EntityWeatherEffect
{
    public long boltVertex;
    private int boltLivingTime;
    private byte lightningState;

    public EntityColorLightningBolt(World world, double x, double y, double z)
    {
        super(world);
        setPosition(x, y, z);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = (this.rand.nextInt(3) + 1);
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance)
    {
        return true;
    }

    public void onUpdate()
    {
        super.onUpdate();
        if (this.lightningState == 2)
        {
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
        }
        this.lightningState = ((byte)(this.lightningState - 1));
        if (this.lightningState < 0) {
            if (this.boltLivingTime == 0)
            {
                setDead();
            }
            else if (this.lightningState < -this.rand.nextInt(10))
            {
                this.boltLivingTime -= 1;
                this.lightningState = 1;
                this.boltVertex = this.rand.nextLong();
            }
        }
        if ((this.lightningState >= 0) && (this.world.isRemote)) {
            this.world.setLastLightningBolt(2);
        }
    }

    protected void entityInit() {}

    protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {}

    protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {}
}

