package dev.hlieb01.createsableinteractive.mixin;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import dev.ryanhcode.sable.mixinterface.entity.entity_sublevel_collision.EntityMovementExtension;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContraptionEntity.class)
public abstract class AbstractContraptionEntityMixin {
    @Inject(method = "tick", at = @At("TAIL"), remap = false)
    private void createSableInteractive$trackPassengerOnSubLevel(CallbackInfo ci) {
        Entity contraptionEntity = (Entity) (Object) this;
        SubLevel trackingSubLevel = ((EntityMovementExtension) contraptionEntity).sable$getTrackingSubLevel();

        // A normal Create contraption is not itself a Sable sub-level. Only propagate a
        // tracking level when one has already been assigned to this contraption entity.
        if (trackingSubLevel == null) {
            return;
        }

        for (Entity passenger : contraptionEntity.getPassengers()) {
            if (passenger instanceof Player player) {
                ((EntityMovementExtension) player).sable$setTrackingSubLevel(trackingSubLevel);
            }
        }
    }
}
