package dev.sterner.gorelib.registry;

import dev.sterner.gorelib.GoreLib;
import dev.sterner.gorelib.particle.ItemStackBeamParticle;
import dev.sterner.gorelib.particle.ItemStackBeamParticleEffect;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface GoreLibParticleTypes {
    Map<ParticleType<?>, Identifier> PARTICLE_TYPES = new LinkedHashMap<>();

    ParticleType<ItemStackBeamParticleEffect> ITEM_BEAM_PARTICLE = register("item_beam_particle", FabricParticleTypes.complex(ItemStackBeamParticleEffect.PARAMETERS_FACTORY));

    static <T extends ParticleEffect> ParticleType<T> register(String name, ParticleType<T> type) {
        PARTICLE_TYPES.put(type, GoreLib.id(name));
        return type;
    }

    static void init() {
        PARTICLE_TYPES.keySet().forEach(particleType -> Registry.register(Registries.PARTICLE_TYPE, PARTICLE_TYPES.get(particleType), particleType));
        ParticleFactoryRegistry.getInstance().register(ITEM_BEAM_PARTICLE, new ItemStackBeamParticle.ItemFactory());
    }
}
