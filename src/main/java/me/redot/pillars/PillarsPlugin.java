package me.redot.pillars;

import co.aikar.commands.PaperCommandManager;
import me.redot.pillars.cache.impl.service.LobbyService;
import me.redot.pillars.cache.impl.service.OriginService;
import me.redot.pillars.cache.model.game.GameManager;
import me.redot.pillars.cache.model.map.MapBuilderManager;
import me.redot.pillars.command.GameCommand;
import me.redot.pillars.command.MapCommand;
import me.redot.pillars.listener.GameListener;
import me.redot.pillars.listener.PillarsGameListener;
import me.redot.pillars.util.FileUtil;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PillarsPlugin extends JavaPlugin {

    private GameManager gameManager;
    private LobbyService lobbyService;
    private PluginManager pluginManager;
    private OriginService originService;
    private MapBuilderManager mapManager;
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        this.getLogger().info("Registering services...");
        this.commandManager = new PaperCommandManager(this);
        this.pluginManager = this.getServer().getPluginManager();
        this.mapManager = new MapBuilderManager();
        this.originService = new OriginService(0, 0);
        this.lobbyService = new LobbyService(this);

        this.getLogger().info("Initializing GameManager...");
        this.gameManager = new GameManager(this);

        this.getLogger().info("Registering commands...");
        this.commandManager.registerCommand(new MapCommand(this));
        this.commandManager.registerCommand(new GameCommand(this));

        this.getLogger().info("Registering listeners...");
        this.pluginManager.registerEvents(new PillarsGameListener(this), this);
        this.pluginManager.registerEvents(new GameListener(this), this);

        this.getLogger().info("Attempting to load lobby map...");
        this.lobbyService.loadMap();
    }

    @Override
    public void onDisable() {
        this.commandManager.unregisterCommands();
        this.gameManager.stopGame();
        FileUtil.deleteRegionsAndEntities();
    }

    public OriginService getOriginService() {
        return this.originService;
    }

    public LobbyService getLobbyService() {
        return this.lobbyService;
    }

    public MapBuilderManager getMapManager() {
        return this.mapManager;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

}
