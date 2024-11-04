package me.nohbdyexe.lukesWhimsy.commands;

import me.nohbdyexe.lukesWhimsy.LukesWhimsy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

public class FlingCommand implements CommandExecutor {

    private final double DEFAULT_KNOCKBACK_STRENGTH = 1.5;
    private String PLUGIN_PREFIX;

    public FlingCommand(LukesWhimsy plugin) {
        this.PLUGIN_PREFIX = plugin.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // FLING COMMAND
        //
        //
        //
        //
        ///////////////
        if (command.getName().equalsIgnoreCase("fling")) {
            if (!(sender instanceof Player) || !sender.isOp()) {
                sender.sendMessage(PLUGIN_PREFIX + "You must be an operator to use this command!");
                return true;
            }

            Player player = (Player) sender;
            double knockbackStrength = DEFAULT_KNOCKBACK_STRENGTH; // Initialize with default

            if (args.length == 0) {
                // Check if the first argument is a valid number
                flingPlayer(player, DEFAULT_KNOCKBACK_STRENGTH);
                player.sendMessage(PLUGIN_PREFIX + "You have flung yourself!");
                return true;
            }

            // Handle one argument possibility
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (args.length == 1) {
                // Check if its a valid number
                try {
                    knockbackStrength = Double.parseDouble(args[0]);
                    if (knockbackStrength > 0) {
                        flingPlayer(player, knockbackStrength);
                        player.sendMessage(PLUGIN_PREFIX+"You have flung yourself with a knockback of " +knockbackStrength + "!");
                    } else {
                        player.sendMessage(PLUGIN_PREFIX+"Knockback value must be greater than 0.");
                    }
                } catch (NumberFormatException e) {
                    if (targetPlayer != null && targetPlayer.isOnline()) {
                        flingPlayer(targetPlayer, DEFAULT_KNOCKBACK_STRENGTH);
                        player.sendMessage(PLUGIN_PREFIX+"You have flung " + targetPlayer.getName() + " with a knockback strength of 1.5!");
                    } else {
                        player.sendMessage(PLUGIN_PREFIX+"Player not found.");
                    }
                }
                return true;
            }

            if (args.length == 2) {
                try {
                    knockbackStrength = Double.parseDouble(args[1]);
                    if (knockbackStrength > 0) {
                        if (targetPlayer != null && targetPlayer.isOnline()) {
                            flingPlayer(targetPlayer, knockbackStrength);
                            player.sendMessage(PLUGIN_PREFIX+"You have flung " + targetPlayer.getName() + " with a knockback strength of " + knockbackStrength + "!");
                        } else {
                            player.sendMessage(PLUGIN_PREFIX+"Player not found.");
                        }
                    } else {
                        player.sendMessage(PLUGIN_PREFIX+"Knockback value must be greater than 0.");
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(PLUGIN_PREFIX+"Second argument must be a number.");
                }
            }
            return true;
        }
        return true;
    }

    private void flingPlayer(Player player, double knockbackStrength) {
        Random random = new Random();

        // Generate random direction
        double x = (random.nextDouble() - 0.5) * 2; // Random value between -1 and 1
        double z = (random.nextDouble() - 0.5) * 2; // Random value between -1 and 1

        // Normalize and apply the knockback strength
        Vector direction = new Vector(x, 0.5, z).normalize().multiply(knockbackStrength);
        player.setVelocity(direction);

        // Optional: Add a small upward velocity for a more dramatic fling effect
        player.setVelocity(player.getVelocity().add(new Vector(0, 1, 0))); // Adding upward velocity
    }
}
