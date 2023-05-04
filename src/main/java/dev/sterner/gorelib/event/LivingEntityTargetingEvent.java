package dev.sterner.gorelib.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public class LivingEntityTargetingEvent {

    /**
     * Triggers when an entity is setting its target
     */
    public static final Event<OnTarget> ON_TARGET = EventFactory.createArrayBacked(OnTarget.class, listeners -> (livingEntity, originalTarget) -> {
        for (OnTarget listener : listeners) {
            return listener.onTarget(livingEntity, originalTarget);
        }
        return true;
    });

    /**
     * Triggers before an entity sets its target, allowing for the event to change its target
     */
    public static final Event<ChangeTarget> CHANGE_TARGET = EventFactory.createArrayBacked(ChangeTarget.class, listeners -> (livingEntity, originalTarget) -> {
        for (ChangeTarget listener : listeners) {
            return listener.onChangeTarget(livingEntity, originalTarget);
        }
        return originalTarget;
    });

    public interface OnTarget {
        boolean onTarget(LivingEntity livingEntity, LivingEntity originalTarget);
    }

    public interface ChangeTarget {
        LivingEntity onChangeTarget(LivingEntity livingEntity, LivingEntity originalTarget);
    }
}
