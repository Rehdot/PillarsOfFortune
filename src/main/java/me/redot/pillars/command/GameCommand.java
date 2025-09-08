package me.redot.pillars.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.redot.pillars.Constants;
import me.redot.pillars.PillarsPlugin;
import me.redot.pillars.cache.impl.game.PillarsGame;
import me.redot.pillars.cache.model.game.BaseGame;
import me.redot.pillars.cache.model.game.GameManager;
import me.redot.pillars.cache.model.map.GameMap;
import me.redot.pillars.util.chat.TextBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("pillars")
public class GameCommand extends BaseCommand {

    private final PillarsPlugin plugin;
    private final GameManager gameManager;

    public GameCommand(PillarsPlugin plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
    }

    @Subcommand("host")
    @CommandPermission(Constants.HOST_PERM)
    private void onStart(CommandSender sender) {
        if (this.gameManager.hasGame()) {
            TextBuilder.error("There is already an active game!")
                    .send(sender);
            return;
        }

        TextBuilder.success("Starting a Pillars of Fortune game...")
                .send(sender);
        try {
            GameMap pillarsMap = GameMap.fromMapName("pillars");
            PillarsGame game = new PillarsGame(this.plugin, pillarsMap);
            this.gameManager.setActiveGame(game);
        } catch (Exception e) {
            throw new InvalidCommandArgument("Failed to begin game: " + e.getMessage());
        }
    }

    @Subcommand("stop")
    @CommandPermission(Constants.HOST_PERM)
    private void onStop(CommandSender sender) {
        if (!this.gameManager.hasGame()) {
            TextBuilder.error("There is no active game!")
                    .send(sender);
            return;
        }

        TextBuilder.warning("Stopping active game...")
                .send(sender);
        this.gameManager.stopGame();
    }

    @Subcommand("join")
    @CommandPermission(Constants.JOIN_PERM)
    private void onJoin(Player player) {
        if (!this.gameManager.hasGame()) {
            TextBuilder.error("There is no active game!")
                    .send(player);
            return;
        }

        BaseGame<?, ?> game = this.gameManager.getActiveGame();
        TextBuilder text = TextBuilder.success();

        if (!game.isRunning()) {
            game.addPlayer(player);
            text.add("You've joined the ")
                    .arg(game.getName())
                    .add(" game.");
        } else {
            game.addSpectator(player);
            text.add("You're now spectating the ")
                    .arg(game.getName())
                    .add(" game.");
        }

        text.send(player);
    }

    @Subcommand("spectate")
    @CommandPermission(Constants.JOIN_PERM)
    private void onSpectate(Player player) {
        if (!this.gameManager.hasGame()) {
            TextBuilder.error("There is no active game!")
                    .send(player);
            return;
        }

        BaseGame<?, ?> game = this.gameManager.getActiveGame();
        TextBuilder text = TextBuilder.success();

        game.addSpectator(player);
        text.add("You're now spectating the ")
                .arg(game.getName())
                .add(" game.");
    }

}
