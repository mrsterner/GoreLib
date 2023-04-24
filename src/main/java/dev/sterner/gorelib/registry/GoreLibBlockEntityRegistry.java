package dev.sterner.gorelib.registry;

import dev.sterner.gorelib.GoreLib;
import dev.sterner.gorelib.multiblock.IGoreLibMultiBlockComponent;
import dev.sterner.gorelib.multiblock.MultiBlockComponentBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


public interface GoreLibBlockEntityRegistry {
    Map<Identifier, BlockEntityType<?>> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

    BlockEntityType<MultiBlockComponentBlockEntity> MULTIBLOCK_COMPONENT = register("multiblock_component", BlockEntityType.Builder.create(MultiBlockComponentBlockEntity::new, getBlocks(IGoreLibMultiBlockComponent.class)).build(null));

    static Block[] getBlocks(Class<?>... blockClasses) {
        Registry<Block> blocks = Registry.BLOCK;
        ArrayList<Block> matchingBlocks = new ArrayList<>();
        for (Block block : blocks) {
            if (Arrays.stream(blockClasses).anyMatch(b -> b.isInstance(block))) {
                matchingBlocks.add(block);
            }
        }
        return matchingBlocks.toArray(new Block[0]);
    }

    static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType<T> type) {
        BLOCK_ENTITY_TYPES.put(GoreLib.id(id), type);
        return type;
    }

    static void init() {
        BLOCK_ENTITY_TYPES.forEach((id, entityType) -> Registry.register(Registry.BLOCK_ENTITY_TYPE, id, entityType));
    }
}