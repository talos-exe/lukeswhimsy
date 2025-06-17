package me.nohbdy.lukesWhimsy.commands;

import me.nohbdy.lukesWhimsy.DataManager;
import me.nohbdy.lukesWhimsy.LukesWhimsy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoudmouthCommand implements CommandExecutor {

    private final String PLUGIN_PREFIX;

    public LoudmouthCommand(LukesWhimsy plugin) {
        DataManager dataManager = new DataManager(plugin);
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender is a player or the console (either can run the command)
        if (!sender.isOp()) {
            sender.sendMessage(PLUGIN_PREFIX+"You must be an operator to use this command!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(PLUGIN_PREFIX+"You must specify a player and a message for this command.");
            return true;
        }

        // Get the target player from the command arguments
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            sender.sendMessage(PLUGIN_PREFIX+"Player not found.");
            return true;
        }

        // Combine the rest of the arguments as the message
        String message = String.join(" ", args).substring(args[0].length() + 1);

        // Simulate the player saying the message
        targetPlayer.chat(message);

        // Optionally, inform the sender
        sender.sendMessage(PLUGIN_PREFIX+"You made " + targetPlayer.getName() + " say: " + message);

        return true;
    }
}