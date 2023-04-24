package dev.sterner.gorelib.multiblock;

import dev.sterner.gorelib.blockentity.GoreLibBlockEntity;
import dev.sterner.gorelib.registry.GoreLibBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class MultiBlockComponentBlockEntity extends GoreLibBlockEntity {
    public BlockPos corePos;

    public MultiBlockComponentBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public MultiBlockComponentBlockEntity(BlockPos pos, BlockState state) {
        super(GoreLibBlockEntityRegistry.MULTIBLOCK_COMPONENT, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        if (corePos != null) {
            nbt.putInt("CoreX", corePos.getX());
            nbt.putInt("CoreY", corePos.getY());
            nbt.putInt("CoreZ", corePos.getZ());
        }
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        corePos = nbt.contains("CoreX") ? new BlockPos(nbt.getInt("CoreX"), nbt.getInt("CoreY"), nbt.getInt("CoreZ")) : null;
        super.readNbt(nbt);
    }

    @Override
    public ActionResult onUse(PlayerEntity player, Hand hand) {
        if (corePos != null && world.getBlockEntity(corePos) instanceof MultiBlockCoreBlockEntity core) {
            return core.onUse(player, hand);
        }
        return super.onUse(player, hand);
    }

    @Override
    public void onBreak(@Nullable PlayerEntity player) {
        if (corePos != null && world.getBlockEntity(corePos) instanceof MultiBlockCoreBlockEntity core) {
            core.onBreak(player);
        }
        super.onBreak(player);
    }
}
