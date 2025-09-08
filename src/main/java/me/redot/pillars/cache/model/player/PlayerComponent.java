package me.redot.pillars.cache.model.player;

import me.redot.pillars.cache.model.game.BaseGame;
import me.redot.pillars.cache.model.game.Game;
import me.redot.pillars.util.CollectionUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class PlayerComponent<P extends GamePlayer<G>, G extends Game<P>> {

    private final Map<UUID, P> playerMap;
    private final G game;

    @SuppressWarnings("unchecked")
    public <B extends BaseGame<G, P>> PlayerComponent(B game) {
        this((G) game);
    }

    public PlayerComponent(G game) {
        this.playerMap = new HashMap<>();
        this.game = game;
    }

    public void forEach(Consumer<P> consumer) {
        for (Map.Entry<UUID, P> entry : this.playerMap.entrySet()) {
            consumer.accept(entry.getValue());
        }
    }

    public void forEachAlive(Consumer<P> consumer) {
        this.forEachWithState(PlayerState.ALIVE, consumer);
    }

    public void forEachEliminated(Consumer<P> consumer) {
        this.forEachWithState(PlayerState.ELIMINATED, consumer);
    }

    public void forEachWithState(PlayerState state, Consumer<P> consumer) {
        this.playerMap.values().stream()
                .filter(p -> p.hasState(state))
                .forEach(consumer);
    }

    public int countPlayers() {
        return this.playerMap.size();
    }

    public int alivePlayerCount() {
        return this.countPlayers(PlayerState.ALIVE);
    }

    public int eliminatedPlayerCount() {
        return this.countPlayers(PlayerState.ELIMINATED);
    }

    public int countPlayers(PlayerState state) {
        int i = 0;
        for (P player : this.playerMap.values()) {
            if (player.hasState(state)) {
                i++;
            }
        }
        return i;
    }

    public Collection<P> getPlayers() {
        return this.playerMap.values();
    }

    public P firstWithState(PlayerState state) {
        return this.playerMap.values().stream().filter(p -> {
            return p.hasState(state);
        }).findFirst().get();
    }

    public P randomPlayer() {
        return Objects.requireNonNull(CollectionUtil.randomElement(this.playerMap.entrySet())).getValue();
    }

    @Nullable
    public P getPlayer(UUID id) {
        return this.playerMap.get(id);
    }

    @Nullable
    public P getPlayer(Player player) {
        return this.getPlayer(player.getUniqueId());
    }

    public boolean hasPlayer(P player) {
        return this.hasPlayer(player.getId());
    }

    public boolean hasPlayer(Player player) {
        return this.hasPlayer(player.getUniqueId());
    }

    public boolean hasPlayer(UUID id) {
        return this.playerMap.get(id) != null;
    }

    public void addPlayer(Player player) {
        if (this.hasPlayer(player)) return;
        P gamePlayer = this.createPlayer(player);
        this.playerMap.put(gamePlayer.getId(), gamePlayer);
    }

    public void addSpectator(Player player) {
        if (this.hasPlayer(player)) return;
        P gamePlayer = this.createSpectator(player);
        this.playerMap.put(gamePlayer.getId(), gamePlayer);
    }

    public void addPlayer(P player) {
        this.playerMap.put(player.getId(), player);
    }

    public void removePlayer(Player player) {
        this.removePlayer(player.getUniqueId());
    }

    public void removePlayer(P player) {
        this.removePlayer(player.getId());
    }

    public void removePlayer(UUID id) {
        this.playerMap.remove(id);
    }

    public void removeAll() {
        this.playerMap.clear();
    }

    protected P createPlayer(Player player) {
        return this.game.createPlayer(player);
    }

    protected P createSpectator(Player player) {
        P gamePlayer = this.createPlayer(player);
        gamePlayer.setState(PlayerState.ELIMINATED);
        return gamePlayer;
    }

}
