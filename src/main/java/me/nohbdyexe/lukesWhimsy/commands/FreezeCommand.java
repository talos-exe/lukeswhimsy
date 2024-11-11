package me.nohbdyexe.lukesWhimsy.commands;

import me.nohbdyexe.lukesWhimsy.DataManager;
import me.nohbdyexe.lukesWhimsy.LukesWhimsy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.HashMap;
import java.util.UUID;

public class FreezeCommand implements CommandExecutor, Listener {

    private final LukesWhimsy plugin;
    private final DataManager dataManager;
    private final String PLUGIN_PREFIX;
    private final HashMap<UUID, Boolean> frozenPlayers; // To store frozen states

    public FreezeCommand(LukesWhimsy plugin) {
        this.plugin = plugin;
        this.dataManager = new DataManager(plugin);
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
        this.frozenPlayers = dataManager.getFrozenPlayers();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the sender is a player or an operator (if console)
        if (!sender.isOp()) {
            sender.sendMessage(PLUGIN_PREFIX + "You must be an operator to use this command!");
            return true;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(PLUGIN_PREFIX + "You must specify a player.");
                return false;
            }

            // Get the target player
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null || !targetPlayer.isOnline()) {
                player.sendMessage(PLUGIN_PREFIX + "Player " + args[0] + " not found or offline.");
                return true;
            }

            UUID targetUUID = targetPlayer.getUniqueId();
            boolean isFrozen = frozenPlayers.getOrDefault(targetUUID,false);

            // Toggle freeze state for the target player
            if (isFrozen) {
                // Unfreeze the player
                targetPlayer.setVelocity(targetPlayer.getVelocity());
                targetPlayer.setAllowFlight(false);
                targetPlayer.setFlying(false);
                frozenPlayers.put(targetUUID,false);
                player.sendMessage(PLUGIN_PREFIX + "Unfroze " + targetPlayer.getName() + ".");
                targetPlayer.sendMessage(PLUGIN_PREFIX + "You have been unfrozen.");
            } else {
                // Freeze the player
                targetPlayer.setVelocity(targetPlayer.getVelocity().zero());
                targetPlayer.setAllowFlight(false);
                targetPlayer.setFlying(false);
                frozenPlayers.put(targetUUID,true);
                player.sendMessage(PLUGIN_PREFIX + "Froze " + targetPlayer.getName() + ".");
                targetPlayer.sendMessage(PLUGIN_PREFIX + "You have been frozen.");
            }
        }

        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                Player targetPlayer = Bukkit.getPlayer(args[0]);
                UUID targetUUID = targetPlayer.getUniqueId();
                boolean isFrozen = frozenPlayers.getOrDefault(targetUUID,false);
                if (targetPlayer != null && targetPlayer.isOnline()) {
                    // Toggle freeze state for the target player
                    if (isFrozen) {
                        // Unfreeze the player
                        targetPlayer.setVelocity(targetPlayer.getVelocity());
                        targetPlayer.setAllowFlight(false);
                        targetPlayer.setFlying(false);
                        frozenPlayers.put(targetUUID,false);
                        sender.sendMessage(PLUGIN_PREFIX + "Unfroze " + targetPlayer.getName() + ".");
                        targetPlayer.sendMessage(PLUGIN_PREFIX + "You have been unfrozen.");
                    } else {
                        // Freeze the player
                        targetPlayer.setVelocity(targetPlayer.getVelocity().zero());
                        targetPlayer.setAllowFlight(false);
                        targetPlayer.setFlying(false);
                        frozenPlayers.put(targetUUID,true);
                        sender.sendMessage(PLUGIN_PREFIX + "Froze " + targetPlayer.getName() + ".");
                        targetPlayer.sendMessage(PLUGIN_PREFIX + "You have been frozen.");
                    }
                } else {
                    sender.sendMessage(PLUGIN_PREFIX + "Player not found.");
                }
            }  else {
                sender.sendMessage(PLUGIN_PREFIX+"Console needs a target player!");
            }
        }

        return true;
    }

    @EventHandler
    public void onPlayerJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (frozenPlayers.getOrDefault(playerUUID, false)) {
            event.setCancelled(true); // Prevent the player from toggling flight
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (frozenPlayers.getOrDefault(playerUUID, false)) {
            event.setCancelled(true); // Prevent the player from moving
        }
    }

}