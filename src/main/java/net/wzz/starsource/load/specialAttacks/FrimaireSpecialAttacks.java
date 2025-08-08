package net.wzz.starsource.load.specialAttacks;

import mods.flammpfeil.slashblade.entity.EntitySummonedSword;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Random;

import static mods.flammpfeil.slashblade.item.ItemSlashBlade.ComboSequence.None;

public class FrimaireSpecialAttacks extends SpecialAttackBase {
    private final ItemSlashBlade.ComboSequence setCombo;
    public static final FrimaireSpecialAttacks theSa = new FrimaireSpecialAttacks(0.5f,100,true,None);

    private FrimaireSpecialAttacks(float speed, int lifetime, boolean multihit, ItemSlashBlade.ComboSequence setCombo) {
        this.setCombo = setCombo;
    }

    @Override
    public String toString() {
        return "FrimaireSpecialAttacks";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        ItemSlashBlade.damageItem(stack, 10, player);
        if (!world.isRemote) {
            Random random = new Random();
            for (int i = 0; i < 400; i++) {
                double angle = random.nextDouble() * 10.0D * 3.141592653589793D;
                double distance = random.nextGaussian() * 10.0D;
                double x = Math.sin(angle) * distance + player.posX;
                double z = Math.cos(angle) * distance + player.posZ;
                double y = Math.cos(angle) * distance + player.posY;
                EntitySummonedSword entitySummonedSword = new EntitySummonedSword(world,player,20);
                entitySummonedSword.setPosition((int) Math.round(((x - 30) + (Math.random() * ((x + 30) - (x - 30))))), (int) y,
                        (int) Math.round(((z - 30) + (Math.random() * ((z + 30) - (z - 30))))));
                entitySummonedSword.setLifeTime(110);
                world.spawnEntity(entitySummonedSword);
            }
            player.playSound(SoundEvents.BLOCK_GLASS_BREAK, 0.8F, 1F);
            ItemSlashBlade.setComboSequence(tag, setCombo);
        }
    }
}
