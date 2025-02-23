package me.nohbdyexe.lukesWhimsy.commands;
import me.nohbdyexe.lukesWhimsy.DataManager;
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

public class RandomTeleportCommand implements CommandExecutor {

    private final LukesWhimsy plugin;
    private String PLUGIN_PREFIX;
    private final HashMap<UUID, Location> lastLocations;
    private final DataManager dataManager;

    public RandomTeleportCommand(LukesWhimsy plugin) {
        this.plugin = plugin;
        this.dataManager = new DataManager(plugin);
        this.lastLocations = dataManager.getRtpLastLocation();
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLUGIN_PREFIX+"You must be a player to use that command.");
            return true;
        }
        Player player = (Player) sender;

        // Debugging to remove map if player is already found.
//        if (lastLocations.containsKey(player.getUniqueId())) {
//            lastLocations.remove(player.getUniqueId());
//        }

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
                    teleportPlayerAsync(player);
                }
            }
        }, 20); // Start the countdown after 20 ticks ( 1 second )

        return true;
    }

    private void teleportPlayerAsync(Player player) {
        // Run the teleport process asynchronously as to not lag the server.
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                // Get the world border size
                int borderSize = (int) player.getWorld().getWorldBorder().getSize() / 2;
                // Generate random coordinates within the world border
                Random random = new Random();
                Location randomLocation = null;

                // Attempts to find safe locations asynchronously
                for (int attempts = 0; attempts < 10; attempts++) {
                    int x = random.nextInt(borderSize * 2) - borderSize; // Get a random X coordinate
                    int z = random.nextInt(borderSize * 2) - borderSize; // Get a random Z coordinate
                    int y = player.getWorld().getHighestBlockYAt(x, z); // Get the highest Y value for the new location.

                    // Check if location is safe.
                    randomLocation = new Location(player.getWorld(), x, y, z);
                    Block block = randomLocation.getBlock();

                    // Make sure we're not on water, lava, or solid ground.
                    if (block.getType().isSolid() && block.getType() != Material.WATER && block.getType() != Material.LAVA) {
                        // Check for air block above
                        if (block.getRelative(0, 1, 0).getType() == Material.AIR) {
                            // Check if surrounding area is clear
                            if (isAreaClear(randomLocation)) {
                                break; // Found safe location
                            }
                        }
                    }
                }

                if (randomLocation == null) {
                    // Wasn't able to find a safe location within 10 attempts
                    player.sendMessage(PLUGIN_PREFIX + "Could not find a safe location to rtp to within 10 tries.");
                    return;
                }

                // Preload the chunk where the player will be teleported asynchronously
                final Chunk chunk = randomLocation.getChunk();
                Bukkit.getScheduler().runTask(plugin, () -> {
                    player.getWorld().loadChunk(chunk);
                    // Load surrounding chunks synchronously
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            final int offsetX = chunk.getX() + dx;
                            final int offsetZ = chunk.getZ() + dz;
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                player.getWorld().loadChunk(offsetX, offsetZ);
                            });
                        }
                    }
                });

                // Teleport the player to the random location (synchronous)
                Location finalRandomLocation = randomLocation;
                Bukkit.getScheduler().runTask(plugin, () -> {
                    player.teleport(finalRandomLocation);
                    player.sendMessage(PLUGIN_PREFIX + "You have teleported to location: " + finalRandomLocation.getX() + ", " + finalRandomLocation.getY() + ", " + finalRandomLocation.getZ());
                });
            }
        });
    }

    private boolean isAreaClear(Location location) {

        //Check if player is not underwater
        if (!isNotUnderwater(location)) {
            return false;
        }


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

    private boolean isNotUnderwater(Location location) {
        int r = 1;
        int waterLevel = location.getBlockY();

        // Check blocks around player.
        for (int y = waterLevel - 1; y < waterLevel + 1; y++) {
            for (int x = -r; x<= r; x++) {
                for (int z = -r; z <= r; z++) {
                    Block block = location.getWorld().getBlockAt(location.getBlockX()+x, y, location.getBlockZ()+z);
                    if (block.getType() == Material.WATER) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
