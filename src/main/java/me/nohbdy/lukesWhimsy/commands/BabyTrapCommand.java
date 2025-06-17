package me.nohbdy.lukesWhimsy.commands;

import me.nohbdy.lukesWhimsy.DataManager;
import me.nohbdy.lukesWhimsy.LukesWhimsy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.UUID;

public class BabyTrapCommand implements CommandExecutor, Listener {

    private final LukesWhimsy plugin;
    private String PLUGIN_PREFIX;
    private HashMap<UUID, Boolean> activeTraps;
    private final DataManager dataManager;

    public BabyTrapCommand(LukesWhimsy plugin) {
        this.plugin = plugin;
        this.dataManager = new DataManager(plugin);
        this.PLUGIN_PREFIX = dataManager.getPluginPrefix();
        this.activeTraps = dataManager.getTrappedPlayers();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("babytrap")) {
            if (!(sender instanceof Player) || !sender.isOp()) {
                sender.sendMessage(PLUGIN_PREFIX + "You must be an operator to use this command!");
                return true;
            }
            Player player = (Player) sender;
            UUID playerId = player.getUniqueId();
            boolean isActive = activeTraps.getOrDefault(playerId, false);

            if (args.length == 0) {
                // Toggle the baby trap for the sender
                activeTraps.put(playerId, !isActive);
                String status = !isActive ? "enabled." : "disabled.";
                player.sendMessage(PLUGIN_PREFIX+"Baby trap " + status);
            } else if (args.length == 1){
                // Summon baby zombies around the specified player
                Player targetPlayer = Bukkit.getPlayer(args[0]);
                if (targetPlayer != null && targetPlayer.isOnline()) {
                    summonBabyZombiesAround(targetPlayer.getLocation());
                    player.sendMessage(PLUGIN_PREFIX+"Baby trapped " + targetPlayer.getName() + "!");
                } else {
                    player.sendMessage(PLUGIN_PREFIX + "Player not found.");
                }
            }

            return true;
        }
        return true;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            UUID attackerId = attacker.getUniqueId();
            UUID victimId = victim.getUniqueId();
            if (activeTraps.getOrDefault(victimId, false)) {
                if (event.getEntity() instanceof Player) {
                    summonBabyZombiesAround(attacker.getLocation());
                }
            }
        }
    }

    private void summonBabyZombiesAround(Location loc) {
        for (int i=0; i<3; i++) {
            Location spawnLocation = loc.clone().add((Math.random() - 0.5) * 5, 0, (Math.random() - 0.5) * 5);
            // Spawn the zombie and cast it to Zombie.
            Zombie babyZombie = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
            babyZombie.setBaby();
        }
    }

}
