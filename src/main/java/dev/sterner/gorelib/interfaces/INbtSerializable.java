package dev.sterner.gorelib.interfaces;

import net.minecraft.nbt.NbtCompound;

public interface INbtSerializable<T extends NbtCompound> {
    default T serializeNBT() {
        throw new RuntimeException("override serializeNBT!");
    }

    default void deserializeNBT(T nbt) {
        throw new RuntimeException("override deserializeNBT!");
    }
}