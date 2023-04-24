package dev.sterner.gorelib.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;

public class NbtUtils {

    public static Vec3d toVec3d(NbtCompound nbt) {
        return new Vec3d(nbt.getDouble("X"), nbt.getDouble("Y"), nbt.getDouble("Z"));
    }

    public static NbtCompound fromVec3d(Vec3d pos) {
        NbtCompound nbtCompound = new NbtCompound();
        if (pos != null) {
            nbtCompound.putDouble("X", pos.getX());
            nbtCompound.putDouble("Y", pos.getY());
            nbtCompound.putDouble("Z", pos.getZ());
        }
        return nbtCompound;
    }
}