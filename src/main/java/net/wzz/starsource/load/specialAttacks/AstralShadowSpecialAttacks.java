package net.wzz.starsource.load.specialAttacks;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityWitherSword;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Random;

public class AstralShadowSpecialAttacks extends SpecialAttackBase {
    public static final AstralShadowSpecialAttacks theSa = new AstralShadowSpecialAttacks();

    private AstralShadowSpecialAttacks() {
    }

    @Override
    public String toString() {
        return "AstralShadowSpecialAttacks";
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
                boolean isBurst = (i % 2 == 0);
                float magicDamage = 10;
                EntityWitherSword entityDrive = new EntityWitherSword(world, player, magicDamage, 140.0F);
                entityDrive.setInterval(7 + i * 2);
                entityDrive.setLifeTime(100);
                int color = new Random().nextInt();
                entityDrive.setColor(color);
                entityDrive.setBurst(isBurst);
                world.spawnEntity(entityDrive);
            }
        }
    }
}
