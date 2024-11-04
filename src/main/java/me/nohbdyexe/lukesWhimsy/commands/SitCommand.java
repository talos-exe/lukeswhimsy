package me.nohbdyexe.lukesWhimsy.commands;

import me.nohbdyexe.lukesWhimsy.LukesWhimsy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.UUID;

public class SitCommand implements CommandExecutor, Listener{

    private HashMap<UUID, ArmorStand> sittingPlayers = new HashMap<>();
    private String PLUGIN_PREFIX;

    public SitCommand(LukesWhimsy plugin) {
        this.sittingPlayers = plugin.getSittingPlayers();
        this.PLUGIN_PREFIX = plugin.getPluginPrefix();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
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
            if(!(sender instanceof Player)) {
                sender.sendMessage(PLUGIN_PREFIX + "Only players can use this command.");
                return true;
            }

            Player player = (Player) sender;

            //Check if player is already sitting
            if (sittingPlayers.containsKey(player.getUniqueId())) {
                //Stand up the player
                ArmorStand stand = sittingPlayers.get(player.getUniqueId());
                stand.remove();
                sittingPlayers.remove(player.getUniqueId()); // Remove from hashmap
                player.sendMessage(PLUGIN_PREFIX + "Standing.");
                return true;
            }

            if (args.length > 0) {
                // Try to sit on another player's head
                Player targetPlayer = Bukkit.getPlayer(args[0]);
                if (targetPlayer != null && targetPlayer.isOnline()) {
                    if (player.getUniqueId().equals(targetPlayer.getUniqueId())) {
                        player.sendMessage( PLUGIN_PREFIX + "You cannot sit on yourself!");
                        return true; // Stop processing further
                    }

                    if (!sittingPlayers.containsKey(targetPlayer.getUniqueId())) {
                        targetPlayer.addPassenger(player);
                        player.sendMessage(PLUGIN_PREFIX + "You are now sitting on " + targetPlayer.getName() +"'s head.");
                        sittingPlayers.put(player.getUniqueId(), null);
                    } else {
                        player.sendMessage(PLUGIN_PREFIX+"You cannot sit on top of a player that is already sitting.");
                    }
                } else {
                    player.sendMessage(PLUGIN_PREFIX + "Player not found.");
                    return true;
                }
            } else {
                // Sit at the current location
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
                return true;
            }
            return true;
        }
        return true;
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (sittingPlayers.containsKey(player.getUniqueId()) && event.isSneaking()) {
            // If the player is sitting and now sneaking (shifting), stand them up
            ArmorStand stand = sittingPlayers.get(player.getUniqueId());
            if (stand != null) {
                stand.remove();
            }
            sittingPlayers.remove(player.getUniqueId()); // Remove from hashmap
            player.sendMessage(PLUGIN_PREFIX + "You stood up!");
        }
    }

}
