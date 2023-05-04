package dev.sterner.gorelib.blockentity;

import dev.sterner.gorelib.block.GoreLibBlockWithEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GoreLibBlockEntity extends BlockEntity  {
    public boolean needsSync;

    public GoreLibBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * Triggered from:
     * {@link GoreLibBlockWithEntity#onBreak(World, BlockPos, BlockState, PlayerEntity)}
     *
     * @param player
     */
    public void onBreak(@Nullable PlayerEntity player) {

    }

    /**
     * Triggered from:
     * {@link GoreLibBlockWithEntity#onPlaced(World, BlockPos, BlockState, LivingEntity, ItemStack)}
     */
    public void onPlaced(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity placer, ItemStack stack) {
    }

    /**
     * Triggered from:
     * {@link GoreLibBlockWithEntity#neighborUpdate(BlockState, World, BlockPos, Block, BlockPos, boolean)}
     */
    public void neighborUpdate(@NotNull World world, BlockState state, BlockPos pos, BlockPos neighbor) {
    }

    /**
     * Triggered from:
     * {@link GoreLibBlockWithEntity#getPickStack(BlockView, BlockPos, BlockState)} )}
     */
    public ItemStack getPickStack(BlockState state, BlockView blockView, BlockPos pos) {
        return ItemStack.EMPTY;
    }

    /**
     * Triggered from:
     * {@link GoreLibBlockWithEntity#onUse(BlockState, World, BlockPos, PlayerEntity, Hand, BlockHitResult)}
     */
    public ActionResult onUse(PlayerEntity player, Hand hand) {
        return ActionResult.PASS;
    }

    /**
     * Triggered from:
     * {@link GoreLibBlockWithEntity#onEntityCollision(BlockState, World, BlockPos, Entity)} (BlockState, World, BlockPos, PlayerEntity, Hand, BlockHitResult)}
     */
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {

    }


    //Save nbt

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound tag = super.toInitialChunkDataNbt();
        this.writeNbt(tag);
        return tag;
    }

    @Override
    public void readNbt(NbtCompound pTag) {
        needsSync = true;
        super.readNbt(pTag);
    }

    public void sync(World world, BlockPos pos) {
        if (world != null && !world.isClient) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
            toUpdatePacket();
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        sync(world, pos);
    }


    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    /**
     * Called on both sides to tick the block entity as part of the ticker
     */
    public void tick() {
        if (needsSync) {
            init();
            needsSync = false;
        }
    }

    /**
     * Called only for server side when ticking
     */
    public void serverTick() {

    }

    /**
     * Called only for client side when ticking
     */
    public void clientTick() {

    }

    /**
     * Called on both sides to force an update after reload
     */
    public void init() {

    }
}
