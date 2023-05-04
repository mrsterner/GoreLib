package dev.sterner.gorelib.mixin;


import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.sterner.gorelib.entity.EntityExtension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin implements EntityExtension {

    @WrapWithCondition(method = "dropItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private boolean gorelib$dropItem(World instance, Entity entity) {
        if (captureDrops() != null && entity instanceof ItemEntity itemEntity) {
            captureDrops().add(itemEntity);
            return false;
        }
        return true;
    }
}
