package me.redot.pillars.listener;

import me.redot.pillars.PillarsPlugin;
import me.redot.pillars.cache.impl.service.LobbyService;
import me.redot.pillars.cache.model.game.BaseGame;
import me.redot.pillars.cache.model.game.GameManager;
import me.redot.pillars.util.chat.CC;
import me.redot.pillars.util.chat.TextBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PillarsGameListener implements Listener {

    private final GameManager gameManager;
    private final LobbyService lobbyService;

    public PillarsGameListener(PillarsPlugin plugin) {
        this.gameManager = plugin.getGameManager();
        this.lobbyService = plugin.getLobbyService();
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        event.setShowDeathMessages(false);
    }

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        BaseGame<?, ?> game = this.gameManager.getActiveGame();
        Player player = event.getPlayer();

        if (game == null || !game.hasPlayer(player)) {
            this.lobbyService.sendToLobby(player);
            return;
        }

        game.handleRespawn(player);
    }

    @EventHandler
    private void onPlayerDamage(EntityDamageEvent event) {
        BaseGame<?, ?> game = this.gameManager.getActiveGame();

        if (game == null || !(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.getHealth() - event.getFinalDamage() <= 0) {
            game.eliminatePlayer(player);
            player.getInventory().clear();
            game.broadcast(this.getDeathMessage(player, event));
        }
    }

    private TextBuilder getDeathMessage(Player player, EntityDamageEvent event) {
        TextBuilder builder = TextBuilder.custom(CC.YELLOW, CC.GOLD);

        if (event instanceof EntityDamageByEntityEvent edbe) {
            return builder.arg(player.getName())
                    .add(" was slain by ")
                    .arg(edbe.getDamager().getName())
                    .add(".");
        }

        return switch (event.getCause()) {
            case FALL -> builder.arg(player.getName()).add(" fell from a high place.");
            case LAVA -> builder.arg(player.getName()).add(" tried to swim in lava.");
            case FIRE, FIRE_TICK -> builder.arg(player.getName()).add(" went up in flames.");
            case VOID -> builder.arg(player.getName()).add(" fell out of the world.");
            case DROWNING -> builder.arg(player.getName()).add(" drowned.");
            default -> builder.arg(player.getName()).add(" died.");
        };
    }


}
