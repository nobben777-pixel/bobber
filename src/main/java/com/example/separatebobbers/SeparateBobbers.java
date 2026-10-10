package com.example.separatebobbers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class SeparateBobbers implements ModInitializer {
    @Override
    public void onInitialize() {
        // Чистим память, когда игрок вышел или сервер остановился
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                BobberTracker.clear(handler.getPlayer().getUUID()));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> BobberTracker.clearAll());
    }
}
