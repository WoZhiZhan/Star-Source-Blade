package net.wzz.starsource.load.specialAttacks;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.wzz.starsource.entity.EntitySuperDimension;

public class ShootingStarSpecialAttacks extends SpecialAttackBase {

    public static final ShootingStarSpecialAttacks theSa = new ShootingStarSpecialAttacks();

    private ShootingStarSpecialAttacks() {
    }

    @Override
    public String toString() {
        return "ShootingStarSpecialAttacks";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        final int cost = -10;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
            ItemSlashBlade.damageItem(stack, 10, player);
        }
        int count = 8 + StylishRankManager.getStylishRank(player);
        EntitySuperDimension superDimension = new EntitySuperDimension(world,player,count);
        superDimension.setPosition(player.posX,player.posY+1,player.posZ);
        superDimension.setLifeTime(200);
        world.spawnEntity(superDimension);
    }
}
