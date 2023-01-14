package me.bukkit.teaming;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.*;

public class Manager implements Listener {

    private final Main plugin = Main.getPlugin(Main.class);
    private final FileConfiguration cfg = plugin.getConfig();
    // on: true, off: false
    private Boolean state = false;
    private Map<UUID, List<UUID>> closePlayers = new HashMap<>();
    private Map<UUID, Integer> damageLoopIDs = new HashMap<>();
    private int taskID, damageLoopID, instanceID;
    boolean found;

    public String toggle(Boolean state) {
        if (this.state.equals(state)) {
            return plugin.redpref + "Anti Teaming plugin is already " + plugin.getToggleMap().get(state);
        } else {
            toggleLoop(state);
            this.state = state;
            return plugin.greenpref + "Anti Teaming plugin is now " + plugin.getToggleMap().get(state);
        }
    }

    public void toggleLoop(Boolean state) {
        int maxPlayers = 0;
        int maxDist = 4;
        List<UUID> nearby = new ArrayList<>();
        if (state) {
            // each group has 1 player that manages the whole nearby group
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                public void run() {
                    nearby.clear();
                    found = false;
                    Bukkit.getServer().getScheduler().cancelTask(instanceID);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        for (Player other : Bukkit.getOnlinePlayers()) {
                            if (other.getLocation().distance(p.getLocation()) <= maxDist && !p.equals(other)) {
                                for (List<UUID> l : closePlayers.values()) {
                                    if (l.contains(p.getUniqueId())) {
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    if (!nearby.contains(other.getUniqueId())) nearby.add(other.getUniqueId());
                                    if (closePlayers.get(p.getUniqueId()).size() > maxPlayers) {
                                        instanceID = toggleDamageLoop(p.getUniqueId());
                                        p.sendMessage(plugin.warningpref + ChatColor.RED + ChatColor.BOLD + "PvP" + ChatColor.RESET + " or you will start "  + ChatColor.RED + ChatColor.BOLD + "taking damage" + ChatColor.RESET + "!");
                                    }
                                }
                                closePlayers.put(p.getUniqueId(), nearby);
                            }
                        }
                    }
                }
            }, 0, 100); //change to every 10 secs
        } else {
            Bukkit.getServer().getScheduler().cancelTasks(plugin);
        }
    }

    public int toggleDamageLoop(UUID id) {
        if(state) {
            damageLoopID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                public void run() {
                    try {
                        if (!closePlayers.get(id).isEmpty()) Bukkit.getPlayer(id).damage(1);
                        for (UUID closeID : closePlayers.get(id)) {
                            Bukkit.getPlayer(closeID).damage(1);
                        }
                    } catch (Error e) {
                        Bukkit.getPlayer(id).sendMessage("lmao error");
                    }
                }
            }, 35, 15);
            damageLoopIDs.put(id, damageLoopID);
        }
        return damageLoopID;
    }
    public void cancelDamageLoop(UUID id) {
        if (!getClosePlayers().get(id).isEmpty()) {
            try {
                if (getDamageLoopIDs().containsKey(id)) Bukkit.getServer().getScheduler().cancelTask(getDamageLoopIDs().get(id));
            } catch (Error error) {
                error.printStackTrace();
            }
        }
    }
    public Boolean getState() {
        return state;
    }

    public Map<UUID, List<UUID>> getClosePlayers() {
        return closePlayers;
    }

    public Map<UUID, Integer> getDamageLoopIDs() {
        return damageLoopIDs;
    }
}