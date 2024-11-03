package me.nohbdyexe.lukesWhimsy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatColor;
import java.util.HashMap;
import java.util.UUID;

public final class LukesWhimsy extends JavaPlugin {

    private final HashMap<UUID, ArmorStand> sittingPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("LukesWhimsy ver 1.0 enabled.");
        getCommand("smite").setExecutor(this);
        getCommand("sit").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("LukesWhimsy ver 1.0 disabled.");
        for (ArmorStand stand : sittingPlayers.values()) {
            stand.remove();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("smite") && args.length == 1) {
            if (!(sender instanceof Player) || !sender.isOp()) {
                sender.sendMessage(ChatColor.BLUE+"[LukesWhimsy] " + ChatColor.RESET + "You must be an operator to use this command!");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target != null && target.isOnline()) {
                Location location = target.getLocation();
                target.getWorld().strikeLightning(location);
                target.sendMessage(ChatColor.BLUE + "[LukesWhimsy] " + ChatColor.RESET + "You have been smitten!");
                sender.sendMessage(ChatColor.BLUE + "[LukesWhimsy] " + ChatColor.RESET + "Smiting " + target.getName() + "!");
                return true;
            } else {
                sender.sendMessage(ChatColor.BLUE+"[LukesWhimsy] " + ChatColor.RESET + "Player not found!");
                return true;
            }
        }

        if (command.getName().equalsIgnoreCase("sit")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.BLUE+"[LukesWhimsy]" + ChatColor.RESET+" Only players can use this command.");
                return true;
            }

            Player player = (Player) sender;

            //Check if player is already sitting
            if (sittingPlayers.containsKey(player.getUniqueId())) {
                //Stand up the player
                ArmorStand stand = sittingPlayers.get(player.getUniqueId());
                stand.remove();
                sittingPlayers.remove(player.getUniqueId()); // Remove from hashmap
                player.sendMessage("Standing.");
                return true;
            }

            if (args.length > 0) {
                // Try to sit on another player's head
                Player targetPlayer = Bukkit.getPlayer(args[0]);
                if (targetPlayer != null && targetPlayer.isOnline()) {
                    targetPlayer.addPassenger(player);
                    player.sendMessage("You are now sitting on " + targetPlayer.getName() +"'s head.");
                } else {
                    player.sendMessage("Player not found.");
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
                player.sendMessage("You are now sitting.");
            }
            return true;
        }
        return false;
    }
}
