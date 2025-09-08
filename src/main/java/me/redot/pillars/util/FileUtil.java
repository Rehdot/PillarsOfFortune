package me.redot.pillars.util;

import me.redot.pillars.Constants;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;

public class FileUtil {

    private FileUtil() {}

    public static void saveToFile(File file, Object object) throws IOException {
        try (var os = new ObjectOutputStream(new FileOutputStream(file))) {
            os.writeObject(object);
            os.flush();
        }
    }

    public static String getOrCreateFolder(File path, String folderName) {
        File folder = Paths.get(path.getAbsolutePath(), folderName).toFile();

        if (!folder.exists()) {
            folder.mkdirs();
        }

        return folder.getAbsolutePath();
    }

    public static File resolveMapFile(String mapName) {
        return Paths.get(Constants.MAP_FOLDER_PATH, mapName + ".txt").toFile();
    }

    /// This is a bad approach to solving this problem,
    /// however, it is acceptable enough for this implementation.
    public static void deleteRegionsAndEntities() {
        for (World world : Bukkit.getWorlds()) {
            File worldFolder = world.getWorldFolder();
            deleteAllInFolder(new File(worldFolder, "region"));
            deleteAllInFolder(new File(worldFolder, "entities"));
        }
    }

    public static void deleteAllInFolder(File folder) {
        File[] contents = folder.listFiles();

        if (contents == null || contents.length < 1) {
            return;
        }

        deleteFiles(contents);
    }

    public static void deleteFiles(File[] files) {
        for (File file : files) {
            if (!file.exists()) continue;
            try {
                file.delete();
            } catch (Exception e) {
                Bukkit.getLogger().info("Failed deleting file: "
                        + file.getAbsolutePath() + "\n" + e.getMessage());
            }
        }
    }

}
