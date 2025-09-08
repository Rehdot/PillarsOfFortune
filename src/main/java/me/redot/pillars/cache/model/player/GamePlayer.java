package me.redot.pillars.cache.model.player;

import me.redot.pillars.cache.model.game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GamePlayer<T extends Game<?>> {

    private final Player player;
    private final T game;
    private PlayerState state;

    public GamePlayer(Player player, T game) {
        this.player = player;
        this.game = game;
        this.state = PlayerState.ALIVE;
    }

    public void teleport(Location location) {
        this.player.teleportAsync(location);
    }

    public Location getLocation() {
        return this.player.getLocation();
    }

    public T getGame() {
        return this.game;
    }

    public boolean hasState(PlayerState s) {
        return this.state.equals(s);
    }

    public PlayerState getState() {
        return this.state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public Player getPlayer() {
        return this.player;
    }

    public UUID getId() {
        return this.player.getUniqueId();
    }

    public String getName() {
        return this.player.getName();
    }

}
