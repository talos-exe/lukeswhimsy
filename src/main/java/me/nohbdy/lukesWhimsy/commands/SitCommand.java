package me.nohbdy.lukesWhimsy.commands;

import me.nohbdy.lukesWhimsy.DataManager;
import me.nohbdy.lukesWhimsy.LukesWhimsy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class SitCommand implements CommandExecutor, Listener {

    private HashMap<UUID, ArmorStand> sittingPlayers;
    private HashSet<UUID> sittingOnPlayer = new HashSet<>();
    private String PLUGIN_PREFIX;
    private static final double SIT_RADIUS = 5.0; // Maximum distance to sit on another player.
    private final DataManager dataManager;

    public SitCommand(LukesWhimsy plugin) {
        this.dataManager = new DataManager(plugin);
        this.sittingPlayers = dataManager.getSittingPlayers();
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // SIT COMMAND
        //
        //
        //
        //
        //
        ///////////////
        if (command.getName().equalsIgnoreCase("sit")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(PLUGIN_PREFIX + "Only players can use this command.");
                return true;
            }

            Player player = (Player) sender;
            Player targetPlayer = null;

            if (args.length == 1) {
                targetPlayer = Bukkit.getPlayer(args[0]);
                if (targetPlayer == null || !targetPlayer.isOnline()) {
                    sender.sendMessage(PLUGIN_PREFIX + "Player not found.");
                    return true;
                }

                if (player.getUniqueId().equals(targetPlayer.getUniqueId())) {
                    player.sendMessage(PLUGIN_PREFIX + "You cannot sit on yourself!");
                    return true;
                }
                handleSitOnPlayerCommand(player, targetPlayer);
            } else {
                handleSitCommand(player);
            }
            return true;
        }
        return true;
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        //Check if player is sitting on armor stand or another player
        if (sittingPlayers.containsKey(player.getUniqueId()) || sittingOnPlayer.contains(player.getUniqueId())) {
            if (event.isSneaking()) {
                if (sittingPlayers.containsKey(player.getUniqueId())) { // Check if they're sitting down or on another player.
                    ArmorStand stand = sittingPlayers.get(player.getUniqueId());
                    if (stand != null) {
                        stand.remove();
                    }
                    sittingPlayers.remove(player.getUniqueId()); // Remove from hashmap
                } else {
                    sittingOnPlayer.remove(player.getUniqueId()); // Remove from on top of player.
                }
                player.sendMessage(PLUGIN_PREFIX + "You stood up.");
            }
        }
    }

    private void handleSitCommand(Player player) {
        // Check if the player is already sitting
        if (sittingPlayers.containsKey(player.getUniqueId())) {
            ArmorStand stand = sittingPlayers.get(player.getUniqueId());
            if (stand != null) {
                stand.remove(); // Remove the armor stand if sitting on it
            }
            sittingPlayers.remove(player.getUniqueId());
            player.sendMessage(PLUGIN_PREFIX + "You stood up.");
        }
        else if (sittingOnPlayer.contains(player.getUniqueId())) {
            sittingOnPlayer.remove(player.getUniqueId());
            Entity targetPlayerVehicle = player.getVehicle();
            targetPlayerVehicle.removePassenger(player);
            player.sendMessage(PLUGIN_PREFIX + "You stood up.");
        } else {
            // Make the player sit at their current location
            Location loc = player.getLocation();
            ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class);
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setMarker(true);
            stand.setInvulnerable(true);
            stand.setCollidable(false);
            stand.setSmall(true);

            stand.addPassenger(player);
            sittingPlayers.put(player.getUniqueId(), stand);
            player.sendMessage(PLUGIN_PREFIX + "You are now sitting.");
        }
    }

    private void handleSitOnPlayerCommand(Player sitter, Player target) {
        // Check if sender is already sitting on someone.
        if (sittingOnPlayer.contains(sitter.getUniqueId())) {
            target.removePassenger(sitter);
            sittingOnPlayer.remove(sitter.getUniqueId());
        }

        if (sittingPlayers.containsKey(sitter.getUniqueId())) {
            sittingPlayers.remove(sitter.getUniqueId());
        }

        if (sitter.getLocation().distance(target.getLocation()) > SIT_RADIUS) {
            sitter.sendMessage(PLUGIN_PREFIX+"That player is too far away.");
            return;
        }

        target.addPassenger(sitter);
        sittingOnPlayer.add(sitter.getUniqueId());
        sitter.sendMessage(PLUGIN_PREFIX + "You are now sitting on " + target.getName());
        target.sendMessage(PLUGIN_PREFIX + sitter.getName() + " is now sitting on your head.");
    }
}
