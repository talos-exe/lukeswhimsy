package me.nohbdy.lukesWhimsy.commands;

import me.nohbdy.lukesWhimsy.DataManager;
import me.nohbdy.lukesWhimsy.LukesWhimsy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    private final String PLUGIN_PREFIX;

    public FlyCommand(LukesWhimsy plugin) {
        DataManager dataManager = new DataManager(plugin);
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("fly")) {
            if (!sender.isOp()) {
                sender.sendMessage(PLUGIN_PREFIX + "You must be an operator to use this command!");
                return true;
            }

            if (sender instanceof Player player) {

                if (args.length == 0) {
                    toggleFlight(player);
                    sender.sendMessage(PLUGIN_PREFIX+"Flying toggled on/off.");
                }
                else if (args.length == 1) {
                    Player targetPlayer = Bukkit.getPlayer(args[0]);
                    if(targetPlayer != null && targetPlayer.isOnline()) {
                        toggleFlight(targetPlayer);
                        sender.sendMessage(PLUGIN_PREFIX+"Toggled flying on/off for "+targetPlayer.getName());
                    } else {
                        sender.sendMessage(PLUGIN_PREFIX+"Player not found.");
                    }
                }
                return true;
            }

            if (sender instanceof ConsoleCommandSender) {
                if (args.length == 1) {
                    Player targetPlayer = Bukkit.getPlayer(args[0]);
                    if (targetPlayer != null && targetPlayer.isOnline()) {
                        toggleFlight(targetPlayer);
                        sender.sendMessage(PLUGIN_PREFIX + "Toggled flying on/off for " + targetPlayer.getName());
                    } else {
                        sender.sendMessage(PLUGIN_PREFIX + "Player not found.");
                    }
                }  else {
                    sender.sendMessage(PLUGIN_PREFIX+"Console needs a target player!");
                }
            }
        }

        return true;
    }

    private void toggleFlight(Player player) {
        boolean flightenabled = player.getAllowFlight();
        player.setAllowFlight(!flightenabled);
        player.setFlying(!flightenabled);
    }
}
