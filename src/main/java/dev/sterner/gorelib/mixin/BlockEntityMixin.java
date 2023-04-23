package dev.sterner.gorelib.mixin;

import dev.sterner.gorelib.interfaces.BlockEntityExtensions;
import dev.sterner.gorelib.interfaces.INBTSerializableCompound;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements BlockEntityExtensions, INBTSerializableCompound {

    @Unique
    private NbtCompound lodestone$extraData = null;

    @Shadow
    public abstract void readNbt(NbtCompound tag);

    @Shadow
    public abstract NbtCompound createNbtWithIdentifyingData();

    @Inject(at = @At("RETURN"), method = "writeIdentifyingData")
    private void gore_lib$saveMetadata(NbtCompound nbt, CallbackInfo ci) {
        if (lodestone$extraData != null && !lodestone$extraData.isEmpty()) {
            nbt.put("GoreData", lodestone$extraData);
        }
    }

    @Inject(at = @At("RETURN"), method = "readNbt")
    private void gore_lib$load(NbtCompound tag, CallbackInfo ci) {
        if (tag.contains("GoreData")) {
            lodestone$extraData = tag.getCompound("GoreData");
        } else if (tag.contains("gore_lib_ExtraEntityData")) {
            lodestone$extraData = tag.getCompound("gore_lib_ExtraEntityData");
        }
    }

    @Inject(method = "markRemoved", at = @At("TAIL"))
    public void gore_lib$invalidate(CallbackInfo ci) {
        invalidateCaps();
    }

    @Override
    public NbtCompound serializeNBT() {
        return this.createNbtWithIdentifyingData();
    }

    @Override
    public void deserializeNBT(NbtCompound nbt) {
        deserializeNBT(null, nbt);
    }

    @Override
    public NbtCompound getExtraCustomData() {
        if (lodestone$extraData == null) {
            lodestone$extraData = new NbtCompound();
        }
        return lodestone$extraData;
    }

    public void deserializeNBT(BlockState state, NbtCompound nbt) {
        this.readNbt(nbt);
    }
}
