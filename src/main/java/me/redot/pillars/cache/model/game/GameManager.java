package me.redot.pillars.cache.model.game;

import me.redot.pillars.PillarsPlugin;

public class GameManager {

    private final PillarsPlugin plugin;
    private BaseGame<?, ?> activeGame;

    public GameManager(PillarsPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean hasGame() {
        return this.activeGame != null;
    }

    public BaseGame<?, ?> getActiveGame() {
        return this.activeGame;
    }

    public void setActiveGame(BaseGame<?, ?> game) {
        this.activeGame = game;
    }

    public void stopGame() {
        if (!this.hasGame()) return;
        this.activeGame.stop();
        this.activeGame = null;
    }

}
