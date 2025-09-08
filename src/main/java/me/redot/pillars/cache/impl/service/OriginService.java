package me.redot.pillars.cache.impl.service;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/// Maps are deleted upon server shutdown via deleting the entire world
/// The OriginService allows us to generate new locations to load and play maps
public class OriginService {

    private int x;
    private int y;
    private int z;
    private final int step;
    private World world;

    public OriginService(int x, int z) {
        this(x, 100, z, 5_000, Bukkit.getWorlds().getFirst());
    }

    /// @param x starting x coordinate
    /// @param z starting z coordinate
    /// @param step how far to space each new origin out
    public OriginService(int x, int y, int z, int step, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.step = step;
        this.world = world;
    }

    /// @apiNote This method generates map origin points in a
    /// staircase-like pattern. This is useful for making sure
    /// we don't ever intrude upon the lobby map at (0, 100, 0)
    public Location nextMapOrigin() {
        if (this.x > this.z) {
            this.z += this.step;
        } else {
            this.x += this.step;
        }
        return new Location(this.world, this.x, this.y, this.z);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public World getWorld() {
        return this.world;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setWorld(World world) {
        this.world = world;
    }

}
