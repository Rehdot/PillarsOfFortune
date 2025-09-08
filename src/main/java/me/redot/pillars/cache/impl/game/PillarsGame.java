package me.redot.pillars.cache.impl.game;

import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import me.redot.pillars.PillarsPlugin;
import me.redot.pillars.cache.impl.service.LobbyService;
import me.redot.pillars.cache.model.game.BaseGame;
import me.redot.pillars.cache.model.map.GameMap;
import me.redot.pillars.cache.model.player.PlayerState;
import me.redot.pillars.cache.model.time.Timeline;
import me.redot.pillars.util.CollectionUtil;
import me.redot.pillars.util.LocationUtil;
import me.redot.pillars.util.chat.CC;
import me.redot.pillars.util.chat.TextBuilder;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class PillarsGame extends BaseGame<PillarsGame, PillarsPlayer> {

    private final Location origin;
    private final Set<Material> materials;
    private Location center, chestLocation;
    private final LobbyService lobbyService;
    private final Set<Enchantment> enchantments;

    public PillarsGame(PillarsPlugin plugin, GameMap map) {
        super(plugin, map);
        this.materials = this.getAllowedMaterials();
        this.enchantments = this.getAllEnchantments();
        this.origin = this.originService.nextMapOrigin();
        this.lobbyService = this.plugin.getLobbyService();
    }

    @Override
    protected String getJoinCommand() {
        return "/pillars join";
    }

    @Override
    public void handleRespawn(Player player) {
        if (!this.hasPlayer(player)) return;
        PillarsPlayer pillarsPlayer = this.playerComponent.getPlayer(player);
        pillarsPlayer.teleport(pillarsPlayer.getSpawnLocation());
    }

    @Override
    public String getName() {
        return "Pillars of Fortune";
    }

    @Override
    public void begin() {
        if (this.playerComponent.alivePlayerCount() < 2) {
            TextBuilder.error("The ")
                    .arg(this.getName())
                    .add(" game was cancelled due to a lack of players.")
                    .tap(this::broadcast);
            super.stop();
            return;
        }

        String line = "---------------------------------------";
        TextBuilder.text(line, CC.GRAY).newLine()
                .add("                 Pillars of Fortune", CC.B_YELLOW).newLine().newLine()
                .add("    You are spawned on pillars and given random", CC.LIME).newLine()
                .add("     items every few seconds. Be the last person", CC.LIME).newLine()
                .add("                  standing to win!", CC.LIME).newLine().newLine()
                .add(line, CC.GRAY).tap(this::broadcast);

        super.begin();
        this.teleportAllToMap();
        this.recursivelyGiveItems();
        this.registerTickCheck(this::checkForWinner);
    }

    @Override
    public void eliminatePlayer(PillarsPlayer player) {
        String line = "       ------------------------------------------";
        TextBuilder.text(line, CC.GRAY).newLine()
                .add("      Thank you for playing " + this.getName(), CC.B_YELLOW).newLine()
                .add(line, CC.GRAY)
                .send(player);
        super.eliminatePlayer(player);
    }

    @Override
    public void fixPlayerGameStates() {
        this.playerComponent.forEach(gamePlayer -> {
            Player player = gamePlayer.getPlayer();

            player.setRespawnLocation(gamePlayer.getSpawnLocation());
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
        });
    }

    @Override
    public void stop() {
        this.lobbyService.sendAllToLobby(this.playerComponent);
        super.stop();
    }

    @Override
    public PillarsPlayer createPlayer(Player player) {
        return new PillarsPlayer(player, this);
    }

    @Override
    public int maxPlayers() {
        return 8;
    }

    @Override
    public void checkForWinner() {
        if (!this.active) return;

        int alive = this.playerComponent.alivePlayerCount();

        if (alive > 1) {
            return;
        }

        TextBuilder builder;
        this.active = false;

        if (alive == 0) {
            builder = TextBuilder.error().arg("Nobody").add(" won the ")
                    .arg(getName())
                    .add(" game!");
        } else {
            PillarsPlayer winner = this.playerComponent.firstWithState(PlayerState.ALIVE);
            builder = TextBuilder.success().arg(winner.getName()) .add(" has won the ")
                    .arg(getName())
                    .add(" game!");
        }

        this.broadcast(builder);
        this.runTaskSecsLater(this::stop, 15); // winner does a victory lap
    }

    @Override
    protected Timeline getStartupTimeline() {
        Timeline startupTimeline = new Timeline(this.plugin);

        startupTimeline.addTimestamp("Load Map", 20, this::loadMap);
        startupTimeline.addTimestamp("Game Beginning", this.startupSeconds(), this::begin);
        startupTimeline.addTimestamp("Countdown", 27, () -> {
            this.broadcast(TextBuilder.text().arg(this.getName())
                    .add(" will start in ")
                    .arg("3").add(" seconds!"));
            this.runTaskSecsLater(() -> {
                this.broadcast(TextBuilder.text().arg(this.getName())
                        .add(" will start in ")
                        .arg("2").add(" seconds!"));
            }, 1);
            this.runTaskSecsLater(() -> {
                this.broadcast(TextBuilder.text().arg(this.getName())
                        .add(" will start in ")
                        .arg("1").add(" second!"));
            }, 2);
        });

        return startupTimeline;
    }

    private void recursivelyGiveItems() {
        if (!this.active) return;
        this.distributeRandomItems();
        this.runTaskSecsLater(this::recursivelyGiveItems, 3);
    }

    private Set<Material> getAllowedMaterials() {
        Set<Material> materialSet = Sets.newHashSet(Material.values());

        materialSet.removeIf(material ->
                material == null ||
                material.isLegacy() ||
                material.isAir() ||
                !material.isItem()
        );

        return materialSet;
    }

    private Set<Enchantment> getAllEnchantments() {
        return Sets.newHashSet(Enchantment.values());
    }

    private void distributeRandomItems() {
        this.playerComponent.forEachAlive(this::giveRandomItem);
        this.giveRandomChestItem();
    }

    private void giveRandomChestItem() {
        if (this.chestLocation.getBlock().getState() instanceof Chest chest) {
            chest.getSnapshotInventory().addItem(this.getRandomEnchantedItem());
            chest.update(true);
        }
    }

    private void giveRandomItem(PillarsPlayer player) {
        player.getPlayer().getInventory().addItem(this.getRandomItem());
    }

    private ItemStack getRandomEnchantedItem() {
        Enchantment enchantment = CollectionUtil.randomElement(this.enchantments);
        int rand = ThreadLocalRandom.current().nextInt(1, 4);
        ItemStack random = getRandomItem();

        random.addUnsafeEnchantment(enchantment, rand);
        return random;
    }

    private ItemStack getRandomItem() {
        return ItemStack.of(CollectionUtil.randomElement(this.materials));
    }

    private void teleportAllToMap() {
        this.playerComponent.forEachAlive(this::teleportToMap);
    }

    private void teleportToMap(PillarsPlayer player) {
        Location spawnLoc = player.getSpawnLocation().clone();
        Location focalPoint = this.center.clone();
        Vector direction = focalPoint.toVector().subtract(spawnLoc.toVector());

        spawnLoc.setDirection(direction);
        player.teleport(spawnLoc);
    }

    private void loadMap() {
        Queue<PillarsPlayer> playerQueue = Queues.newArrayDeque(this.playerComponent.getPlayers());

        this.map.load(this.origin, this.plugin, (loc, mat) -> {
            Block block = loc.getBlock();

            if (mat.equals(Material.SPONGE)) {
                PillarsPlayer player = playerQueue.poll();
                block.setType(Material.AIR);

                if (player != null) {
                    player.setSpawnLocation(LocationUtil.centerBlock(loc));
                }
                return;
            }

            block.setType(mat);
            BlockState state = block.getState();

            if (state instanceof Chest) {
                this.center = loc.clone().add(0, 35, 0);
                this.chestLocation = loc;
            }
        });
    }

}
