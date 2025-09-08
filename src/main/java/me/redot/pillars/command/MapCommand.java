package me.redot.pillars.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.redot.pillars.Constants;
import me.redot.pillars.PillarsPlugin;
import me.redot.pillars.cache.model.map.GameMap;
import me.redot.pillars.cache.model.map.MapBuilderManager;
import me.redot.pillars.util.FileUtil;
import me.redot.pillars.util.chat.TextBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;

@CommandAlias("gamemap")
@CommandPermission(Constants.MAP_PERM)
public class MapCommand extends BaseCommand {

    private final PillarsPlugin plugin;
    private final MapBuilderManager mapManager;

    public MapCommand(PillarsPlugin plugin) {
        this.plugin = plugin;
        this.mapManager = plugin.getMapManager();
    }

    @Subcommand("save")
    private void onSave(Player player) {
        GameMap.Builder builder = this.mapManager.getOrCreateBuilder(player.getUniqueId());

        if (builder.hasAllFields()) {
            builder.saveToNamedFile();
            TextBuilder.success("Successfully saved map file.")
                    .send(player);
            return;
        }

        TextBuilder textBuilder = TextBuilder.error("Failed to save map due to missing fields:");

        if (!builder.hasName()) {
            textBuilder.newLine().add("- ").arg("Map Name");
        }
        if (!builder.hasCorner1()) {
            textBuilder.newLine().add("- ").arg("Corner 1");
        }
        if (!builder.hasCorner2()) {
            textBuilder.newLine().add("- ").arg("Corner 2");
        }

        textBuilder.send(player);
    }

    @Subcommand("load")
    private void onLoad(Player player, String mapName) {
        File mapFile = FileUtil.resolveMapFile(mapName);

        if (!mapFile.exists()) {
            throw new InvalidCommandArgument("Failed resolving file! Path: " + mapFile.getAbsolutePath());
        }

        GameMap map;
        try {
            map = GameMap.fromFile(mapFile);
        } catch (Exception e) {
            throw new InvalidCommandArgument("Failed reading map file: " + e.getMessage());
        }

        map.load(player.getLocation(), this.plugin);
        TextBuilder.success("Attempting to load map...")
                .send(player);
    }

    @Subcommand("setname")
    private void onSetName(Player player, String mapName) {
        GameMap.Builder builder = this.mapManager.getOrCreateBuilder(player.getUniqueId());
        builder.withName(mapName);

        TextBuilder.success("Set your current map's name to ")
                .arg(mapName).add(".")
                .send(player);
    }

    @Subcommand("setcorner1")
    private void onSetCorner1(Player player) {
        GameMap.Builder builder = this.mapManager.getOrCreateBuilder(player.getUniqueId());
        Location loc = player.getLocation();

        builder.withCorner1(player.getLocation().clone().subtract(0, 1, 0));
        TextBuilder.success("Set corner 1 to ")
                .arg(loc.getBlockX() + ", " + (loc.getBlockY() - 1) + ", " + loc.getBlockZ())
                .add(".")
                .send(player);
    }

    @Subcommand("setcorner2")
    private void onSetCorner2(Player player) {
        GameMap.Builder builder = this.mapManager.getOrCreateBuilder(player.getUniqueId());
        Location loc = player.getLocation();

        builder.withCorner2(player.getLocation().clone().subtract(0, 1, 0));
        TextBuilder.success("Set corner 2 to ")
                .arg(loc.getBlockX() + ", " + (loc.getBlockY() - 1) + ", " + loc.getBlockZ())
                .add(".")
                .send(player);
    }

}
