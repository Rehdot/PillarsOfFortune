package me.redot.pillars.cache.model.game;

import me.redot.pillars.PillarsPlugin;
import me.redot.pillars.cache.impl.service.OriginService;
import me.redot.pillars.cache.model.map.GameMap;
import me.redot.pillars.cache.model.player.GamePlayer;
import me.redot.pillars.cache.model.player.PlayerComponent;
import me.redot.pillars.cache.model.player.PlayerState;
import me.redot.pillars.cache.model.time.TimeComponent;
import me.redot.pillars.cache.model.time.Timeline;
import me.redot.pillars.util.chat.TextBuilder;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public abstract class BaseGame<G extends Game<P>, P extends GamePlayer<G>> implements Game<P> {

    protected boolean active;
    protected final GameMap map;
    protected final PillarsPlugin plugin;
    protected final OriginService originService;
    protected final TimeComponent timeComponent;
    protected final PlayerComponent<P, G> playerComponent;

    public BaseGame(PillarsPlugin plugin, GameMap map) {
        this.map = map;
        this.active = false;
        this.plugin = plugin;
        this.timeComponent = new TimeComponent(this);
        this.playerComponent = new PlayerComponent<>(this);
        this.originService = this.plugin.getOriginService();
        this.startup();
    }

    protected abstract String getJoinCommand();

    protected abstract void checkForWinner();

    public abstract void handleRespawn(Player player);

    @Override
    public int startupSeconds() {
        return 30;
    }

    @Override
    public int timeoutSeconds() {
        return Timeline.secsFromMins(5);
    }

    @Override
    public void begin() {
        this.fixPlayerGameStates();
        this.timeComponent.start();
        this.active = true;
    }

    @Override
    public void stop() {
        this.active = false;
        this.timeComponent.unregisterAll();
        this.playerComponent.removeAll();
        this.plugin.getGameManager().setActiveGame(null);
    }

    @Override
    public void addPlayer(Player player) {
        this.playerComponent.addPlayer(player);
    }

    @Override
    public boolean hasPlayer(Player player) {
        return this.playerComponent.hasPlayer(player);
    }

    @Override
    public void startup() {
        this.announce();
        this.defineTimeoutStamp();
        this.getStartupTimeline().start();
    }

    public void announce() {
        TextBuilder clickText = TextBuilder.success().arg("-> ").add("Click here to join!")
                .clickAction(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, this.getJoinCommand()))
                .hoverText(TextBuilder.text("Click to join!").build());
        TextBuilder text = TextBuilder.text("A ")
                .arg(this.getName())
                .add(" game is starting in ")
                .arg(this.startupSeconds()+"")
                .add(" seconds!").newLine()
                .append(clickText);
        Bukkit.getOnlinePlayers().forEach(text::send);
    }

    public boolean isRunning() {
        return this.active;
    }

    public boolean isFull() {
        return this.playerComponent.countPlayers() >= this.maxPlayers();
    }

    public void eliminatePlayer(Player player) {
        if (!this.hasPlayer(player)) return;
        this.eliminatePlayer(this.playerComponent.getPlayer(player));
    }

    public void eliminatePlayer(P gamePlayer) {
        Player player = gamePlayer.getPlayer();

        player.setGameMode(GameMode.SPECTATOR);
        gamePlayer.setState(PlayerState.ELIMINATED);
    }

    public void addSpectator(Player player) {
        P other = this.playerComponent.firstWithState(PlayerState.ALIVE);
        this.playerComponent.addSpectator(player);
        player.setGameMode(GameMode.SPECTATOR);
        player.teleportAsync(other.getLocation());
    }

    public boolean hasPlayer(UUID id) {
        return this.playerComponent.hasPlayer(id);
    }

    public boolean hasPlayer(P player) {
        return this.playerComponent.hasPlayer(player);
    }

    /// This method can be overridden for custom starting logic.
    /// Otherwise, it just works automatically.
    protected Timeline getStartupTimeline() {
        Timeline startupTimeline = new Timeline(this.getPlugin());
        startupTimeline.addTimestamp("Game Beginning", this.startupSeconds(), this::begin);
        return startupTimeline;
    }

    protected void defineTimeoutStamp() {
        this.registerTimestamp("Timeout", this.timeoutSeconds(), () -> {
            TextBuilder.warning("This game has timed out and is now ending.")
                    .tap(this::broadcast);
            this.stop();
        });
    }

    public int secondsUntilBegin() {
        return this.startupSeconds() - this.timeComponent.getTimeline().getSeconds();
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public void registerTickCheck(Runnable action) {
        this.timeComponent.registerTickCheck(action);
    }

    public void registerTimestamp(int seconds, Runnable... actions) {
        this.timeComponent.registerTimestamp(seconds, actions);
    }

    public void registerTimestamp(String name, int seconds, Runnable... actions) {
        this.timeComponent.registerTimestamp(name, seconds, actions);
    }

    public void runTaskLater(Runnable task, long ticks) {
        Bukkit.getScheduler().runTaskLater(this.plugin, task, ticks);
    }

    public void runTaskSecsLater(Runnable task, long seconds) {
        this.runTaskLater(task, seconds * 20);
    }

    public void broadcast(TextBuilder text) {
        this.playerComponent.forEach(text::send);
    }

    protected void broadcastSound(Sound sound) {
        this.playerComponent.forEach(player -> this.sendSound(sound, player));
    }

    protected void sendSound(Sound sound, P gamePlayer) {
        Player player = gamePlayer.getPlayer();
        player.playSound(player.getLocation(), sound, 1f, 1f);
    }

}
