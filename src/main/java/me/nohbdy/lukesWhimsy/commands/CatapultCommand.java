package me.nohbdy.lukesWhimsy.commands;
import me.nohbdy.lukesWhimsy.DataManager;
import me.nohbdy.lukesWhimsy.LukesWhimsy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class CatapultCommand implements CommandExecutor, Listener {

    private final String PLUGIN_PREFIX;
    private final HashMap<UUID, CatapultData> catapultDataMap;

    public CatapultCommand(LukesWhimsy plugin) {
        DataManager dataManager = new DataManager(plugin);
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
        this.catapultDataMap = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    private record CatapultData(UUID playerId, Location landingLocation) {
    }

    @Override
    public boolean onCommand(CommandSender sender,Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("catapult")) {
            if (!(sender instanceof Player player) || !sender.isOp()) {
                sender.sendMessage(PLUGIN_PREFIX+"You must be an operator to use this command!");
                return true;
            }

            Location playerLocation = player.getLocation();
            Vector direction = playerLocation.getDirection().normalize().multiply(1.2); // Move 20 blocks forward
            direction.setY(0.6);

            // Calculate the landing location for the arrow
            Location landingLocation = playerLocation.clone().add(direction);
            landingLocation.setY(playerLocation.getY()); // Launch the arrow above the player

            // Spawn the arrow
            FallingBlock fallingBlock = player.getWorld().spawnFallingBlock(playerLocation.add(0, 1.5, 0), Material.ANVIL.createBlockData());
            fallingBlock.setVelocity(direction);
            fallingBlock.setDropItem(false); // Prevent block drops

            catapultDataMap.put(fallingBlock.getUniqueId(), new CatapultData(player.getUniqueId(), landingLocation));

            sender.sendMessage(PLUGIN_PREFIX+"Launching catapult!");

            return true;
        }
        return false;
    }

    @EventHandler
    public void onFallingBlockLand(EntityChangeBlockEvent event) {
        // Check if entity is an arrow.
        if (event.getEntity() instanceof FallingBlock fallingBlock) {
            CatapultData catapultData = catapultDataMap.get(fallingBlock.getUniqueId());

            if (catapultData != null) {
                fallingBlock.getWorld().createExplosion(fallingBlock.getLocation(), 4F, false, false);
                Player player = Bukkit.getPlayer(catapultData.playerId());
                if(player != null && player.isOnline()) {
                    player.sendMessage(PLUGIN_PREFIX+"Kaboom!");
                }
                event.setCancelled(true);
                catapultDataMap.remove(fallingBlock.getUniqueId());
            }
        }
    }
}
