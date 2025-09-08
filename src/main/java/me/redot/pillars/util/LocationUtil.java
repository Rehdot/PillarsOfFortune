package me.redot.pillars.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

import java.util.function.Consumer;

public class LocationUtil {

    private LocationUtil() {}

    public static Location centerBlock(Location loc) {
        return loc.toCenterLocation().subtract(0, 0.5, 0);
    }

    public static void forEachLocation(BoundingBox box, World world, Consumer<Location> consumer) {
        int minX = (int) Math.floor(box.getMinX());
        int minY = (int) Math.floor(box.getMinY());
        int minZ = (int) Math.floor(box.getMinZ());

        int maxX = (int) Math.floor(box.getMaxX());
        int maxY = (int) Math.floor(box.getMaxY());
        int maxZ = (int) Math.floor(box.getMaxZ());

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    consumer.accept(new Location(world, x, y, z));
                }
            }
        }
    }

}
