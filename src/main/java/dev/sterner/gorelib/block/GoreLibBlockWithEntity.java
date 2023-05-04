package dev.sterner.gorelib.block;

import dev.sterner.gorelib.blockentity.GoreLibBlockEntity;
import dev.sterner.gorelib.util.BlockTickUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This is a Block with an BlockEntity with a few helper functions to make ticking block entities and other methods run on the BlockEntity instead of the Block
 *
 * @param <T>
 */
public class GoreLibBlockWithEntity<T extends GoreLibBlockEntity> extends BlockWithEntity {
    protected BlockEntityType<T> blockEntityType = null;
    protected BlockEntityTicker<T> ticker = null;

    public GoreLibBlockWithEntity(Settings properties) {
        super(properties);

    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }


    /**
     * Sets the block entity with a ticker enabled
     */
    public GoreLibBlockWithEntity<T> setBlockEntity(BlockEntityType<T> type) {
        return setBlockEntity(type, true);
    }

    /**
     * Sets the block entity with an optional ticker
     */
    public GoreLibBlockWithEntity<T> setBlockEntity(BlockEntityType<T> type, boolean shouldTick) {
        this.blockEntityType = type;
        if (shouldTick) {
            this.ticker = BlockTickUtils::tickSided;
        }
        return this;
    }


    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return hasBlockEntity(state) ? blockEntityType.instantiate(pos, state) : null;
    }

    public boolean hasBlockEntity(BlockState state) {
        return this.blockEntityType != null;
    }

    @Override
    @Nullable
    public <B extends BlockEntity> BlockEntityTicker<B> getTicker(@NotNull World world, @NotNull BlockState state, @NotNull BlockEntityType<B> type) {
        return (BlockEntityTicker<B>) ticker;
    }

    @Override
    public void onPlaced(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity livingEntity, @NotNull ItemStack itemStack) {
        if (hasBlockEntity(state)) {
            if (world.getBlockEntity(pos) instanceof GoreLibBlockEntity simpleBlockEntity) {
                simpleBlockEntity.onPlaced(world, pos, state, livingEntity, itemStack);
            }
        }
        super.onPlaced(world, pos, state, livingEntity, itemStack);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        if (hasBlockEntity(state)) {
            if (world.getBlockEntity(pos) instanceof GoreLibBlockEntity simpleBlockEntity) {
                ItemStack stack = simpleBlockEntity.getPickStack(state, world, pos);
                if (!stack.isEmpty()) {
                    return stack;
                }
            }
        }
        return super.getPickStack(world, pos, state);
    }

    @Override
    public void onBreak(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull PlayerEntity player) {
        onBlockBroken(state, world, pos, player);
        super.onBreak(world, pos, state, player);
    }

    public void onBlockBroken(BlockState state, BlockView world, BlockPos pos, @Nullable PlayerEntity player) {
        if (hasBlockEntity(state)) {
            if (world.getBlockEntity(pos) instanceof GoreLibBlockEntity simpleBlockEntity) {
                simpleBlockEntity.onBreak(player);
            }
        }
    }

    @Override
    public void onEntityCollision(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (hasBlockEntity(state)) {
            if (world.getBlockEntity(pos) instanceof GoreLibBlockEntity simpleBlockEntity) {
                simpleBlockEntity.onEntityCollision(state, world, pos, entity);
            }
        }
        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    public void neighborUpdate(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos neighbourPos, boolean isMoving) {
        if (hasBlockEntity(state)) {
            if (world.getBlockEntity(pos) instanceof GoreLibBlockEntity simpleBlockEntity) {
                simpleBlockEntity.neighborUpdate(world, state, pos, neighbourPos);
            }
        }
        super.neighborUpdate(state, world, pos, block, neighbourPos, isMoving);
    }


    @NotNull
    @Override
    public ActionResult onUse(@NotNull BlockState state, @NotNull World level, @NotNull BlockPos pos, @NotNull PlayerEntity player, @NotNull Hand hand, @NotNull BlockHitResult ray) {
        if (hasBlockEntity(state)) {
            if (level.getBlockEntity(pos) instanceof GoreLibBlockEntity simpleBlockEntity) {
                return simpleBlockEntity.onUse(player, hand);
            }
        }
        return super.onUse(state, level, pos, player, hand, ray);
    }
}