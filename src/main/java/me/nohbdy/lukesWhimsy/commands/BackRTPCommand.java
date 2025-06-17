package me.nohbdy.lukesWhimsy.commands;

import me.nohbdy.lukesWhimsy.DataManager;
import me.nohbdy.lukesWhimsy.LukesWhimsy;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.UUID;

public class BackRTPCommand implements CommandExecutor {

    private final HashMap<UUID, Location> lastLocations;
    private final String PLUGIN_PREFIX;

    public BackRTPCommand(LukesWhimsy plugin) {
        DataManager dataManager = new DataManager(plugin);
        this.lastLocations = dataManager.getRtpLastLocation();
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
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

    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Save the current location before teleporting
        lastLocations.put(playerId, player.getLocation());
    }


}
