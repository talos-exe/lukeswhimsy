package me.nohbdyexe.lukesWhimsy.commands;

import me.nohbdyexe.lukesWhimsy.LukesWhimsy;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {

    private final LukesWhimsy plugin;
    private String PLUGIN_PREFIX;

    public FeedCommand(LukesWhimsy plugin) {
        this.plugin = plugin;
        this.PLUGIN_PREFIX = plugin.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender,Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("feed")) {
            if (!(sender instanceof Player) || !sender.isOp()) {
                sender.sendMessage(PLUGIN_PREFIX + "You must be an operator to use this command!");
                return true;
            }

            Player player = (Player) sender;

            // Set food level and saturation to maximum
            player.setFoodLevel(20);
            player.setSaturation(20);
            sender.sendMessage(PLUGIN_PREFIX+"You have fed yourself.");
        }
        return true;
    }
}
