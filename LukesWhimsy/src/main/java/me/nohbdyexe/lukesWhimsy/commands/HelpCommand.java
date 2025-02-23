package me.nohbdyexe.lukesWhimsy.commands;

import me.nohbdyexe.lukesWhimsy.DataManager;
import me.nohbdyexe.lukesWhimsy.LukesWhimsy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {
    private final LukesWhimsy plugin;
    private String PLUGIN_PREFIX;
    //private final List<String> helpCommands;
    private final DataManager dataManager;

    public HelpCommand(LukesWhimsy plugin) {
        this.plugin = plugin;
        this.dataManager = new DataManager(plugin);
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
        //helpCommands.add("")
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("lwhelp")) {

            if (sender.isOp()) {
                sender.sendMessage(ChatColor.BLUE + "---------- [LukesWhimsy] Help ----------");
                sender.sendMessage(ChatColor.BLUE + "/feed <player>" + ChatColor.RESET + " - Feed yourself or a player.");
                sender.sendMessage(ChatColor.BLUE + "/heal <player>" + ChatColor.RESET + " - Heal yourself or a player.");
                sender.sendMessage(ChatColor.BLUE + "/fling <player> [distance]" + ChatColor.RESET + " - Fling yourself or a certain player. Distance modifiable.");
                sender.sendMessage(ChatColor.BLUE + "/onepunchman" + ChatColor.RESET + " - Makes one invincible and able to one shot everything.");
                sender.sendMessage(ChatColor.BLUE + "/catapult" + ChatColor.RESET + " - Shoots a projectile that explodes.");
                sender.sendMessage(ChatColor.BLUE + "/fly" + ChatColor.RESET + " - Toggles fly on/off for yourself.");
                sender.sendMessage(ChatColor.BLUE + "/sit <player> <targetplayer>" + ChatColor.RESET + " - Sit anywhere or on a player. Able to make a player sit on another player.");
                sender.sendMessage(ChatColor.BLUE + "/hng" + ChatColor.RESET + " - Make a villager noise.");
                sender.sendMessage(ChatColor.BLUE + "/babytrap <player>" + ChatColor.RESET + " - Toggle babytrap on/off to retaliate against players, or simply summon them onto a player.");
                sender.sendMessage(ChatColor.BLUE + "/rtp" + ChatColor.RESET + " - Randomly teleport anywhere within the world border.");
                sender.sendMessage(ChatColor.BLUE + "/back" + ChatColor.RESET + " - Returns you to your last saved location.");
                sender.sendMessage(ChatColor.BLUE + "/smite <player>" + ChatColor.RESET + " - Smite a player.");
                sender.sendMessage(ChatColor.BLUE + "/top" + ChatColor.RESET + " - Teleport to the surface.");
                sender.sendMessage(ChatColor.BLUE + "/freeze <player>" + ChatColor.RESET + " - Freeze a player.");
                sender.sendMessage(ChatColor.BLUE + "/loudmouth <player>" + ChatColor.RESET + " - Make a player say what you want them to say.");
                return true;
            }

            sender.sendMessage(ChatColor.BLUE + "---------- [LukesWhimsy] Help ----------");
            sender.sendMessage(ChatColor.BLUE + "/sit <player>" + ChatColor.RESET + " - Sit anywhere or on a player.");
            sender.sendMessage(ChatColor.BLUE + "/hng" + ChatColor.RESET + " - Make a villager noise.");
            sender.sendMessage(ChatColor.BLUE + "/rtp" + ChatColor.RESET + " - Randomly teleport anywhere within the world border.");
            sender.sendMessage(ChatColor.BLUE + "/back" + ChatColor.RESET + " - Returns you to your last saved location (via rtp).");

            return true;
        }
        return true;
    }
}
