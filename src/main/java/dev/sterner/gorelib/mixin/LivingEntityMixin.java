package dev.sterner.gorelib.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.sterner.gorelib.entity.EntityExtension;
import dev.sterner.gorelib.event.LivingEntityDropEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements EntityExtension {

    @Shadow
    protected int playerHitTimer;

    @Shadow
    public abstract int getXpToDrop();

    @Unique
    private final ThreadLocal<Integer> dropLootingLevel = new ThreadLocal<>();

    @Inject(method = "drop", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;playerHitTimer : I"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void gore_lib$dropPre(DamageSource source, CallbackInfo ci, Entity entity, int lootingLevel) {
        this.captureDrops(new ArrayList<>());
        dropLootingLevel.set(lootingLevel);
    }

    @Inject(method = "drop", at = @At("TAIL"))
    private void gore_lib$dropPost(DamageSource source, CallbackInfo ci) {
        boolean bl = this.playerHitTimer > 0;
        Collection<ItemEntity> drops = captureDrops(null);
        if (LivingEntityDropEvent.SHOULD_DROP_ON_DEATH.invoker().shouldDrop(LivingEntity.class.cast(this), source, drops, dropLootingLevel.get(), bl)) {
            drops.forEach(e -> e.getWorld().spawnEntity(e));
        }

        dropLootingLevel.remove();
    }

    @WrapWithCondition(method = "dropXp", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"))
    private boolean gore_lib$dropXp(ServerWorld serverWorld, Vec3d vec3d, int xpToDrop) {
        return LivingEntityDropEvent.SHOULD_DROP_XP.invoker().shouldDrop(LivingEntity.class.cast(this), vec3d, xpToDrop);
    }
}
