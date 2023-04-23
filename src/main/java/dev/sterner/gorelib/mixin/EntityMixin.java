package dev.sterner.gorelib.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.sterner.gorelib.interfaces.EntityExtension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;

@Mixin(Entity.class)
public class EntityMixin implements EntityExtension {

    @Shadow
    public World world;
    @Unique
    private Collection<ItemEntity> captureDrops = null;

    @WrapWithCondition(method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private boolean gore_lib$dropStack(World instance, Entity entity) {
        if (captureDrops() != null && entity instanceof ItemEntity itemEntity) {
            captureDrops().add(itemEntity);
            return false;
        }
        return true;
    }

    @Override
    @Nullable
    public Collection<ItemEntity> captureDrops() {
        return captureDrops;
    }

    @Override
    public Collection<ItemEntity> captureDrops(@Nullable Collection<ItemEntity> newCapture) {
        Collection<ItemEntity> oldDrops = captureDrops;
        this.captureDrops = newCapture;
        return oldDrops;
    }
}