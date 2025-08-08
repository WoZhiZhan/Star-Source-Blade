package net.wzz.starsource.load.specialAttacks;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntitySakuraEndManager;
import mods.flammpfeil.slashblade.entity.EntityWitherSword;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Random;

public class StarBurstSpecialAttacks extends SpecialAttackBase {
    public static final StarBurstSpecialAttacks theSa = new StarBurstSpecialAttacks();

    private StarBurstSpecialAttacks() {
    }

    @Override
    public String toString() {
        return "StarBurstSpecialAttacks";
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
            for (int i = 0; i < count; i++) {
                EntitySakuraEndManager entityDA = new EntitySakuraEndManager(world, player);
                world.spawnEntity(entityDA);
            }
        }
    }
}
