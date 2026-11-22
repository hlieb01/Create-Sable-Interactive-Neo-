package dev.hlieb01.createsableinteractive.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Common base for addon blocks whose special behaviour is supplied by later systems. */
public class SimpleAddonBlock extends Block {
    public SimpleAddonBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
}
