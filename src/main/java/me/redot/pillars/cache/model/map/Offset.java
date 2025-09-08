package me.redot.pillars.cache.model.map;

import org.bukkit.Location;

import java.io.Serial;
import java.io.Serializable;

public class Offset implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int x, y, z;

    private Offset(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /// @return Location found by offsetting the origin by this
    public Location offset(Location origin) {
        return origin.clone().add(this.x, this.y, this.z);
    }

    public static Offset difference(Location from, Location to) {
        Location diff = to.clone().subtract(from);
        return new Offset(diff.getBlockX(), diff.getBlockY(), diff.getBlockZ());
    }

}
