package me.redot.pillars.cache.impl.service;

import me.redot.pillars.PillarsPlugin;
import me.redot.pillars.cache.model.map.GameMap;
import me.redot.pillars.cache.model.player.GamePlayer;
import me.redot.pillars.cache.model.player.PlayerComponent;
import me.redot.pillars.util.FileUtil;
import me.redot.pillars.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/// A service for player lobbying related queries
public class LobbyService {

    private Location lobbySpawnLoc;
    private final PillarsPlugin plugin;

    public LobbyService(PillarsPlugin plugin) {
        this.plugin = plugin;
    }

    public void sendAllToLobby(PlayerComponent<?, ?> playerComponent) {
        playerComponent.forEach(this::sendToLobby);
    }

    public void sendToLobby(GamePlayer<?> gamePlayer) {
        this.sendToLobby(gamePlayer.getPlayer());
    }

    public void sendToLobby(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setRespawnLocation(this.lobbySpawnLoc);
        player.setFlying(false);
        player.teleportAsync(this.lobbySpawnLoc);
    }

    public void loadMap() {
        this.loadMap(new Location(Bukkit.getWorlds().getFirst(), 0, 100, 0));
    }

    public void loadMap(Location origin) {
        this.loadMap(this.findLobbyMap(), origin, this.plugin);
    }

    public void loadMap(GameMap lobbyMap, Location origin, JavaPlugin plugin) {
        lobbyMap.load(origin, plugin, (loc, mat) -> {
            Block block = loc.getBlock();

            if (mat.equals(Material.SPONGE)) {
                this.lobbySpawnLoc = LocationUtil.centerBlock(loc);
                block.setType(Material.AIR);
            } else {
                block.setType(mat);
            }
        });
    }

    public GameMap findLobbyMap() {
        try {
            File mapFile = FileUtil.resolveMapFile("lobby");
            return GameMap.fromFile(mapFile);
        } catch (Exception e) {
            this.plugin.getLogger().warning("Failed to resolve lobby map! Ensure a map named 'lobby' is saved.\n");
            return null;
        }
    }

    public Location getLobbySpawnLoc() {
        return this.lobbySpawnLoc;
    }

}
