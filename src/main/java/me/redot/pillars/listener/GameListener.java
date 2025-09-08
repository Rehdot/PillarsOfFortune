package me.redot.pillars.listener;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import me.redot.pillars.PillarsPlugin;
import me.redot.pillars.cache.impl.service.LobbyService;
import me.redot.pillars.cache.model.game.BaseGame;
import me.redot.pillars.cache.model.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener {

    private final GameManager gameManager;
    private final LobbyService lobbyService;

    public GameListener(PillarsPlugin plugin) {
        this.gameManager = plugin.getGameManager();
        this.lobbyService = plugin.getLobbyService();
    }

    @EventHandler
    private void onPlayerAttack(PrePlayerAttackEntityEvent event) {
        BaseGame<?, ?> game = this.gameManager.getActiveGame();

        if (game == null || !game.isRunning() || !game.hasPlayer(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onFoodLevelChange(FoodLevelChangeEvent event) {
        BaseGame<?, ?> game = this.gameManager.getActiveGame();

        if (game == null || !game.isRunning() || !game.hasPlayer(event.getEntity().getUniqueId())) {
            event.setCancelled(true); // no starving in the lobby allowed
        }
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        BaseGame<?, ?> game = this.gameManager.getActiveGame();
        Player player = event.getPlayer();

        if (game != null && game.hasPlayer(player)) {
            game.addSpectator(player);
            return;
        }

        this.lobbyService.sendToLobby(player);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        if (!this.gameManager.hasGame()) return;

        BaseGame<?, ?> game = this.gameManager.getActiveGame();
        Player player = event.getPlayer();

        if (game.hasPlayer(player)) {
            game.eliminatePlayer(player);
        }
    }

}
