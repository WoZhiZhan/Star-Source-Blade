package net.wzz.starsource.load.specialAttacks;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.wzz.starsource.entity.EntityAnnihilationDimension;
import net.wzz.starsource.entity.EntityColorLightningBolt;

public class AnnihilationSpecialAttacks extends SpecialAttackBase {
    public static final AnnihilationSpecialAttacks theSa = new AnnihilationSpecialAttacks();

    private AnnihilationSpecialAttacks() {
    }

    @Override
    public String toString() {
        return "Annihilation";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        final int cost = -10;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
            ItemSlashBlade.damageItem(stack, 10, player);
        }
        EntityAnnihilationDimension entityDrive = new EntityAnnihilationDimension(world, player, 100);
        entityDrive.setPosition(player.posX,player.posY+1,player.posZ);
        entityDrive.setLifeTime(1000);
        world.spawnEntity(entityDrive);
        for (Entity e : world.getEntitiesWithinAABBExcludingEntity(null,
                player.getEntityBoundingBox().grow(40))) {
            if (e != null && !(e instanceof EntityPlayer) && !(e instanceof EntityColorLightningBolt) && !(e instanceof EntityAnnihilationDimension)) {
                e.updateBlocked = true;
            }
        }
    }
}
