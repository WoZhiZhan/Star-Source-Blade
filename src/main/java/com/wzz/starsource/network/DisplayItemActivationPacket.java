package com.wzz.starsource.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DisplayItemActivationPacket {
    public final ItemStack itemStack;

    public DisplayItemActivationPacket(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public DisplayItemActivationPacket(FriendlyByteBuf buf) {
        this.itemStack = buf.readItem();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(itemStack);
    }

    public static void handle(DisplayItemActivationPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                h(msg, ctx);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void h(DisplayItemActivationPacket msg, Supplier<NetworkEvent.Context> ctx) {
        Minecraft.getInstance().gameRenderer.displayItemActivation(msg.itemStack);
    }
}