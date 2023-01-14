package me.bukkit.bedwars;

import me.bukkit.bedwars.PlayerManager.PlayerManager;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by arnavguneta.
 */
public class Mechanics implements Listener {

	private Main plugin = Main.getPlugin(Main.class);
	private FileConfiguration cfg = plugin.getConfig();
	int diamondct = 1;
	int emeraldct = 1;

	@EventHandler public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		ChatColor color = getColor(player);
		e.setJoinMessage(plugin.yellow + player.getName() + " has joined the game.");
		plugin.manager.setupScoreboard(player);
		PlayerManager pm = new PlayerManager(player, false,false, true);
		plugin.playermanager.put(pm.getUuid(), pm);

		if (player.getDisplayName().equalsIgnoreCase("abguneta") || player.getDisplayName().equalsIgnoreCase("coolkidarnie")) {
			player.setPlayerListName(color + "" + ChatColor.BOLD + "OP " + ChatColor.YELLOW + player.getDisplayName() + ChatColor.WHITE);
		} else {
			player.setPlayerListName(
					color + "" + ChatColor.BOLD + "MEMBER " + color + player.getDisplayName() + ChatColor.WHITE);
		}

		new BukkitRunnable() {
			@Override public void run() {
				plugin.manager.setWorldMode(player);
			}
		}.runTaskLaterAsynchronously(plugin, 5);

	}

	@EventHandler public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		e.setQuitMessage(
				plugin.yellow + ChatColor.BOLD + player.getName() + ChatColor.WHITE + " has left the game.");

	}

	@EventHandler public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		PlayerManager pm = plugin.playermanager.get(player.getUniqueId());
		ChatColor color = getColor(player);

		if (pm.getSetupMode()) {
			plugin.manager.response = event.getMessage();
			plugin.manager.setupOptions(player);
			event.setCancelled(true);
		}

		if (player.getDisplayName().equalsIgnoreCase("coolkidarnie") || player.getDisplayName()
				.equalsIgnoreCase("abguneta")) {
			event.setFormat(color + "" + ChatColor.BOLD + "OP " + ChatColor.YELLOW + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
		} else {
			event.setFormat(color + "" + ChatColor.BOLD + "MEMBER " + color + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
		}
	}

	public ChatColor getColor(Player player) {
		if (player.getName().equals("abguneta")) {
			return ChatColor.RED;
		} else if (player.getName().equals("coolkidarnie")) {
			return ChatColor.RED;
		} else {
			return ChatColor.YELLOW;
		}
	}

	public void logLocations(int team, String type, Location location, Player player) {
		cfg.set("gens." + "" + type + "_" + team + ".world", player.getLocation().getWorld().getName());
		cfg.set("gens." + "" + type + "_" + team + ".X", player.getLocation().getX());
		cfg.set("gens." + "" + type + "_" + team + ".Y", player.getLocation().getY());
		cfg.set("gens." + "" + type + "_" + team + ".Z", player.getLocation().getZ());
		cfg.set("gens." + "" + type + "_" + team + ".yaw", player.getLocation().getYaw());
		cfg.set("gens." + "" + type + "_" + team + ".pitch", player.getLocation().getPitch());
		plugin.saveConfig();
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		Location location = event.getClickedBlock().getLocation();
		Material mat = null;

		if (action.equals(Action.LEFT_CLICK_BLOCK)) {
			if (!plugin.manager.genSetup)
				return;
			event.setCancelled(true);
			if (plugin.manager.response.equalsIgnoreCase("iron")) {
				event.getClickedBlock().getLocation().getBlock().setType(Material.IRON_BLOCK);
				mat = Material.IRON_INGOT;
			} else if (plugin.manager.response.equalsIgnoreCase("gold")) {
				event.getClickedBlock().getLocation().getBlock().setType(Material.GOLD_BLOCK);
				mat = Material.GOLD_INGOT;
			} else if (plugin.manager.response.equalsIgnoreCase("diamond")) {
				event.getClickedBlock().getLocation().getBlock().setType(Material.DIAMOND_BLOCK);
				mat = Material.DIAMOND;
				logLocations(diamondct, "diamond", location, player);
				diamondct++;
			} else if (plugin.manager.response.equalsIgnoreCase("emerald")) {
				event.getClickedBlock().getLocation().getBlock().setType(Material.EMERALD_BLOCK);
				mat = Material.EMERALD;
				logLocations(emeraldct, "emerald", location, player);
				emeraldct++;
			}
			plugin.manager.genSetup = false;

			Generator generator = new Generator(event.getClickedBlock().getLocation(), mat);
			//todo serialize location

			player.sendMessage("\n");
			player.sendMessage(plugin.green + "Generator has been setup.");
			player.sendMessage("\n");
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation();
		PlayerManager pm;

		for (Bed bed : plugin.beds) {
			if (bed == null) {
				return;
			}
			if (!location.equals(bed.getHeadLocation()) && !location.equals(bed.getFootLocation()) ) {
				return;
			}

			if (bed.getPlayer().equals(player)) {
				player.sendMessage(plugin.red + "You can't break your own bed!");
				event.setCancelled(true);
				return;
			} else {
				pm = plugin.playermanager.get(bed.getPlayer().getUniqueId());
				Bukkit.broadcastMessage(
						plugin.red + bed.getPlayer().getName() + "'s bed was destroyed by " + player.getName() + "!");
				plugin.beds.remove(bed);
				pm.setCanRespawn(false);
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		PlayerManager pm = plugin.playermanager.get(player.getUniqueId());

		if (pm.getCanRespawn()) {
			event.setDeathMessage(plugin.red + event.getDeathMessage());
			return;
		}
		Bukkit.broadcastMessage(plugin.red + player.getName() + " has been eliminated!");
		player.setGameMode(GameMode.SPECTATOR);

		event.setDeathMessage("");
	}

}
