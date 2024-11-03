package me.nohbdyexe.lukesWhimsy.commands;

import me.nohbdyexe.lukesWhimsy.LukesWhimsy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SmiteCommand implements CommandExecutor {

    private final LukesWhimsy plugin;
    private String PLUGIN_PREFIX;

    public SmiteCommand(LukesWhimsy plugin) {
        this.plugin = plugin;
        this.PLUGIN_PREFIX = plugin.getPluginPrefix();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // SMITE COMMAND
        //
        //
        //
        //
        //
        //
        ///////////////
        if (command.getName().equalsIgnoreCase("smite") && args.length == 1) {
            if (!(sender instanceof Player) || !sender.isOp()) {
                sender.sendMessage( PLUGIN_PREFIX + "You must be an operator to use this command!");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target != null && target.isOnline()) {
                Location location = target.getLocation();
                target.getWorld().strikeLightning(location);
                target.sendMessage(PLUGIN_PREFIX + "You have been smitten!");
                sender.sendMessage(PLUGIN_PREFIX + "Smiting " + target.getName() + "!");
                return true;
            } else {
                sender.sendMessage(PLUGIN_PREFIX + "Player not found!");
                return true;
            }
        }
        return true;
    }
}
