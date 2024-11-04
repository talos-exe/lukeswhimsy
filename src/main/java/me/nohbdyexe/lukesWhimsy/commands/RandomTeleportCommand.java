package me.nohbdyexe.lukesWhimsy.commands;
import me.nohbdyexe.lukesWhimsy.LukesWhimsy;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RandomTeleportCommand implements CommandExecutor {

    private final LukesWhimsy plugin;
    private String PLUGIN_PREFIX;
    private final HashMap<UUID, Location> lastLocations;

    public RandomTeleportCommand(LukesWhimsy plugin) {
        this.plugin = plugin;
        this.lastLocations = plugin.getRtpLastLocation();
        this.PLUGIN_PREFIX = plugin.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLUGIN_PREFIX+"You must be a player to use that command.");
            return true;
        }
        Player player = (Player) sender;

        // Debugging to remove map if player is already found.
        if (lastLocations.containsKey(player.getUniqueId())) {
            lastLocations.remove(player.getUniqueId());
        }

        // Initialize initial location
        Location initialLocation = player.getLocation();
        lastLocations.put(player.getUniqueId(), player.getLocation());

        player.sendMessage(PLUGIN_PREFIX + "Teleporting in 3 seconds. Do not move.");

        // Start timer countdown
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            int countdown = 3;

            @Override
            public void run() {
                if (countdown > 0) {
                    if (!player.getLocation().equals(initialLocation)) {
                        player.sendMessage(PLUGIN_PREFIX + "You moved. Teleport cancelled.");
                        lastLocations.remove(player.getUniqueId(), initialLocation);
                        return; // Cancel teleport if the player moves
                    }
                    player.sendMessage(PLUGIN_PREFIX + "Teleporting in " + countdown + "...");
                    countdown--;
                    Bukkit.getScheduler().runTaskLater(plugin, this, 20);
                } else {
                    teleportPlayer(player);
                }
            }
        }, 20); // Start the countdown after 20 ticks ( 1 second )

        return true;
    }

    private void teleportPlayer(Player player) {
        // Get the world border size
        int borderSize = (int) player.getWorld().getWorldBorder().getSize() / 2;
        // Generate random coordinates within the world border
        Random random = new Random();
        // Create a new location
        Location randomLocation = null;

        // Attempts to find safe locations
        for (int attempts = 0; attempts < 10; attempts++) {
            int x = random.nextInt(borderSize * 2) - borderSize; // Get a random X coordinate
            int z = random.nextInt(borderSize * 2) - borderSize; // Get a random Z coordinate
            int y = player.getWorld().getHighestBlockYAt(x, z); // Get the highest Y value for the new location.

            // Check if location is safe.
            randomLocation = new Location(player.getWorld(), x, y, z);
            Block block = randomLocation.getBlock();

            //Make sure we're not on water, lava, solid ground.
            if (block.getType().isSolid() && block.getType() != Material.WATER && block.getType() != Material.LAVA) {
                //Check for air block
                if (block.getRelative(0, 1, 0).getType() == Material.AIR) {
                    // Check if surrounding area is clear.
                    if (isAreaClear(randomLocation)) {
                        break; // Found safe location.
                    }
                }
            }
        }

        if (randomLocation == null) {
            //Wasn't able to find a safe location within 10 attempts.
            player.sendMessage(PLUGIN_PREFIX + "Could not find a safe location to rtp to within 10 tries.");
            return;
        }

        // Preload the chunk where the player will be teleported
        Chunk chunk = randomLocation.getChunk();
        player.getWorld().loadChunk(chunk);

        // Load surrounding chunks
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                player.getWorld().loadChunk(chunk.getX() + dx, chunk.getZ() + dz);
            }
        }

        // Teleport the player
        player.teleport(randomLocation);
        player.sendMessage(PLUGIN_PREFIX + "You have teleported to location: " + randomLocation.getX() + ", " + randomLocation.getY() + ", " + randomLocation.getZ());

    }

    private boolean isAreaClear(Location location) {
        // Check the blocks around the player.
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 2; y++) { // Check the block the player is standing on and the two above.
                for (int z = -1; z <= 1; z++ ){
                    Block block = location.clone().add(x,y,z).getBlock();
                    if (block.getType().isSolid()) {
                        return false; // Block found, area not clear.
                    }
                }
            }
        }
        return true; // Block found, area clear.
    }

}
