package dev.sterner.gorelib.entity;

import net.minecraft.entity.ItemEntity;

import java.util.Collection;

public interface EntityExtension {
    Collection<ItemEntity> captureDrops();

    Collection<ItemEntity> captureDrops(Collection<ItemEntity> value);
}
