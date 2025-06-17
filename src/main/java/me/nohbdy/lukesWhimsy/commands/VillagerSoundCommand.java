package me.nohbdy.lukesWhimsy.commands;

import me.nohbdy.lukesWhimsy.DataManager;
import me.nohbdy.lukesWhimsy.LukesWhimsy;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class VillagerSoundCommand implements CommandExecutor {

    private final String PLUGIN_PREFIX;

    public VillagerSoundCommand(LukesWhimsy plugin) {
        DataManager dataManager = new DataManager(plugin);
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PLUGIN_PREFIX + "Only players can use this command.");
            return true;
        }

        Location location = player.getLocation(); // Get the player's location

        // Play the villager noise at the player's location
        player.getWorld().playSound(location, Sound.ENTITY_VILLAGER_AMBIENT, 1.0F, 1.0F);

        return true;
    }
}
