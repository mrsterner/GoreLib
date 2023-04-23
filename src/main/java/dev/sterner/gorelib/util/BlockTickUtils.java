package dev.sterner.gorelib.util;

import dev.sterner.gorelib.blockentity.GoreLibBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTickUtils {

    public static <B extends GoreLibBlockEntity> void tickSided(World world, BlockPos pos, BlockState state, B blockEntity) {
        blockEntity.tick();
        if (world.isClient()) {
            blockEntity.clientTick();
        } else {
            blockEntity.serverTick();
        }
    }
}