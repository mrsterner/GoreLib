package dev.sterner.gorelib.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class LivingEntityVisibilityEvent {

    /**
     * Changes the visibility range of entities detecting other entites (seeing them)
     */
    public static final Event<ModifyVisibility> MODIFY_MULTIPLIER = EventFactory.createArrayBacked(ModifyVisibility.class, listeners -> (livingEntity, entity, originalMultiplier) -> {
        for (ModifyVisibility listener : listeners) {
            return Math.max(0, listener.modify(livingEntity, entity, originalMultiplier));
        }
        return originalMultiplier;
    });

    @FunctionalInterface
    public interface ModifyVisibility {
        double modify(LivingEntity livingEntity, Entity entity, double originalMultiplier);
    }
}
