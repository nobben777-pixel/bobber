package com.example.separatebobbers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;

/**
 * Хранит для каждого игрока: ID удочки -> её поплавок.
 * Ванильное поле player.fishing подменяется только на время use() удочки.
 * Работает только на серверном потоке.
 */
public final class BobberTracker {
    private static final Map<UUID, Map<String, FishingHook>> HOOKS = new HashMap<>();
    private static String pendingId;

    private BobberTracker() {}

    /** Перед use(): подставляем игроку поплавок именно этой удочки (или null). */
    public static void begin(Player player, String rodId) {
        pendingId = rodId;
        FishingHook hook = null;
        Map<String, FishingHook> map = HOOKS.get(player.getUUID());
        if (map != null) {
            hook = map.get(rodId);
            if (hook != null && hook.isRemoved()) {
                map.remove(rodId);
                hook = null;
            }
        }
        player.fishing = hook;
    }

    /** После use(): запоминаем, какой поплавок теперь у этой удочки. */
    public static void end(Player player) {
        if (pendingId == null) return;

        Map<String, FishingHook> map = HOOKS.computeIfAbsent(player.getUUID(), k -> new HashMap<>());
        FishingHook hook = player.fishing;
        if (hook != null && !hook.isRemoved()) {
            map.put(pendingId, hook);
        } else {
            map.remove(pendingId);
        }
        map.values().removeIf(Entity::isRemoved);
        pendingId = null;
    }

    public static void clear(UUID playerId) {
        HOOKS.remove(playerId);
    }

    public static void clearAll() {
        HOOKS.clear();
        pendingId = null;
    }
}
