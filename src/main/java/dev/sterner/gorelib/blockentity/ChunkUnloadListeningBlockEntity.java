package dev.sterner.gorelib.blockentity;

import dev.sterner.gorelib.interfaces.BlockEntityExtensions;

public interface ChunkUnloadListeningBlockEntity {
    default void onChunkUnloaded() {
        if (this instanceof BlockEntityExtensions ex) {
            ex.invalidateCaps();
        }
    }
}