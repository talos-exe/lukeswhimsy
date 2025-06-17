package me.nohbdy.lukesWhimsy.commands;

import me.nohbdy.lukesWhimsy.DataManager;
import me.nohbdy.lukesWhimsy.LukesWhimsy;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TopCommand implements CommandExecutor {

    private final String PLUGIN_PREFIX;

    public TopCommand(LukesWhimsy plugin) {
        DataManager dataManager = new DataManager(plugin);
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command sender is a player
        if (!(sender instanceof Player || !sender.isOp())) {
            sender.sendMessage(PLUGIN_PREFIX + "You must be an operator to use this command.");
            return true;
        }

        assert sender instanceof Player;
        Player player = (Player) sender;

        // Get the player's current X and Z coordinates
        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();

        // Get the highest block Y at the player's current X, Z coordinates
        int y = player.getWorld().getHighestBlockYAt((int) x, (int) z);

        // Create a new Location object for the teleport destination (same X, Z, highest Y) (add player offset of 2 blocks)
        Location topLocation = new Location(player.getWorld(), x, y+2, z);

        // Teleport the player to the calculated location
        player.teleport(topLocation);

        // Send a confirmation message to the player
        player.sendMessage(PLUGIN_PREFIX + "You have been teleported to the surface.");

        return true;
    }
}