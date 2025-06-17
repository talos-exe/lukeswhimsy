package me.nohbdy.lukesWhimsy;
import me.nohbdy.lukesWhimsy.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.logging.Level;


public final class LukesWhimsy extends JavaPlugin implements Listener {

    private DataManager dataManager;
    private HomeDataManager homeDataManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("ver 1.4.3 enabled.");
        getLogger().info("use /lwhelp to get started!");

        // Create manager objects
        dataManager = new DataManager(this);
        homeDataManager = new HomeDataManager(this);

        // Load commands
        Objects.requireNonNull(getCommand("smite")).setExecutor(new SmiteCommand(this));
        Objects.requireNonNull(getCommand("sit")).setExecutor(new SitCommand(this));
        Objects.requireNonNull(getCommand("fling")).setExecutor(new FlingCommand(this));
        Objects.requireNonNull(getCommand("onepunchman")).setExecutor(new OnePunchCommand(this));
        Objects.requireNonNull(getCommand("catapult")).setExecutor(new CatapultCommand(this));
        Objects.requireNonNull(getCommand("feed")).setExecutor(new FeedCommand(this));
        Objects.requireNonNull(getCommand("heal")).setExecutor(new HealCommand(this));
        Objects.requireNonNull(getCommand("fly")).setExecutor(new FlyCommand(this));
        Objects.requireNonNull(getCommand("lwhelp")).setExecutor(new HelpCommand(this));
        Objects.requireNonNull(getCommand("hng")).setExecutor(new VillagerSoundCommand(this));
        Objects.requireNonNull(getCommand("babytrap")).setExecutor(new BabyTrapCommand(this));
        Objects.requireNonNull(getCommand("rtp")).setExecutor(new RandomTeleportCommand(this));
        Objects.requireNonNull(getCommand("back")).setExecutor(new BackCommand(this));
        Objects.requireNonNull(getCommand("top")).setExecutor(new TopCommand(this));
        Objects.requireNonNull(getCommand("freeze")).setExecutor(new FreezeCommand(this));
        Objects.requireNonNull(getCommand("loudmouth")).setExecutor(new LoudmouthCommand(this));
        //getCommand("home").setExecutor(new HomeCommand(this));

        // Load data from file
        homeDataManager.loadHomes();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("ver 1.4.3 disabled.");
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
