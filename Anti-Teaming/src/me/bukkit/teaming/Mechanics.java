package me.bukkit.teaming;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class Mechanics implements Listener {

    private final Main plugin = Main.getPlugin(Main.class);
    private final FileConfiguration cfg = plugin.yaml.cfg;
// TODO onDeath clear nearby

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        // on death: if player had nearby neighbors, remove their list and cancel damage. if player was in someones nearby, update that list.

        if (plugin.manager.getState()) {
            Player dead = e.getEntity();
//            e.getEntity().sendMessage(e.getEntity().getLastDamageCause().getCause().toString());
            if (dead.getKiller() instanceof Player) {
                if (plugin.manager.getClosePlayers().containsKey(dead.getKiller().getUniqueId())) {
                    plugin.manager.cancelDamageLoop(dead.getKiller().getUniqueId());
                    plugin.manager.getClosePlayers().remove(dead.getKiller().getUniqueId());
                } else if (plugin.manager.getClosePlayers().containsKey(dead.getUniqueId())) {
                    plugin.manager.cancelDamageLoop(dead.getUniqueId());
                    plugin.manager.getClosePlayers().remove(dead.getUniqueId());
                }
            } else {
                if (plugin.manager.getClosePlayers().containsKey(dead.getUniqueId())) {
                    plugin.manager.cancelDamageLoop(dead.getUniqueId());
                    plugin.manager.getClosePlayers().remove(dead.getUniqueId());
                }
                // find if dead player is in anyone's nearby list
//                outer:
//                for (Player p : Bukkit.getOnlinePlayers()) {
//                    if (plugin.manager.getClosePlayers().containsKey(p.getUniqueId())) {
//                        for (UUID id : plugin.manager.getClosePlayers().get(p.getUniqueId())) {
//                            if (dead.getUniqueId().equals(id)) {
//                                plugin.manager.cancelDamageLoop(p.getUniqueId());
//                                plugin.manager.getClosePlayers().remove(p.getUniqueId());
//                                break outer;
//                            }
//                        }
//                    }
//                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (plugin.manager.getState()) {
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                if (plugin.manager.getClosePlayers().containsKey(e.getDamager().getUniqueId())) {
                    plugin.manager.cancelDamageLoop(e.getDamager().getUniqueId());
                } else if (plugin.manager.getClosePlayers().containsKey(e.getEntity().getUniqueId())) {
                    plugin.manager.cancelDamageLoop(e.getEntity().getUniqueId());
                }
            }
        }
    }
}


