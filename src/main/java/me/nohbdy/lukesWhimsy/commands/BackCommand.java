package me.nohbdy.lukesWhimsy.commands;

import me.nohbdy.lukesWhimsy.DataManager;
import me.nohbdy.lukesWhimsy.LukesWhimsy;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.UUID;

public class BackCommand implements CommandExecutor, Listener {

    private final HashMap<UUID, Location> lastLocations;
    private final String PLUGIN_PREFIX;

    public BackCommand(LukesWhimsy plugin) {
        DataManager dataManager = new DataManager(plugin);
        this.lastLocations = dataManager.getRtpLastLocation();
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("back")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(PLUGIN_PREFIX + "You must be a player to use that command.");
                return true;
            }

            UUID playerId = player.getUniqueId();
            Location currentLocation = player.getLocation();

            // Check if the player has a last location saved
            if (lastLocations.containsKey(playerId)) {
                Location lastLocation = lastLocations.get(playerId);
                player.teleport(lastLocation);
                lastLocations.remove(playerId); // Remove location just teleported to
                player.sendMessage(PLUGIN_PREFIX + "Teleported back to your last location.");
                lastLocations.put(playerId, currentLocation); // Add back command to location we just teleported from.
            } else {
                player.sendMessage(PLUGIN_PREFIX + "No last location found.");
            }
        }
        return true;
    }

    // Save location on teleport
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Save the current location before teleporting
        lastLocations.put(playerId, player.getLocation());
    }

    // Save location when player dies
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerId = player.getUniqueId();

        // Save the location where the player died
        lastLocations.put(playerId, player.getLocation());
    }


}
