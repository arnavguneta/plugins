package me.bukkit.survival.pvp.PvP;

import me.bukkit.survival.Main;
import me.bukkit.survival.pvp.Battle;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings({ "ALL", "WeakerAccess" }) public class PvPMechanics implements Listener {

	private Main plugin = Main.getPlugin(Main.class);

	@EventHandler public void onBreak(BlockBreakEvent event) {
		if (plugin.pmanager.isInBattle(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Player winner = event.getEntity().getKiller();

		if (plugin.pmanager.isInBattle(player)) {
			Battle b = plugin.pmanager.getBattle(player);

			plugin.pmanager.endBattle(b, winner.getName());
			event.getDrops().clear();
		}

	}

	@EventHandler public void onLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		if (plugin.pmanager.isInBattle(player)) {
			Battle b = plugin.pmanager.getBattle(player);

			if (player.getDisplayName().equalsIgnoreCase(b.getPlayer1())) {
				plugin.pmanager.endBattle(b, b.getPlayer2());
			} else if (player.getDisplayName().equalsIgnoreCase(b.getPlayer2())) {
				plugin.pmanager.endBattle(b, b.getPlayer1());
			}
		}
	}

	@EventHandler public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (plugin.pmanager.isInBattle(player)) {
			Battle b = plugin.pmanager.getBattle(player);

			if (player.getDisplayName().equalsIgnoreCase(b.getPlayer1())) {
				plugin.pmanager.endBattle(b, b.getPlayer2());
			} else if (player.getDisplayName().equalsIgnoreCase(b.getPlayer2())) {
				plugin.pmanager.endBattle(b, b.getPlayer1());
			}
		}
	}

	@EventHandler public void onMove(PlayerMoveEvent event) {
		String moverName = event.getPlayer().getName();

        for (String pName : plugin.pmanager.waiting) {
        	if (moverName.equalsIgnoreCase(pName)) {
				event.setCancelled(true);
			}
		}


	}


		@EventHandler public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager().getType() != EntityType.PLAYER)
			return;
		if (event.getEntity().getType() != EntityType.PLAYER)
			return;
		//both players

		Player damaged = (Player) event.getEntity();
		Player damager = (Player) event.getDamager();

		if (plugin.pmanager.isInBattle(damager) || plugin.pmanager.isInBattle(damaged)) {
			if (plugin.pmanager.isInBattle(damager) && plugin.pmanager.isInBattle(damaged)) {
				if (!plugin.pmanager.getBattle(damager).equals(plugin.pmanager.getBattle(damaged))) {
					event.setCancelled(true);
					damager.sendMessage(plugin.pvpred + "You cannot damage this person!");
				} else {
					event.setCancelled(false);
				}
			} else {
				event.setCancelled(true);
				damager.sendMessage(plugin.pvpred + "You cannot damage this person!");
			}

		}

	}

	@EventHandler public void onCommand(PlayerCommandPreprocessEvent event) {
		Player p = event.getPlayer();

		if (plugin.pmanager.isInBattle(p)) {
			String command[] = event.getMessage().split(" ");
			if (command[0].equalsIgnoreCase("/1v1")) {
				if (command[1].equalsIgnoreCase("quit")) {
					return;
				} else {
					event.setCancelled(true);
					p.sendMessage(plugin.pvpred + "You are in a 1v1, type " + ChatColor.YELLOW + "/1v1 quit"
							+ ChatColor.WHITE + " to leave!");
				}
			} else {
				event.setCancelled(true);
				p.sendMessage(
						plugin.pvpred + "You are in a 1v1, type " + ChatColor.YELLOW + "/1v1 quit" + ChatColor.WHITE
								+ " to leave!");

			}

		}
	}

}
