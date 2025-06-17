package me.nohbdy.lukesWhimsy;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.RESET;

public class DataManager {

    private final HashMap<UUID, Map<String,Location>> homesLocations; // Store homes for each player
    private final HashMap<UUID, ArmorStand> sittingPlayers; // Store sitting players
    private final HashMap<UUID, Boolean> onePunchPlayers; //Store onepunched players
    private final HashMap<UUID, Boolean> trappedPlayers; //Store babytrapped players
    private final HashMap<UUID, Location> rtpLastLocation; // Store lastrtplocation
    private final HashMap<UUID, Boolean> frozenPlayers; //Store frozen players
    private final String PLUGIN_PREFIX = BLUE + "[LukesWhimsy] " + RESET;

    public DataManager(LukesWhimsy plugin) {
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
