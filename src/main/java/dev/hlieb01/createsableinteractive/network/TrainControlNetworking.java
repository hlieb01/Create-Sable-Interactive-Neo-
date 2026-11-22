package dev.hlieb01.createsableinteractive.network;

import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import dev.hlieb01.createsableinteractive.config.TrainSpeedConfig;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class TrainControlNetworking {
    private TrainControlNetworking() {}

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(TrainControlNetworking::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(TrainControlPayload.TYPE, TrainControlPayload.STREAM_CODEC,
            (payload, context) -> context.enqueueWork(() -> handle(payload, context.player())));
    }

    private static void handle(TrainControlPayload payload, net.minecraft.world.entity.player.Player sender) {
        if (!(sender instanceof ServerPlayer player) || !(player.getVehicle() instanceof CarriageContraptionEntity entity)) return;
        if (entity.getCarriage() == null || entity.getControllingPlayer().filter(player.getUUID()::equals).isEmpty()) return;

        Train train = entity.getCarriage().train;
        if (train.derailed || train.graph == null) return;
        train.navigation.cancelNavigation();
        train.runtime.paused = true;
        train.manualTick = true;

        double acceleration = Math.max(train.acceleration(), 0.001);
        double maxSpeedMps = TrainSpeedConfig.maxTrainSpeedMps();
        double maxSpeedBlocksPerTick = maxSpeedMps < 0 ? Double.POSITIVE_INFINITY : maxSpeedMps / 20.0;

        if ((payload.inputMask() & TrainControlPayload.ACCELERATE) != 0) {
            double direction = Math.signum(train.speed);
            if (direction == 0) direction = 1;
            double nextSpeed = train.speed + direction * acceleration;
            train.speed = Math.copySign(Math.min(Math.abs(nextSpeed), maxSpeedBlocksPerTick), nextSpeed);
            train.targetSpeed = train.speed;
        }
        if ((payload.inputMask() & TrainControlPayload.BRAKE) != 0) {
            train.speed = Math.copySign(Math.max(0, Math.abs(train.speed) - acceleration * 2), train.speed);
            train.targetSpeed = train.speed;
        }
    }
}
