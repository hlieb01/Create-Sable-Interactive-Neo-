package dev.hlieb01.createsableinteractive.mixin;

import com.simibubi.create.content.trains.entity.Navigation;
import com.simibubi.create.content.trains.graph.TrackNode;
import com.simibubi.create.content.trains.station.GlobalStation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Ignore station approach-side facing in Navigation's route search only. */
@Mixin(value = Navigation.class, remap = false)
public abstract class NavigationStationMixin {
    @Redirect(
        method = "search(DDZLjava/util/ArrayList;Lcom/simibubi/create/content/trains/entity/Navigation$StationTest;)V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/simibubi/create/content/trains/station/GlobalStation;canApproachFrom(Lcom/simibubi/create/content/trains/graph/TrackNode;)Z",
            remap = false
        ),
        remap = false
    )
    private boolean createSableInteractive$allowSearchFromEitherSide(GlobalStation station, TrackNode side) {
        return true;
    }
}
