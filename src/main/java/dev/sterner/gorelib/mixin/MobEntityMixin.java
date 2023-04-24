package dev.sterner.gorelib.mixin;

import dev.sterner.gorelib.event.LivingEntityTargetingEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Shadow
    @Nullable
    private LivingEntity target;

    @Inject(method = "setTarget", at = @At("HEAD"))
    private void gore_lib$setTarget(LivingEntity target, CallbackInfo ci) {
        MobEntity mobEntity = MobEntity.class.cast(this);
        LivingEntity newTarget = LivingEntityTargetingEvent.CHANGE_TARGET.invoker().onChangeTarget(mobEntity, target);
        this.target = newTarget;
        LivingEntityTargetingEvent.ON_TARGET.invoker().onTarget(mobEntity, newTarget);
    }
}
