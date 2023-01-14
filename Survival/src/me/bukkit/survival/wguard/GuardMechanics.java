package me.bukkit.survival.wguard;

import me.bukkit.survival.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

/**
 * Created by arnavguneta.
 */
@SuppressWarnings("ALL") public class GuardMechanics implements Listener {

	private Main plugin = Main.getPlugin(Main.class);
	private FileConfiguration guard = plugin.guard.cfg;

	@EventHandler public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		plugin.gmanager.wandMode.put(player.getName(), false);
		plugin.gmanager.protectedBlocks.put(player.getName(), new ArrayList<Location>());

	}

	@EventHandler public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();

		if (plugin.gmanager.wandMode.get(player.getName()) == true) {
			plugin.gmanager.message = event.getMessage();
			event.setCancelled(true);
			plugin.gmanager.setUpWand(player);
		}
	}

	@EventHandler public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Location loc = event.getBlock().getLocation();

		for (Player p : Bukkit.getOnlinePlayers()) {
			ArrayList<Location> temp = plugin.gmanager.protectedBlocks.get(p.getName());

			if (p.getName().equalsIgnoreCase(player.getName())) {
				if (temp.contains(loc)) {
					event.setCancelled(false);
				}
			} else {
				if (temp.contains(loc)) {
					player.sendMessage(plugin.guardred + "Area is protected.");
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Location loc = event.getBlock().getLocation();

		for (Player p : Bukkit.getOnlinePlayers()) {
			ArrayList<Location> temp = plugin.gmanager.protectedBlocks.get(p.getName());

			if (p.getName().equalsIgnoreCase(player.getName())) {
				if (temp.contains(loc)) {
					event.setCancelled(false);
				}
			} else {
				if (temp.contains(loc)) {
					player.sendMessage(plugin.guardred + "Area is protected.");
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (player.getItemInHand().getType() != Material.getMaterial(plugin.gmanager.getWand(player)) && (
				event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)) {

			Location loc = event.getClickedBlock().getLocation();

			for (Player p : Bukkit.getOnlinePlayers()) {
				ArrayList<Location> temp = plugin.gmanager.protectedBlocks.get(p.getName());

				if (p.getName().equalsIgnoreCase(player.getName())) {
					if (temp.contains(loc)) {
						event.setCancelled(false);
					}
				} else {
					if (temp.contains(loc)) {
						//if ()
						player.sendMessage(plugin.guardred + "Area is protected.");
						event.setCancelled(true);
					}
				}
			}
			return;
		}
		if (player.getItemInHand().hasItemMeta()) {

			if (player.getItemInHand().getItemMeta().getDisplayName()
					.equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "Guard Wand")) {

				event.setCancelled(true);
				Location loc = event.getClickedBlock().getLocation();

				for (Player p : Bukkit.getOnlinePlayers()) {
					ArrayList<Location> temp = plugin.gmanager.protectedBlocks.get(p.getName());
					ArrayList<Location> temp1 = plugin.gmanager.protectedBlocks.get(player.getName());

					if (temp.contains(loc) || temp1.contains(loc)) {
						player.sendMessage(plugin.guardred + "Area is already protected.");
						return;
					}
				}

				int x = event.getClickedBlock().getLocation().getBlockX();
				int y = event.getClickedBlock().getLocation().getBlockY();
				int z = event.getClickedBlock().getLocation().getBlockZ();

				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					// left pos
					plugin.gmanager.pos1.put(player.getName(), event.getClickedBlock().getLocation());
					player.sendMessage(
							plugin.guardgreen + "First position set to (" + ChatColor.YELLOW + x + ChatColor.WHITE
									+ ", " + ChatColor.YELLOW + y + ChatColor.WHITE + ", " + ChatColor.YELLOW + z
									+ ChatColor.WHITE + ").");
					plugin.gmanager.setupLocations(player);
				} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					// right pos
					plugin.gmanager.pos2.put(player.getName(), event.getClickedBlock().getLocation());
					player.sendMessage(
							plugin.guardgreen + "Second position set to (" + ChatColor.YELLOW + x + ChatColor.WHITE
									+ ", " + ChatColor.YELLOW + y + ChatColor.WHITE + ", " + ChatColor.YELLOW + z
									+ ChatColor.WHITE + ").");
					plugin.gmanager.setupLocations(player);
				}

			}
		}

	}
}
