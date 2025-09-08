package me.redot.pillars.cache.model.time;

import java.util.ArrayList;
import java.util.List;

public class Timestamp {

    private boolean expired;
    private final int second;
    private final String title;
    private final List<Runnable> actions;

    public Timestamp(String title, int seconds) {
        this.title = title;
        this.expired = false;
        this.second = seconds;
        this.actions = new ArrayList<>();
    }

    public void addAction(Runnable runnable) {
        this.actions.add(runnable);
    }

    public void run() {
        if (this.expired) return;
        this.actions.forEach(Runnable::run);
        this.expired = true;
    }

    public boolean isExpired() {
        return this.expired;
    }

    public int getSecond() {
        return this.second;
    }

    public String getTitle() {
        return this.title;
    }

}
