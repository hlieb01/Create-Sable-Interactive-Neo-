package dev.hlieb01.createsableinteractive.block;

import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/** A speed-rated, destructible barrier for Create carriage collision boxes. */
public final class WarningBarrierBlock extends Block {
    private final double speedLimitMps;

    public WarningBarrierBlock(double speedLimitMps, BlockBehaviour.Properties properties) {
        super(properties);
        if (speedLimitMps <= 0) throw new IllegalArgumentException("speed limit must be positive");
        this.speedLimitMps = speedLimitMps;
    }

    public double getSpeedLimitMps() {
        return speedLimitMps;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || !(entity instanceof CarriageContraptionEntity carriage)) return;
        if (carriage.getCarriage() == null) return;

        Train train = carriage.getCarriage().train;
        double speedMps = Math.abs(train.speed) * 20.0;
        if (speedMps > speedLimitMps) {
            level.destroyBlock(pos, false);
            level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0f, 0.85f);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.CRIT, pos.getX() + .5, pos.getY() + .5,
                    pos.getZ() + .5, 12, .35, .35, .35, .08);
            }
            return;
        }

        train.speed = 0;
        train.targetSpeed = 0;
        train.navigation.cancelNavigation();
    }
}
