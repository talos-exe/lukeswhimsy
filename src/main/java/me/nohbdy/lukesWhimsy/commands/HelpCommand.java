package me.nohbdy.lukesWhimsy.commands;

import me.nohbdy.lukesWhimsy.DataManager;
import me.nohbdy.lukesWhimsy.LukesWhimsy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.*;

public class HelpCommand implements CommandExecutor {

    public HelpCommand(LukesWhimsy plugin) {
        //private final List<String> helpCommands;
        //helpCommands.add("")
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("lwhelp")) {

            if (sender.isOp()) {
                sender.sendMessage(BLUE + "---------- [LukesWhimsy] Help ----------");
                sender.sendMessage(BLUE + "/feed <player>" + RESET + " - Feed yourself or a player.");
                sender.sendMessage(BLUE + "/heal <player>" + RESET + " - Heal yourself or a player.");
                sender.sendMessage(BLUE + "/fling <player> [distance]" + RESET + " - Fling yourself or a certain player. Distance modifiable.");
                sender.sendMessage(BLUE + "/onepunchman" + RESET + " - Makes one invincible and able to one shot everything.");
                sender.sendMessage(BLUE + "/catapult" + RESET + " - Shoots a projectile that explodes.");
                sender.sendMessage(BLUE + "/fly" + RESET + " - Toggles fly on/off for yourself.");
                sender.sendMessage(BLUE + "/sit <player> <targetplayer>" + RESET + " - Sit anywhere or on a player. Able to make a player sit on another player.");
                sender.sendMessage(BLUE + "/hng" + RESET + " - Make a villager noise.");
                sender.sendMessage(BLUE + "/babytrap <player>" + RESET + " - Toggle babytrap on/off to retaliate against players, or simply summon them onto a player.");
                sender.sendMessage(BLUE + "/rtp" + RESET + " - Randomly teleport anywhere within the world border.");
                sender.sendMessage(BLUE + "/back" + RESET + " - Returns you to your last saved location.");
                sender.sendMessage(BLUE + "/smite <player>" + RESET + " - Smite a player.");
                sender.sendMessage(BLUE + "/top" + RESET + " - Teleport to the surface.");
                sender.sendMessage(BLUE + "/freeze <player>" + RESET + " - Freeze a player.");
                sender.sendMessage(BLUE + "/loudmouth <player>" + RESET + " - Make a player say what you want them to say.");
                return true;
            }

            sender.sendMessage(BLUE + "---------- [LukesWhimsy] Help ----------");
            sender.sendMessage(BLUE + "/sit <player>" + RESET + " - Sit anywhere or on a player.");
            sender.sendMessage(BLUE + "/hng" + RESET + " - Make a villager noise.");
            sender.sendMessage(BLUE + "/rtp" + RESET + " - Randomly teleport anywhere within the world border.");
            sender.sendMessage(BLUE + "/back" + RESET + " - Returns you to your last saved location (via rtp).");

            return true;
        }
        return true;
    }
}
