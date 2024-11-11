package me.nohbdyexe.lukesWhimsy;
import me.nohbdyexe.lukesWhimsy.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;


public final class LukesWhimsy extends JavaPlugin implements @NotNull Listener {

    private DataManager dataManager;
    private HomeDataManager homeDataManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("ver 1.3 enabled.");
        getLogger().info("use /whimsyhelp to get started!");

        // Create manager objects
        dataManager = new DataManager(this);
        homeDataManager = new HomeDataManager(this);

        // Load commands
        getCommand("smite").setExecutor(new SmiteCommand(this));
        getCommand("sit").setExecutor(new SitCommand(this));
        getCommand("fling").setExecutor(new FlingCommand(this));
        getCommand("onepunchman").setExecutor(new OnePunchCommand(this));
        getCommand("catapult").setExecutor(new CatapultCommand(this));
        getCommand("feed").setExecutor(new FeedCommand(this));
        getCommand("heal").setExecutor(new HealCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("lwhelp").setExecutor(new HelpCommand(this));
        getCommand("hng").setExecutor(new VillagerSoundCommand(this));
        getCommand("babytrap").setExecutor(new BabyTrapCommand(this));
        getCommand("rtp").setExecutor(new RandomTeleportCommand(this));
        getCommand("back").setExecutor(new BackCommand(this));
        getCommand("top").setExecutor(new TopCommand(this));
        getCommand("freeze").setExecutor(new FreezeCommand(this));
        getCommand("loudmouth").setExecutor(new LoudmouthCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));

        // Load data from file
        homeDataManager.loadHomes();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("ver 1.3 disabled.");
        cleanupAllHashes();
        homeDataManager.saveHomes();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        getLogger().log(Level.INFO, sender.getName() + " issued the command: " + command.getName());
        for (String arg : args) {
            getLogger().log(Level.INFO, arg);
        }
        return true;
    }

    private void cleanupAllHashes() {
        for (ArmorStand stand : dataManager.getSittingPlayers().values()) {
            stand.remove();
        }
        dataManager.getSittingPlayers().clear();
        dataManager.getTrappedPlayers().clear();
        dataManager.getFrozenPlayers().clear();
        dataManager.getRtpLastLocation().clear();
        getLogger().info("[LukesWhimsy] Removed all sitting players' armor stands.");
        getLogger().info("[LukesWhimsy] Cleared all hashmaps.");


    }
}
