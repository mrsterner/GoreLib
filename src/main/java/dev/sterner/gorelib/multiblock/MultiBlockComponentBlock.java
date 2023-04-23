package dev.sterner.gorelib.multiblock;

import dev.sterner.gorelib.block.GoreLibBlockWithEntity;
import dev.sterner.gorelib.registry.GoreLibBlockEntityRegistry;
import net.minecraft.block.AbstractBlock;

public class MultiBlockComponentBlock extends GoreLibBlockWithEntity<MultiBlockComponentBlockEntity> implements IGoreLibMultiBlockComponent {
    public MultiBlockComponentBlock(AbstractBlock.Settings settings) {
        super(settings);
        setBlockEntity(GoreLibBlockEntityRegistry.MULTIBLOCK_COMPONENT);
    }
}