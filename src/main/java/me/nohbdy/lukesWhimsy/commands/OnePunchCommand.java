package me.nohbdy.lukesWhimsy.commands;

import me.nohbdy.lukesWhimsy.DataManager;
import me.nohbdy.lukesWhimsy.LukesWhimsy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.util.Vector;

import java.util.UUID;

public class OnePunchCommand implements CommandExecutor, Listener {

    private String PLUGIN_PREFIX;
    private final LukesWhimsy plugin;
    private final DataManager dataManager;

    public OnePunchCommand(LukesWhimsy plugin) {
        this.plugin = plugin;
        this.dataManager = new DataManager(plugin);
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("onepunchman")) {
            if (!(sender instanceof Player) || !sender.isOp()) {
                sender.sendMessage(PLUGIN_PREFIX+"You must be an operator to use this command!");
                return true;
            }

            Player player = (Player) sender;
            UUID playerId = player.getUniqueId();
            boolean isEnabled = dataManager.getOnePunchPlayers().getOrDefault(playerId, false);

            if (isEnabled) {
                // Disable one-punch mode
                dataManager.getOnePunchPlayers().remove(playerId);
                player.sendMessage(PLUGIN_PREFIX+"One-punch mode disabled.");
            } else {
                // Enable one-punch mode
                dataManager.getOnePunchPlayers().put(playerId, true);
                player.sendMessage(PLUGIN_PREFIX+"One-punch mode enabled. You are now invincible.");
            }

            return true;
        }
        return false;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player || event.getDamager() instanceof Arrow) {
            Player player = null;
            if (event.getDamager() instanceof Player) {
                player = (Player) event.getDamager();
            } else if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();
                // Get shooter of arrow
                if (arrow.getShooter() instanceof Player) {
                    player = (Player) arrow.getShooter();
                } else {
                    return;
                }
            }
            UUID playerId = player.getUniqueId();
            if (dataManager.getOnePunchPlayers().containsKey(playerId)) {
                if(!(event.getEntity()instanceof Player) || !dataManager.getOnePunchPlayers().containsKey(event.getEntity().getUniqueId())) {
                    Entity damagedEntity = event.getEntity();
                    // Deal damage equal to one-shot (this could be set to a high value)
                    Vector direction = damagedEntity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                    // If one-punch mode is enabled, set damage to a high value
                    event.setDamage(9999);
                    direction.multiply(5.0);
                    direction.setY(1.5);
                    damagedEntity.setVelocity(direction);
                }
                if (dataManager.getOnePunchPlayers().containsKey(event.getEntity().getUniqueId())) {
                    Entity damagedEntity = event.getEntity();
                    // Apply velocity and knockback, but no damage if another player has this on.
                    Vector direction = damagedEntity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                    direction.multiply(5.0);
                    direction.setY(1.5);
                    damagedEntity.setVelocity(direction);
                }
            }
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UUID playerId = player.getUniqueId();
            if (dataManager.getOnePunchPlayers().containsKey(playerId)) {
                // Cancel fire tick damage
                if (player.getFireTicks()>0) {
                    player.setFireTicks(0);
                }
                // Cancel all damage
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            UUID playerId = player.getUniqueId();
            if (dataManager.getOnePunchPlayers().containsKey(playerId)) {
                // Cancel the event to prevent hunger loss
                event.setCancelled(true);
                player.setFoodLevel(20); // Ensure food level stays at maximum
                player.setSaturation(20); // Ensure saturation is also maxed
            }
        }
    }
}
