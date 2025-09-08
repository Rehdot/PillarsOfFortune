package me.redot.pillars.cache.model.time;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Timeline {

    private long tick;
    private final JavaPlugin plugin;
    private final BukkitRunnable runnable;
    private final List<Runnable> tickChecks;
    private final Map<Integer, Timestamp> timeStamps;

    public Timeline(JavaPlugin plugin) {
        this.tick = 0;
        this.plugin = plugin;
        this.timeStamps = new HashMap<>();
        this.runnable = this.newRunnable();
        this.tickChecks = new ArrayList<>();
    }

    public void start() {
        this.runnable.runTaskTimer(this.plugin, 0L, 1L);
    }

    public void addTickCheck(Runnable action) {
        this.tickChecks.add(action);
    }

    public void addTimestamp(String title, int seconds, Runnable... actions) {
        Timestamp stamp = this.timeStamps.get(seconds);

        if (stamp == null) {
            stamp = new Timestamp(title, seconds);
        }

        for (Runnable action : actions) {
            stamp.addAction(action);
        }

        this.timeStamps.put(seconds, stamp);
    }

    public void clearTimestamps() {
        this.timeStamps.clear();
    }

    public void removeTimestamp(int seconds) {
        this.timeStamps.remove(seconds);
    }

    public int getSeconds() {
        return (int)(this.tick / 20L);
    }

    protected void secondCheck() {
        Timestamp stamp = this.timeStamps.get(this.getSeconds());

        if (stamp != null) {
            stamp.run();
        }
    }

    protected BukkitRunnable newRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                Timeline.this.tick++;
                for (Runnable tickCheck : Timeline.this.tickChecks) {
                    tickCheck.run();
                }
                if (Timeline.this.tick % 20 == 0) {
                    Timeline.this.secondCheck();
                }
            }
        };
    }

    public static int secsFromMins(int mins) {
        return (mins * 60);
    }

    public static int secsFromMins(int mins, int seconds) {
        return (mins * 60) + seconds;
    }

}
