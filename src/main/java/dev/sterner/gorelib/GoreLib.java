package dev.sterner.gorelib;

import dev.sterner.gorelib.registry.GoreLibBlockEntityRegistry;
import dev.sterner.gorelib.registry.GoreLibDataTrackerRegistry;
import dev.sterner.gorelib.registry.GoreLibParticleTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GoreLib implements ModInitializer, ClientModInitializer {
    private static final String MODID = "gorelib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);


    public static Identifier id(String id) {
        return new Identifier(MODID, id);
    }

    @Override
    public void onInitialize() {
        GoreLibDataTrackerRegistry.init();
        GoreLibBlockEntityRegistry.init();
    }

    @Override
    public void onInitializeClient() {
        GoreLibParticleTypes.init();
    }
}