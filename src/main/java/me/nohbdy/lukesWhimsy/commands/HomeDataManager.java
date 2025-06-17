package me.nohbdy.lukesWhimsy.commands;

import me.nohbdy.lukesWhimsy.DataManager;
import me.nohbdy.lukesWhimsy.LukesWhimsy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HomeDataManager{

    private final LukesWhimsy plugin;
    private final HashMap<UUID, Map<String, Location>> homeLocations;
    private final DataManager dataManager;
    private File homesFile;
    private FileConfiguration config;

    public HomeDataManager(LukesWhimsy plugin) {
        this.plugin = plugin;
        this.dataManager = new DataManager(plugin);
        this.homeLocations = dataManager.getHomesLocations();
    }

    // Load homes.yml
    public void loadHomes() {
        homesFile = new File(plugin.getDataFolder(), "homes.yml");

        if (!homesFile.exists()) {
            // Create the file if it doesn't exist
            plugin.saveResource("homes.yml",false);
        }

        config = YamlConfiguration.loadConfiguration(homesFile);

        // Ensure the "homes" section exists in the configuration
        if (!config.contains("homes")) {
            config.createSection("homes");  // Create the "homes" section if it doesn't exist
            saveHomes(); // Save the file with the "homes" section
        }

            for (String uuidStr : config.getConfigurationSection("homes").getKeys(false)) {
                UUID uuid = UUID.fromString(uuidStr);
                Map<String,Location> playerHomes = new HashMap<>();

                // Loop through the homes of each player
                for (String homeName : config.getConfigurationSection("homes." + uuidStr).getKeys(false)) {
                    double x = config.getDouble("homes." + uuidStr + "." + homeName + ".x");
                    double y = config.getDouble("homes." + uuidStr + "." + homeName + ".y");
                    double z = config.getDouble("homes." + uuidStr + "." + homeName + ".z");
                    float yaw = (float) config.getDouble("homes." + uuidStr + "." + homeName + ".yaw");
                    float pitch = (float) config.getDouble("homes." + uuidStr + "." + homeName + ".pitch");
                    String worldName = config.getString("homes." + uuidStr + "." + homeName + ".world");

                    Location homeLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
                    playerHomes.put(homeName, homeLocation);
                }

                homeLocations.put(uuid, playerHomes);
        }
    }

    // Save homes data to homes.yml

    public void saveHomes() {
        // Iterate through the homes and save them
        for (Map.Entry<UUID, Map<String, Location>> entry : homeLocations.entrySet()) {
            UUID uuid = entry.getKey();
            Map<String, Location> playerHomes = entry.getValue();

            for (Map.Entry<String, Location> homeEntry : playerHomes.entrySet()) {
                String homeName = homeEntry.getKey();
                Location location = homeEntry.getValue();

                config.set("homes." + uuid.toString() + "." + homeName + ".x", location.getX());
                config.set("homes." + uuid.toString() + "." + homeName + ".y", location.getY());
                config.set("homes." + uuid.toString() + "." + homeName + ".z", location.getZ());
                config.set("homes." + uuid.toString() + "." + homeName + ".yaw", location.getYaw());
                config.set("homes." + uuid.toString() + "." + homeName + ".pitch", location.getPitch());
                config.set("homes." + uuid.toString() + "." + homeName + ".world", location.getWorld().getName());
            }
        }

        try {
            config.save(homesFile);
            plugin.saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create a home for a player at their current location.

    public void createHome(Player player, String homeName) {
        UUID playerUUID = player.getUniqueId();
        Location location = player.getLocation();

        homeLocations.putIfAbsent(playerUUID, new HashMap<>());
        homeLocations.get(playerUUID).put(homeName, location);
        saveHomes();
    }

    // Delete a home for a player

    public void deleteHome(Player player, String homeName) {
        UUID playerUUID = player.getUniqueId();
        Map<String, Location> playerHomes = homeLocations.get(playerUUID);

        if (playerHomes != null && playerHomes.containsKey(homeName)) {
            playerHomes.remove(homeName);
            homeLocations.put(playerUUID, playerHomes);
            saveHomes();
        }
    }

    // List all homes for a player.
    public List<String> listHomes(Player player) {
        UUID playerUUID = player.getUniqueId();
        Map<String, Location> playerHomes = homeLocations.get(playerUUID);
        return playerHomes == null ? List.of() : List.copyOf(playerHomes.keySet());
    }

    // Teleport a player to a specific home.
    public void teleportToHome(Player player, String homeName) {
        UUID playerUUID = player.getUniqueId();
        Map<String, Location> playerHomes = homeLocations.get(playerUUID);

        if (playerHomes != null && playerHomes.containsKey(homeName)) {
            Location homeLocation = playerHomes.get(homeName);
            player.teleport(homeLocation);
        }
    }

}
