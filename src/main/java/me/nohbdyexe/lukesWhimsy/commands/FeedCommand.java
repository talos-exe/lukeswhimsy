package me.nohbdyexe.lukesWhimsy.commands;

import me.nohbdyexe.lukesWhimsy.DataManager;
import me.nohbdyexe.lukesWhimsy.LukesWhimsy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {

    private final LukesWhimsy plugin;
    private String PLUGIN_PREFIX;
    private final DataManager dataManager;

    public FeedCommand(LukesWhimsy plugin) {
        this.plugin = plugin;
        this.dataManager = new DataManager(plugin);
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender,Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("feed")) {
            if (!sender.isOp()) {
                sender.sendMessage(PLUGIN_PREFIX + "You must be an operator to use this command!");
                return true;
            }

            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args.length == 0) {
                    player.setFoodLevel(20);
                    player.setSaturation(20);
                    sender.sendMessage(PLUGIN_PREFIX+"You have fed yourself.");
                    return true;
                }
                else if (args.length == 1) {
                    Player targetPlayer = Bukkit.getPlayer(args[0]);
                    if(targetPlayer != null && targetPlayer.isOnline()) {
                        targetPlayer.setFoodLevel(20);
                        targetPlayer.setSaturation(20);
                        sender.sendMessage(PLUGIN_PREFIX+"You have fed " + targetPlayer.getName());
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
                        targetPlayer.setFoodLevel(20);
                        targetPlayer.setSaturation(20);
                        sender.sendMessage(PLUGIN_PREFIX + "Fed " + targetPlayer.getName());
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
}
