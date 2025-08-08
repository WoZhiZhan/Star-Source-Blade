package com.wzz.starsource.init;

import com.wzz.starsource.ModMain;
import com.wzz.starsource.network.DisplayItemActivationPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModNetwork {
    private static final String PROTOCOL_VERSION = "1";
    private static int messageID = 0;
    public static final SimpleChannel PACKET_HANDLER =
            NetworkRegistry.newSimpleChannel(ResourceLocation.tryBuild(ModMain.MODID, "network"),
                    () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void register() {
        ModNetwork.addNetworkMessage(DisplayItemActivationPacket.class,
                DisplayItemActivationPacket::encode,
                DisplayItemActivationPacket::new,
                DisplayItemActivationPacket::handle);
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(nextId(), messageType, encoder, decoder, messageConsumer);
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer, NetworkDirection networkDirection) {
        PACKET_HANDLER.registerMessage(nextId(), messageType, encoder, decoder, messageConsumer, Optional.of(networkDirection));
    }

    private static int nextId() {
        return messageID++;
    }
}