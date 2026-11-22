package dev.hlieb01.createsableinteractive.network;

import dev.hlieb01.createsableinteractive.CreateSableInteractive;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TrainControlPayload(int inputMask) implements CustomPacketPayload {
    public static final int ACCELERATE = 1;
    public static final int BRAKE = 2;

    public static final Type<TrainControlPayload> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(CreateSableInteractive.MOD_ID, "train_control")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, TrainControlPayload> STREAM_CODEC =
        StreamCodec.composite(ByteBufCodecs.VAR_INT, TrainControlPayload::inputMask, TrainControlPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
