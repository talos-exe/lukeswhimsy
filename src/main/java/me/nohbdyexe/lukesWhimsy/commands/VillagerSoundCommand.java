package me.nohbdyexe.lukesWhimsy.commands;

import me.nohbdyexe.lukesWhimsy.DataManager;
import me.nohbdyexe.lukesWhimsy.LukesWhimsy;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class VillagerSoundCommand implements CommandExecutor {

    private final LukesWhimsy plugin;
    private String PLUGIN_PREFIX;
    private final DataManager dataManager;

    public VillagerSoundCommand(LukesWhimsy plugin) {
        this.plugin = plugin;
        this.dataManager = new DataManager(plugin);
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLUGIN_PREFIX + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        Location location = player.getLocation(); // Get the player's location

        // Play the villager noise at the player's location
        player.getWorld().playSound(location, Sound.ENTITY_VILLAGER_AMBIENT, 1.0F, 1.0F);

        return true;
    }
}
