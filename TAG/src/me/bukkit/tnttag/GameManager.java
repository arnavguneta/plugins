package me.bukkit.tnttag;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class GameManager implements Listener {

	private MainClass plugin = MainClass.getPlugin(MainClass.class);

	private int lobbyCountdown = 11;
	public int explosionCountdown = 31;
	public int playersNeeded = 2;
	private boolean isStarted;

	Location lobbySpawn;
	Location gameSpawn;

	public void setupGame() {
		if (plugin.getConfig().contains("GameSpawn")) {
			this.gameSpawn = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("GameSpawn.world")),
					plugin.getConfig().getDouble("GameSpawn.X"), plugin.getConfig().getDouble("GameSpawn.Y"),
					plugin.getConfig().getDouble("GameSpawn.Z"), (float) plugin.getConfig().getDouble("GameSpawn.yaw"),
					(float) plugin.getConfig().getDouble("GameSpawn.pitch"));
			plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Game Spawn Located");
		}

		if (plugin.getConfig().contains("LobbySpawn")) {
			this.lobbySpawn = new Location(
					Bukkit.getServer().getWorld(plugin.getConfig().getString("LobbySpawn.world")),
					plugin.getConfig().getDouble("LobbySpawn.X"), plugin.getConfig().getDouble("LobbySpawn.Y"),
					plugin.getConfig().getDouble("LobbySpawn.Z"),
					(float) plugin.getConfig().getDouble("LobbySpawn.yaw"),
					(float) plugin.getConfig().getDouble("LobbySpawn.pitch"));
			plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Lobby Spawn Located");
		}

		for (Player online : Bukkit.getOnlinePlayers()) {
			plugin.playersInGame.add(online);
			plugin.playermanager.put(online.getUniqueId(),
					new PlayerManager(online.getUniqueId(), false, 0, false, false));

			online.setFoodLevel(20);
			online.setHealth(20);
			online.setGameMode(GameMode.ADVENTURE);

			playerScoreboard.scoreLobby(online);

			online.getInventory().clear();
			online.getInventory().setHelmet(null);
			playerScoreboard.scoreLobby(online);
			online.teleport(lobbySpawn);
		}
		lobbyCountdown = 11;
		explosionCountdown = 31;
		lobbyWait();
	}

	public void lobbyWait() {

		int online = Bukkit.getOnlinePlayers().size();
		Bukkit.getServer().broadcastMessage(
				plugin.yellowpref + " There are §7(§f" + online + "§7/§f" + playersNeeded + "§7)§f players online");
		playerCheck(online);
	}

	public void gameStart() {
		explosionCountdown();
		plugin.gameMechanics.tntPlacer();

		for (Player player : Bukkit.getOnlinePlayers()) {
			player.setWalkSpeed(.3f);
			player.teleport(gameSpawn);
			player.getInventory().clear();
		}

	}

	public void gameStop(Player player) {
		// implement bungeecord
		player.setWalkSpeed(.2f);
		for (Player p : Bukkit.getOnlinePlayers()) {
			playerScoreboard.scoreLobby(p);
		}
		player.getInventory().setHelmet(null);
		player.getInventory().clear();

		player.setGameMode(GameMode.ADVENTURE);

		plugin.playersInGame.clear();
		plugin.playermanager.clear();
		plugin.playersLeftGame.clear();


		player.setPlayerListName(ChatColor.WHITE + player.getName());
		setStarted(false);

	}

	public boolean playerCheck(int online) {
		if (online >= playersNeeded) {
			if (isStarted == false) {
				lobbyCountdown();
				setStarted(true);
			}
			return true;
		} else {
			return false;
		}
	}

	public void explosionCountdown() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (explosionCountdown > 1) {
					explosionCountdown = explosionCountdown - 1;

					for (Player player : Bukkit.getOnlinePlayers()) {
						playerScoreboard.scoreGame(player, explosionCountdown);
					}

				} else {

					if (plugin.playersInGame.size() <= 1) {
						Bukkit.getServer().broadcastMessage(plugin.redpref + "Game has been stopped.");
						for (Player online : Bukkit.getOnlinePlayers()) {
							gameStop(online);
						}
						this.cancel();
						return;
					} else {
						explosionCountdown = 31;
						plugin.gameMechanics.tntCheck();

						if (plugin.playersInGame.size() <= 1) {
							Bukkit.getServer().broadcastMessage(plugin.greenpref + ChatColor.GOLD
									+ plugin.playersInGame.get(0).getName() + ChatColor.WHITE + " has won the game!");
							Bukkit.getServer().broadcastMessage(plugin.greenpref + "Type " + ChatColor.GOLD
									+ "/game start" + ChatColor.WHITE + " to start a new game.");
							for (Player online : Bukkit.getOnlinePlayers()) {
								gameStop(online);
							}
							this.cancel();
							return;
						} else {
							Bukkit.getServer().broadcastMessage(plugin.redpref
									+ "BOOM! Players have been eliminated. §cTagger§f has been selected");
							plugin.gameMechanics.tntPlacer();
						}
					}
				}
			}

		}.runTaskTimerAsynchronously(plugin, 0, 20l);

	}

	public void lobbyCountdown() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (lobbyCountdown > 1) {
					playerCheck(plugin.playersInGame.size());

					if (playerCheck(plugin.playersInGame.size()) == true) {
						lobbyCountdown = lobbyCountdown - 1;
						Bukkit.getServer().broadcastMessage(plugin.greenpref + "Game starting in " + ChatColor.GREEN
								+ lobbyCountdown + ChatColor.WHITE + " seconds");
						for (Player online : Bukkit.getOnlinePlayers()) {
							online.playSound(online.getLocation(), Sound.NOTE_PLING, 2, 2);
						}
					} else {
						Bukkit.getServer().broadcastMessage(plugin.redpref + "Game has been stopped.");
						this.cancel();
						lobbyCountdown = 11;

					}

				} else {
					this.cancel();
					gameStart();

					Bukkit.getServer().broadcastMessage(plugin.greenpref + "Game has started! Good luck.");

				}

			}
		}.runTaskTimerAsynchronously(plugin, 20 * 3, 20l);
	}

	public boolean isStarted() {
		return isStarted;
	}

	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}

}
