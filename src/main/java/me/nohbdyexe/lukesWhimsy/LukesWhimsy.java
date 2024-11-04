package me.nohbdyexe.lukesWhimsy;
import me.nohbdyexe.lukesWhimsy.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;


public final class LukesWhimsy extends JavaPlugin implements @NotNull Listener {

    private HashMap<UUID, ArmorStand> sittingPlayers = new HashMap<>();
    private HashMap<UUID, Boolean> onePunchPlayers = new HashMap<>();
    private HashMap<UUID, Boolean> trappedPlayers = new HashMap<>();
    private final String PLUGIN_PREFIX = ChatColor.BLUE + "[LukesWhimsy] " + ChatColor.RESET;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("[LukesWhimsy]ver 1.2 enabled.");
        getLogger().info("[LukesWhimsy] use /whimsyhelp to get started!");
        getCommand("smite").setExecutor(new SmiteCommand(this));
        getCommand("sit").setExecutor(new SitCommand(this));
        getCommand("fling").setExecutor(new FlingCommand(this));
        getCommand("onepunchman").setExecutor(new OnePunchCommand(this));
        getCommand("catapult").setExecutor(new CatapultCommand(this));
        getCommand("feed").setExecutor(new FeedCommand(this));
        getCommand("heal").setExecutor(new HealCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("whimsyhelp").setExecutor(new HelpCommand(this));
        getCommand("hng").setExecutor(new VillagerSoundCommand(this));
        getCommand("babytrap").setExecutor(new BabyTrapCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("[LukesWhimsy] ver 1.2 disabled.");
        cleanupAllHashes();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        getLogger().log(Level.INFO, sender.getName() + " issued the command: " + command.getName());
        for (String arg : args) {
            getLogger().log(Level.INFO, arg);
        }
        return true;
    }

    public HashMap<UUID, ArmorStand> getSittingPlayers() {
        return sittingPlayers;
    }

    public HashMap<UUID, Boolean> getOnePunchPlayers() {
        return onePunchPlayers;
    }

    public HashMap<UUID, Boolean> getTrappedPlayers() {
        return trappedPlayers;
    }

    public String getPluginPrefix() {
        return PLUGIN_PREFIX;
    }

    private void cleanupAllHashes() {
        for (ArmorStand stand : sittingPlayers.values()) {
            stand.remove();
        }
        sittingPlayers.clear();
        trappedPlayers.clear();
        onePunchPlayers.clear();
        getLogger().info("[LukesWhimsy] Removed all sitting players' armor stands.");
        getLogger().info ("[LukesWhimsy] Cleared all hashmaps.");
    }


}
