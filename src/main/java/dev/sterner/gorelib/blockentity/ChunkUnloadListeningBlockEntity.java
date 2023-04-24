package dev.sterner.gorelib.blockentity;

public interface ChunkUnloadListeningBlockEntity {
    default void onChunkUnloaded() {
        if (this instanceof BlockEntityExtensions ex) {
            ex.invalidateCaps();
        }
    }
}