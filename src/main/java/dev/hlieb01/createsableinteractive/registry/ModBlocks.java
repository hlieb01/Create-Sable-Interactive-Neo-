package dev.hlieb01.createsableinteractive.registry;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.hlieb01.createsableinteractive.CreateSableInteractive;
import dev.hlieb01.createsableinteractive.block.GiantBoardBlock;
import dev.hlieb01.createsableinteractive.block.LinkNodeBlock;
import dev.hlieb01.createsableinteractive.block.PhysicMachineRotatorBlock;
import dev.hlieb01.createsableinteractive.block.SpeedCameraBlock;
import dev.hlieb01.createsableinteractive.block.SimpleAddonBlock;
import dev.hlieb01.createsableinteractive.block.WarningBarrierBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public final class ModBlocks {
    private ModBlocks() {}

    public static final BlockEntry<WarningBarrierBlock> WARNING_BARRIER_10 = barrier("warning_barrier_10", 10);
    public static final BlockEntry<WarningBarrierBlock> WARNING_BARRIER_20 = barrier("warning_barrier_20", 20);
    public static final BlockEntry<WarningBarrierBlock> WARNING_BARRIER_30 = barrier("warning_barrier_30", 30);
    public static final BlockEntry<WarningBarrierBlock> WARNING_BARRIER_50 = barrier("warning_barrier_50", 50);
    public static final BlockEntry<GiantBoardBlock> GIANT_BOARD = simple("giant_board", GiantBoardBlock::new);
    public static final BlockEntry<LinkNodeBlock> LINK_NODE = simple("link_node", LinkNodeBlock::new);
    public static final BlockEntry<PhysicMachineRotatorBlock> PHYSIC_MACHINE_ROTATOR =
        simple("physic_machine_rotator", PhysicMachineRotatorBlock::new);
    public static final BlockEntry<SpeedCameraBlock> SPEED_CAMERA = simple("speed_camera", SpeedCameraBlock::new);

    private static BlockEntry<WarningBarrierBlock> barrier(String id, double limit) {
        return CreateSableInteractive.REGISTRATE.block(id,
                properties -> new WarningBarrierBlock(limit, properties))
            .initialProperties(() -> Blocks.OAK_PLANKS)
            .simpleItem()
            .register();
    }

    private static <T extends SimpleAddonBlock> BlockEntry<T> simple(String id,
            Function<BlockBehaviour.Properties, T> factory) {
        return CreateSableInteractive.REGISTRATE.block(id, factory::apply)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .simpleItem()
            .register();
    }

    public static void register() {
        // Loading this class initializes all entries before NeoForge's registry events are dispatched.
    }
}
