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

public class SuperAstralShadowSpecialAttacks extends SpecialAttackBase {
    public static final SuperAstralShadowSpecialAttacks theSa = new SuperAstralShadowSpecialAttacks();

    private SuperAstralShadowSpecialAttacks() {
    }

    @Override
    public String toString() {
        return "SuperAstralShadowSpecialAttacks";
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
            int count = 50 + StylishRankManager.getStylishRank(player);
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                double angle = random.nextDouble() * 6.0D * 3.141592653589793D;
                double distance = random.nextGaussian() * 6.0D;
                double x = Math.sin(angle) * distance + player.posX;
                double z = Math.cos(angle) * distance + player.posZ;
                double y = Math.cos(angle) * distance + player.posY;
                boolean isBurst = (i % 2 == 0);
                float magicDamage = 8;
                EntityWitherSword entityDrive = new EntityWitherSword(world, player, magicDamage, 140.0F);
                entityDrive.setPosition((int) Math.round(((x - 30) + (Math.random() * ((x + 30) - (x - 30))))), (int) y,
                        (int) Math.round(((z - 30) + (Math.random() * ((z + 30) - (z - 30))))));
                entityDrive.setInterval(7 + i * 2);
                entityDrive.setLifeTime(200);
                int color = new Random().nextInt();
                entityDrive.setColor(color);
                entityDrive.setBurst(isBurst);
                world.spawnEntity(entityDrive);
            }
        }
    }
}
