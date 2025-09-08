package me.redot.pillars.cache.model.map;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/// Class for usage with commands for saving and loading maps
public class MapBuilderManager {

    private final Map<UUID, GameMap.Builder> playerBuilderMap;

    public MapBuilderManager() {
        this.playerBuilderMap = new HashMap<>();
    }

    public GameMap.Builder getOrCreateBuilder(UUID id) {
        return this.playerBuilderMap.computeIfAbsent(id, uuid -> GameMap.builder());
    }

    public GameMap.Builder getBuilder(UUID id) {
        return this.playerBuilderMap.get(id);
    }

    public void putBuilder(UUID id, GameMap.Builder builder) {
        this.playerBuilderMap.put(id, builder);
    }

}
