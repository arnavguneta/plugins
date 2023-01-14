package me.bukkit.events;

import me.bukkit.util.Util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();
		e.setJoinMessage(
				ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.WHITE + p.getName());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {

		Player p = e.getPlayer();
		e.setQuitMessage(
				ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "] " + ChatColor.WHITE + p.getName());
	}

	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		ChatColor color = Util.getColor(player);
		event.setFormat(color + "" + ChatColor.BOLD + "%1$s" + ChatColor.WHITE + ": " + "%2$s");
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {

		if (e.getEntity() instanceof Player) {
			Player p = e.getEntity().getPlayer();
			Player pKiller = p.getKiller().getPlayer();

			if (pKiller instanceof Player) {

				long pKillerHealth = Math.round(pKiller.getHealth());

				e.setDeathMessage(null);
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "Death" + ChatColor.WHITE
						+ "] " + ChatColor.RED + pKiller.getName() + "" + ChatColor.WHITE + "" + " killed "
						+ ChatColor.GREEN + "" + p.getName() + ChatColor.WHITE + " with " + ChatColor.RED + ""
						+ ChatColor.BOLD + "" + pKillerHealth + ChatColor.WHITE + " hearts");

				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "Death" + ChatColor.WHITE
						+ "] " + "-200 Coins upon player death!");

				pKiller.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "Kill"
						+ ChatColor.WHITE + "] " + ChatColor.RED + "" + pKiller.getName() + "" + ChatColor.WHITE + ""
						+ " killed " + ChatColor.GREEN + "" + p.getName() + ChatColor.WHITE + " with " + ChatColor.RED
						+ "" + ChatColor.BOLD + "" + pKillerHealth + ChatColor.WHITE + " hearts");
				pKiller.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "Kill"
						+ ChatColor.WHITE + "] " + "" + "+200 Coins upon player kill!");

			}

		}

	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();

		if (p.isOp()) {
			return;
		} else {
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();

		if (p.isOp()) {
			return;
		} else {
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void onFall(final EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}

		if (e.getCause() == DamageCause.FALL) {
			e.setCancelled(true);
		}
	}

}
