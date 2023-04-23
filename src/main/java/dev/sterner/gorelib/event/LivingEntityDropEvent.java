package dev.sterner.gorelib.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

import java.util.Collection;

public class LivingEntityDropEvent {

    public static final Event<OnLivingDrops> SHOULD_DROP_ON_DEATH = EventFactory.createArrayBacked(OnLivingDrops.class, listeners -> (livingEntity, damageSource, drops, lootingLevel, recentlyHit) -> {
        for (OnLivingDrops listener : listeners) {
            return listener.shouldDrop(livingEntity, damageSource, drops, lootingLevel, recentlyHit);
        }
        return true;
    });

    public interface OnLivingDrops {
        boolean shouldDrop(LivingEntity livingEntity, DamageSource damageSource, Collection<ItemEntity> drops, int lootingLevel, boolean recentlyHit);
    }
}
