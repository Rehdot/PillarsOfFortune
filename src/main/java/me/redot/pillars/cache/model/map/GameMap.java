package me.redot.pillars.cache.model.map;

import co.aikar.commands.InvalidCommandArgument;
import me.redot.pillars.util.FileUtil;
import me.redot.pillars.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/// A very bare-bones and non-ideal map loading and saving implementation
public class GameMap implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private final Map<Offset, Material> offsetMaterialMap;

    public GameMap(String name, Map<Offset, Material> offsetMaterialMap) {
        this.name = name;
        this.offsetMaterialMap = offsetMaterialMap;
    }

    public String getName() {
        return this.name;
    }

    public void load(Location origin, JavaPlugin plugin) {
        this.load(origin, plugin, (loc, mat) -> {
            loc.getBlock().setType(mat); // default load action
        });
    }

    public void load(Location origin, JavaPlugin plugin, BiConsumer<Location, Material> loadAction) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            this.offsetMaterialMap.forEach((off, material) -> {
                loadAction.accept(off.offset(origin), material);
            });
        });
    }

    public static GameMap fromMapName(String mapName) throws IOException, ClassNotFoundException {
        return fromFile(FileUtil.resolveMapFile(mapName));
    }

    public static GameMap fromFile(File file) throws IOException, ClassNotFoundException {
        try (var is = new ObjectInputStream(new FileInputStream(file))) {
            return (GameMap) is.readObject();
        }
    }

    private static GameMap fromCorners(String name, Location corner1, Location corner2) {
        BoundingBox box = BoundingBox.of(corner1, corner2);
        Map<Offset, Material> offsetMaterialMap = new HashMap<>();

        LocationUtil.forEachLocation(box, corner1.getWorld(), loc -> {
            Material type = loc.getBlock().getType();

            if (!type.equals(Material.AIR)) {
                offsetMaterialMap.put(Offset.difference(corner1, loc), type);
            }
        });

        return new GameMap(name, offsetMaterialMap);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private Location corner1;
        private Location corner2;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCorner1(Location corner1) {
            this.corner1 = corner1;
            return this;
        }

        public Builder withCorner2(Location corner2) {
            this.corner2 = corner2;
            return this;
        }

        public boolean hasName() {
            return this.name != null;
        }

        public boolean hasCorner1() {
            return this.corner1 != null;
        }

        public boolean hasCorner2() {
            return this.corner2 != null;
        }

        public boolean hasAllFields() {
            return this.hasName() && this.hasCorner1() && this.hasCorner2();
        }

        public String getName() {
            return this.name;
        }

        public Location getCorner1() {
            return this.corner1;
        }

        public Location getCorner2() {
            return this.corner2;
        }

        public GameMap build() {
            return GameMap.fromCorners(this.name, this.corner1, this.corner2);
        }

        public void saveToNamedFile() {
            File file = FileUtil.resolveMapFile(this.name);

            try {
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
            } catch (IOException e) {
                throw new InvalidCommandArgument("Failed to create new file from path: " + file.getAbsoluteFile());
            }

            try {
                FileUtil.saveToFile(file, this.build());
            } catch (IOException e) {
                throw new InvalidCommandArgument("Failed to save map to file: " + e.getMessage());
            }
        }

    }

}
