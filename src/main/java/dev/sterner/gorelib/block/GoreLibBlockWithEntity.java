package dev.sterner.gorelib.block;

import dev.sterner.gorelib.blockentity.GoreLibBlockEntity;
import dev.sterner.gorelib.util.BlockTickUtils;
import net.minecraft.block.*;
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
        return hasTileEntity(state) ? blockEntityType.instantiate(pos, state) : null;
    }

    public boolean hasTileEntity(BlockState state) {
        return this.blockEntityType != null;
    }

    @Override
    @Nullable
    public <B extends BlockEntity> BlockEntityTicker<B> getTicker(@NotNull World level, @NotNull BlockState state, @NotNull BlockEntityType<B> type) {
        return (BlockEntityTicker<B>) ticker;
    }

    @Override
    public void onPlaced(@NotNull World pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        if (hasTileEntity(pState)) {
            if (pLevel.getBlockEntity(pPos) instanceof GoreLibBlockEntity simpleBlockEntity) {
                simpleBlockEntity.onPlace(pPlacer, pStack);
            }
        }
        super.onPlaced(pLevel, pPos, pState, pPlacer, pStack);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        if (hasTileEntity(state)) {
            if (world.getBlockEntity(pos) instanceof GoreLibBlockEntity simpleBlockEntity) {
                ItemStack stack = simpleBlockEntity.onClone(state, world, pos);
                if (!stack.isEmpty()) {
                    return stack;
                }
            }
        }
        return super.getPickStack(world, pos, state);
    }

    @Override
    public void onBreak(@NotNull World level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull PlayerEntity player) {
        onBlockBroken(state, level, pos, player);
        super.onBreak(level, pos, state, player);
    }

    public void onBlockBroken(BlockState state, BlockView level, BlockPos pos, @Nullable PlayerEntity player) {
        if (hasTileEntity(state)) {
            if (level.getBlockEntity(pos) instanceof GoreLibBlockEntity simpleBlockEntity) {
                simpleBlockEntity.onBreak(player);
            }
        }
    }

    @Override
    public void onEntityCollision(@NotNull BlockState pState, @NotNull World pLevel, @NotNull BlockPos pPos, @NotNull Entity pEntity) {
        if (hasTileEntity(pState)) {
            if (pLevel.getBlockEntity(pPos) instanceof GoreLibBlockEntity simpleBlockEntity) {
                simpleBlockEntity.onEntityInside(pState, pLevel, pPos, pEntity);
            }
        }
        super.onEntityCollision(pState, pLevel, pPos, pEntity);
    }

    @Override
    public void neighborUpdate(@NotNull BlockState pState, @NotNull World pLevel, @NotNull BlockPos pPos, @NotNull Block pBlock, @NotNull BlockPos pFromPos, boolean pIsMoving) {
        if (hasTileEntity(pState)) {
            if (pLevel.getBlockEntity(pPos) instanceof GoreLibBlockEntity simpleBlockEntity) {
                simpleBlockEntity.onNeighborUpdate(pState, pPos, pFromPos);
            }
        }
        super.neighborUpdate(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
    }


    @NotNull
    @Override
    public ActionResult onUse(@NotNull BlockState state, @NotNull World level, @NotNull BlockPos pos, @NotNull PlayerEntity player, @NotNull Hand hand, @NotNull BlockHitResult ray) {
        if (hasTileEntity(state)) {
            if (level.getBlockEntity(pos) instanceof GoreLibBlockEntity simpleBlockEntity) {
                return simpleBlockEntity.onUse(player, hand);
            }
        }
        return super.onUse(state, level, pos, player, hand, ray);
    }
}