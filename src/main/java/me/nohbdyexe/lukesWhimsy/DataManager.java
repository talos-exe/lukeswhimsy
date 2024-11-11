package me.nohbdyexe.lukesWhimsy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {

    private final LukesWhimsy plugin;
    private final HashMap<UUID, Map<String,Location>> homesLocations; // Store homes for each player
    private HashMap<UUID, ArmorStand> sittingPlayers; // Store sitting players
    private HashMap<UUID, Boolean> onePunchPlayers; //Store onepunched players
    private HashMap<UUID, Boolean> trappedPlayers; //Store babytrapped players
    private HashMap<UUID, Location> rtpLastLocation; // Store lastrtplocation
    private HashMap<UUID, Boolean> frozenPlayers; //Store frozen players
    private final String PLUGIN_PREFIX = ChatColor.BLUE + "[LukesWhimsy] " + ChatColor.RESET;

    public DataManager(LukesWhimsy plugin) {
        this.plugin = plugin;
        this.homesLocations = new HashMap<>();
        this.sittingPlayers = new HashMap<>();
        this.onePunchPlayers = new HashMap<>();
        this.trappedPlayers = new HashMap<>();
        this.rtpLastLocation = new HashMap<>();
        this.frozenPlayers = new HashMap<>();
    }


    public HashMap<UUID, ArmorStand> getSittingPlayers() {
        return sittingPlayers;
    }

    public HashMap<UUID, Boolean> getOnePunchPlayers() {
        return onePunchPlayers;
    }

    public HashMap<UUID, Boolean> getFrozenPlayers() {
        return frozenPlayers;
    }

    public HashMap<UUID, Boolean> getTrappedPlayers() {
        return trappedPlayers;
    }

    public HashMap<UUID, Location> getRtpLastLocation() {
        return rtpLastLocation;
    }

    public HashMap<UUID, Map<String, Location>> getHomesLocations() {
        return homesLocations;
    }

    public String getPluginPrefix() {
        return PLUGIN_PREFIX;
    }

}
