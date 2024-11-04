package me.nohbdyexe.lukesWhimsy.commands;

import me.nohbdyexe.lukesWhimsy.LukesWhimsy;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BackRTPCommand implements CommandExecutor {

    private final LukesWhimsy plugin;
    private HashMap<UUID, Location> lastLocations;
    private String PLUGIN_PREFIX;

    public BackRTPCommand(LukesWhimsy plugin) {
        this.plugin = plugin;
        this.lastLocations = plugin.getRtpLastLocation();
        this.PLUGIN_PREFIX = plugin.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("back")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(PLUGIN_PREFIX + "You must be a player to use that command.");
                return true;
            }

            Player player = (Player) sender;
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
}
