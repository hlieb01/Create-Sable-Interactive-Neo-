package dev.hlieb01.createsableinteractive.mixin;

import com.simibubi.create.content.trains.entity.Train;
import dev.hlieb01.createsableinteractive.config.TrainSpeedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * TODO: replace this Create-side detach with a real Sable SubLevel conversion.
 * This deliberately does not claim to provide gravity or permanent derailment:
 * Create.detachFromTracks() stores migration points and can reattach the train.
 */
@Mixin(value = Train.class, remap = false)
public abstract class TrainDerailmentTodoMixin {
    @Inject(method = "crash", at = @At("HEAD"), remap = false)
    private void createSableInteractive$markHighSpeedCrashForSableDerailment(CallbackInfo ci) {
        Train train = (Train) (Object) this;
        double configuredMaxSpeedMps = TrainSpeedConfig.maxTrainSpeedMps();

        // Only mark crashes above an explicitly configured speed cap; -1 means unlimited.
        if (!train.derailed && configuredMaxSpeedMps > 0
            && Math.abs(train.speed) * 20.0 > configuredMaxSpeedMps) {
            // TODO: create/initialize a Sable SubLevel through its container, transfer the
            // contraption, velocities, passengers and collision state, then disable Create
            // track migration. No public safe train-to-SubLevel conversion API was found.
            train.detachFromTracks();
        }
    }
}
