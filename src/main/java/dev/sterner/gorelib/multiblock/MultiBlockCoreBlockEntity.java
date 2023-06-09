package dev.sterner.gorelib.multiblock;

import dev.sterner.gorelib.blockentity.GoreLibBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class MultiBlockCoreBlockEntity extends GoreLibBlockEntity implements IMultiBlockCore {
    ArrayList<BlockPos> componentPositions = new ArrayList<>();

    public final MultiBlockStructure structure;

    public MultiBlockCoreBlockEntity(BlockEntityType<?> type, MultiBlockStructure structure, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.structure = structure;
        setupMultiblock(pos);
    }

    @Override
    public MultiBlockStructure getStructure() {
        return structure;
    }

    @Override
    public ArrayList<BlockPos> getComponentPositions() {
        return componentPositions;
    }

    @Override
    public void onBreak(@Nullable PlayerEntity player) {
        destroyMultiblock(player, world, pos);
    }
}