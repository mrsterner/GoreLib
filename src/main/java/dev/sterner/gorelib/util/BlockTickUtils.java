package dev.sterner.gorelib.util;

import dev.sterner.gorelib.block.GoreLibBlockWithEntity;
import dev.sterner.gorelib.blockentity.GoreLibBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTickUtils {

    /**
     * Helper to make {@link GoreLibBlockWithEntity#setBlockEntity(BlockEntityType, boolean)} create a ticker for both sides
     *
     * @param world
     * @param pos
     * @param state
     * @param blockEntity
     * @param <B>
     */
    public static <B extends GoreLibBlockEntity> void tickSided(World world, BlockPos pos, BlockState state, B blockEntity) {
        blockEntity.tick();
        if (world.isClient()) {
            blockEntity.clientTick();
        } else {
            blockEntity.serverTick();
        }
    }
}