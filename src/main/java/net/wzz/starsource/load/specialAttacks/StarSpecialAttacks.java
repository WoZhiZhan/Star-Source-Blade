package net.wzz.starsource.load.specialAttacks;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class StarSpecialAttacks extends SpecialAttackBase {

    public static final StarSpecialAttacks theSa = new StarSpecialAttacks();

    private StarSpecialAttacks() {
    }

    @Override
    public String toString() {
        return "StarSpecialAttacks";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        final int cost = -10;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
            ItemSlashBlade.damageItem(stack, 10, player);
        }
        int count = 5 + StylishRankManager.getStylishRank(player);
        for (int i = 0; i < count; i++) {
            if (!world.isRemote) {
                EntityDrive drive = new EntityDrive(world,player,8);
                drive.setPosition(player.lastTickPosX,player.lastTickPosY+1,player.lastTickPosZ);
                world.spawnEntity(drive);
            }
        }
    }
}
