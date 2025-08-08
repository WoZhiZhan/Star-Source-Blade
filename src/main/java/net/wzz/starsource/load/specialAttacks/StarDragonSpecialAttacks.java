package net.wzz.starsource.load.specialAttacks;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.entity.EntitySakuraEndManager;
import mods.flammpfeil.slashblade.entity.EntitySummonedSword;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.wzz.starsource.entity.EntityDragonDimension;

import java.util.Random;

import static mods.flammpfeil.slashblade.item.ItemSlashBlade.ComboSequence.None;

public class StarDragonSpecialAttacks extends SpecialAttackBase {
    private final ItemSlashBlade.ComboSequence setCombo;
    public static final StarDragonSpecialAttacks theSa = new StarDragonSpecialAttacks(0.5f,100,true,None);

    private StarDragonSpecialAttacks(float speed, int lifetime, boolean multihit, ItemSlashBlade.ComboSequence setCombo) {
        this.setCombo = setCombo;
    }

    @Override
    public String toString() {
        return "StarDragonSpecialAttacks";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        ItemSlashBlade.damageItem(stack, 10, player);
        if (!world.isRemote) {
            Random random = new Random();
            for (int i = 0; i < 150; i++) {
                double angle = random.nextDouble() * 10.0D * 3.141592653589793D;
                double distance = random.nextGaussian() * 8.0D;
                double x = Math.sin(angle) * distance + player.posX;
                double z = Math.cos(angle) * distance + player.posZ;
                double y = Math.cos(angle) * distance + player.posY;
                EntitySummonedSword entitySummonedSword = new EntitySummonedSword(world,player,99999);
                entitySummonedSword.setPosition((int) Math.round(((x - 30) + (Math.random() * ((x + 30) - (x - 30))))), (int) y,
                        (int) Math.round(((z - 30) + (Math.random() * ((z + 30) - (z - 30))))));
                entitySummonedSword.setLifeTime(80);
                world.spawnEntity(entitySummonedSword);
            }
            int count = 20 + StylishRankManager.getStylishRank(player);
            for (int i = 0; i < count; i++) {
                EntitySakuraEndManager entityDA = new EntitySakuraEndManager(world, player);
                world.spawnEntity(entityDA);
            }
            EntityDragonDimension superDimension = new EntityDragonDimension(world,player,count);
            superDimension.setPosition(player.posX,player.posY+8,player.posZ);
            superDimension.setLifeTime(10);
            world.spawnEntity(superDimension);
            for (int i = 0; i < 36; i++) {
                EntityDrive entityDrive = new EntityDrive(world, player, (float) count, false, 0.0F);
                entityDrive.setLocationAndAngles(player.posX, player.posY + (double)player.getEyeHeight() / 2.0, player.posZ, player.rotationYaw + (float)(10 * i), 0.0F);
                entityDrive.setDriveVector(0.5F);
                entityDrive.setLifeTime(80);
                entityDrive.setIsMultiHit(false);
                entityDrive.setRoll(90.0F);
                world.spawnEntity(entityDrive);
                EntityDrive entityDrive1 = new EntityDrive(world, player, (float) count, false, 0.0F);
                entityDrive1.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw + (float)(10 * i), 0.0F);
                entityDrive1.setDriveVector(0.5F);
                entityDrive1.setLifeTime(80);
                entityDrive1.setIsMultiHit(false);
                entityDrive1.setRoll(90.0F);
                world.spawnEntity(entityDrive1);
            }
            player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.8F, 1F);
            ItemSlashBlade.setComboSequence(tag, setCombo);
        }
    }
}
