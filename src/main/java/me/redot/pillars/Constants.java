package me.redot.pillars;

import me.redot.pillars.util.FileUtil;
import org.bukkit.Bukkit;

public class Constants {

    public static final String MAP_FOLDER_PATH = FileUtil.getOrCreateFolder(Bukkit.getPluginsFolder(), "gamemaps");
    public static final String HOST_PERM = "pillars.game";
    public static final String JOIN_PERM = "pillars.join";
    public static final String MAP_PERM = "pillars.maps";

}
