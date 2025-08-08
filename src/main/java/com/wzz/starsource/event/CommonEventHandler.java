package com.wzz.starsource.event;

import com.wzz.starsource.init.ModItems;
import com.wzz.starsource.item.BlessingStarsItem;
import com.wzz.starsource.network.DisplayItemActivationPacket;
import com.wzz.starsource.network.tuil.NetworkHandler;
import com.wzz.starsource.util.ModUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEventHandler {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        if (event.getAmount() >= living.getHealth()) {
            if (living instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.getOffhandItem().getItem() instanceof BlessingStarsItem) {
                    serverPlayer.getOffhandItem().shrink(1);
                    revive(event, serverPlayer);
                } else if (serverPlayer.getMainHandItem().getItem() instanceof BlessingStarsItem) {
                    serverPlayer.getMainHandItem().shrink(1);
                    revive(event, serverPlayer);
                }
            }
        }
    }

    private static void revive(LivingHurtEvent event, ServerPlayer serverPlayer) {
        event.setCanceled(true);
        serverPlayer.heal(4f);
        ModUtils.playSound(SoundEvents.TOTEM_USE, event.getEntity());
        ModUtils.spawnParticles(event.getEntity(), ParticleTypes.HAPPY_VILLAGER, 10, 0.1f);
        serverPlayer.getInventory().add(new ItemStack(ModItems.STAR_SOURCE_INGOT.get()));
        NetworkHandler.sendToPlayer(new DisplayItemActivationPacket(new ItemStack(ModItems.BLESSING_STARS.get())), serverPlayer);
    }
}
