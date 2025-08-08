package net.wzz.starsource.load.specialAttacks;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RastarGroupSpecialAttacks extends SpecialAttackBase {
    public static final RastarGroupSpecialAttacks theSa = new RastarGroupSpecialAttacks();

    private RastarGroupSpecialAttacks() {
    }

    @Override
    public String toString() {
        return "RastarGroupSpecialAttacks";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        final int cost = -10;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
            ItemSlashBlade.damageItem(stack, 10, player);
        }
        if (!world.isRemote) {
            int count = 10 + StylishRankManager.getStylishRank(player);
            for (int i = 0; i < 12; i++) {
                boolean isBurst = (i % 2 == 0);
                float magicDamage = count;
                EntityDrive entityDrive = new EntityDrive(world, player, magicDamage, false, 0.0F);
                entityDrive.setLocationAndAngles(player.posX, player.posY + (double)player.getEyeHeight() / 2.0, player.posZ, player.rotationYaw + (float)(30 * i), 0.0F);
                entityDrive.setDriveVector(0.5F);
                entityDrive.setLifeTime(80);
                entityDrive.setIsMultiHit(false);
                entityDrive.setRoll(90.0F);
                world.spawnEntity(entityDrive);
                EntityDrive entityDrive1 = new EntityDrive(world, player, magicDamage, false, 0.0F);
                entityDrive1.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw + (float)(30 * i), 0.0F);
                entityDrive1.setDriveVector(0.5F);
                entityDrive1.setLifeTime(80);
                entityDrive1.setIsMultiHit(false);
                entityDrive1.setRoll(90.0F);
                world.spawnEntity(entityDrive1);
                EntityDrive entityDrive11 = new EntityDrive(world, player, magicDamage, false, 0.0F);
                entityDrive11.setLocationAndAngles(player.posX, player.posY + 2, player.posZ, player.rotationYaw + (float)(30 * i), 0.0F);
                entityDrive11.setDriveVector(0.5F);
                entityDrive11.setLifeTime(80);
                entityDrive11.setIsMultiHit(false);
                entityDrive11.setRoll(90.0F);
                world.spawnEntity(entityDrive11);
            }
        }
    }
}
