package dev.sterner.gorelib.mixin;

import dev.sterner.gorelib.event.LivingEntityTargetingEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.UpdateAttackTargetTask;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UpdateAttackTargetTask.class)
public class UpdateAttackTargetTaskMixin {

    @Inject(method = "updateAttackTarget", at = @At("TAIL"))
    private static <E extends MobEntity> void gore_lib$updateAttackTarget(E mob, LivingEntity livingEntity, CallbackInfo ci) {
        LivingEntity newTarget = LivingEntityTargetingEvent.CHANGE_TARGET.invoker().onChangeTarget(mob, livingEntity);
        mob.getBrain().remember(MemoryModuleType.ATTACK_TARGET, newTarget);
        mob.getBrain().forget(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        LivingEntityTargetingEvent.ON_TARGET.invoker().onTarget(mob, newTarget);
    }
}
