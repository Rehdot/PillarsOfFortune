package me.redot.pillars.cache.model.time;

import me.redot.pillars.cache.model.game.BaseGame;

public class TimeComponent {

    private final Timeline timeline;

    public TimeComponent(BaseGame<?, ?> game) {
        this.timeline = new Timeline(game.getPlugin());
    }

    public void start() {
        this.timeline.start();
    }

    public Timeline getTimeline() {
        return this.timeline;
    }

    public void registerTickCheck(Runnable action) {
        this.timeline.addTickCheck(action);
    }

    public void registerTimestamp(String title, int seconds, Runnable... actions) {
        this.timeline.addTimestamp(title, seconds, actions);
    }

    public void registerTimestamp(int seconds, Runnable... actions) {
        this.timeline.addTimestamp("Untitled", seconds, actions);
    }

    public void unregisterTimestamp(int seconds) {
        this.timeline.removeTimestamp(seconds);
    }

    public void unregisterAll() {
        this.timeline.clearTimestamps();
    }

}
