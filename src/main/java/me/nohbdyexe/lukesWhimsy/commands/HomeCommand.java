package me.nohbdyexe.lukesWhimsy.commands;

import me.nohbdyexe.lukesWhimsy.DataManager;
import me.nohbdyexe.lukesWhimsy.LukesWhimsy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeCommand implements CommandExecutor {

    private final HomeDataManager homeDataManager;
    private final LukesWhimsy plugin;
    private final DataManager dataManager;
    private final String PLUGIN_PREFIX;

    public HomeCommand(LukesWhimsy plugin) {
        this.plugin = plugin;
        this.dataManager = new DataManager(plugin);
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
        this.homeDataManager = new HomeDataManager(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,  String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(PLUGIN_PREFIX+"Usage: /home [homename] or /home create|delete|listall");
            return true; // Show usage if no arguments
        }

        String action = args[0];

        switch (action.toLowerCase()) {
            case "create":
                if (args.length < 2) {
                    player.sendMessage(PLUGIN_PREFIX+"Usage: /home create [homename]");
                    return true;
                }
                String homeNameCreate = args[1];
                homeDataManager.createHome(player, homeNameCreate);
                player.sendMessage(PLUGIN_PREFIX+"Home '" + homeNameCreate + "' created.");
                break;

            case "delete":
                if (args.length < 2) {
                    player.sendMessage(PLUGIN_PREFIX+"Usage: /home delete [homename]");
                    return true;
                }
                String homeNameDelete = args[1];
                homeDataManager.deleteHome(player, homeNameDelete);
                player.sendMessage(PLUGIN_PREFIX+"Home '" + homeNameDelete + "' deleted.");
                break;

            case "listall":
                List<String> homes = homeDataManager.listHomes(player);
                if (homes.isEmpty()) {
                    player.sendMessage(PLUGIN_PREFIX+"You have no homes.");
                } else {
                    player.sendMessage(PLUGIN_PREFIX+"Your homes: " + String.join(", ", homes));
                }
                break;

            default:
                String homeName = args[0];
                homeDataManager.teleportToHome(player, homeName);
                player.sendMessage(PLUGIN_PREFIX+"Teleporting to home '" + homeName + "'.");
                break;
        }


        return true;
    }
}
