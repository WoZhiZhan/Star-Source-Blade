package net.wzz.starsource.load.specialAttacks;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.wzz.starsource.entity.EntityColorLightningBolt;

import static mods.flammpfeil.slashblade.item.ItemSlashBlade.ComboSequence.None;

public class JudgeStarSpecialAttacks extends SpecialAttackBase {
    private final ItemSlashBlade.ComboSequence setCombo;
    public static final JudgeStarSpecialAttacks theSa = new JudgeStarSpecialAttacks(0.5f,100,true,None);

    private JudgeStarSpecialAttacks(float speed, int lifetime, boolean multihit, ItemSlashBlade.ComboSequence setCombo) {
        this.setCombo = setCombo;
    }

    @Override
    public String toString() {
        return "JudgeStarSpecialAttacks";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        ItemSlashBlade.damageItem(stack, 10, player);
        for (Entity e : player.world.getEntitiesWithinAABBExcludingEntity(player,
                    player.getEntityBoundingBox().grow(15))) {
            if (e != player && e != null) {
                EntityColorLightningBolt entitySummonedSword = new EntityColorLightningBolt(world, e.posX, e.posY, e.posZ);
                world.spawnEntity(entitySummonedSword);
                e.attackEntityFrom(DamageSource.causePlayerDamage(player),25);
            }
        }
        ItemSlashBlade.setComboSequence(tag, setCombo);
    }
}
