package me.redot.pillars.cache.model.game;

import me.redot.pillars.cache.model.player.GamePlayer;
import org.bukkit.entity.Player;

public interface Game<T extends GamePlayer<?>> {

    /// @return An instance of GamePlayer that the game is using.
    T createPlayer(Player player);

    /// @return The maximum players this game can host.
    int maxPlayers();

    /// Creates and adds a GamePlayer instance to the PlayerComponent.
    void addPlayer(Player player);

    /// @return If the game has a certain player.
    boolean hasPlayer(Player player);

    /// The name of the game.
    String getName();

    /// The amount of time in seconds to wait before starting.
    int startupSeconds();

    /// The maximum amount of time in seconds the game can last.
    int timeoutSeconds();

    /// Adjusts every player's state to fit the context of the game.
    void fixPlayerGameStates();

    /// Runs as soon as the Game is instantiated.
    /// Should be used for registering when the game should begin, etc.
    void startup();

    /// Executes all beginning game logic.
    void begin();

    /// Stops the game immediately.
    void stop();

}
