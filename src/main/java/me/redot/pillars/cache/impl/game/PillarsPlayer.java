package me.redot.pillars.cache.impl.game;

import me.redot.pillars.cache.model.player.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PillarsPlayer extends GamePlayer<PillarsGame> {

    private Location spawnLocation;

    public PillarsPlayer(Player player, PillarsGame game) {
        super(player, game);
    }

    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
    }

}
