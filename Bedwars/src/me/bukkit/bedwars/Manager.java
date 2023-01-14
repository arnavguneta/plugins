package me.bukkit.bedwars;

import me.bukkit.bedwars.PlayerManager.PlayerManager;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

/**
 * Created by arnavguneta.
 */
public class Manager {

	public String response;
	Boolean optSelected = false;
	Boolean genSetup = false;
	Boolean started = false;
	private Main plugin = Main.getPlugin(Main.class);
	private FileConfiguration cfg = plugin.getConfig();
	private int count = 5;
	private int gameMinutes = 10;
	private int gameSeconds = 0;

	public void setupScoreboard(Player player) {
		PlayerManager pmanager = plugin.playermanager.get(player.getUniqueId());
		ScoreboardManager manager = getServer().getScoreboardManager();
		final Scoreboard b = manager.getNewScoreboard();
		final Objective o = b.registerNewObjective("score", "");

		o.setDisplaySlot(DisplaySlot.SIDEBAR);

		o.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Bedwars");

		Score space2 = o.getScore(ChatColor.RED + "");
		space2.setScore(6);

		Score name = o.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Player: ");
		name.setScore(5);

		Score nameVal = o.getScore(ChatColor.WHITE + player.getName());
		nameVal.setScore(4);

		Score space = o.getScore(ChatColor.WHITE + "");
		space.setScore(3);

		if (pmanager.getIngame() && started) {
			Score bal = o.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Time: "); //10mins
			bal.setScore(2);

			if (gameSeconds < 10) {
				Score balAmount = o.getScore(ChatColor.WHITE + "" + getGameMinutes() + ":0" + getGameSeconds());
				balAmount.setScore(1);
			} else {
				Score balAmount = o.getScore(ChatColor.WHITE + "" + getGameMinutes() + ":" + getGameSeconds());
				balAmount.setScore(1);
			}

		} else if (getCount() != 5) {
			Score bal = o.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "State: "); //10mins
			bal.setScore(2);

			Score balAmount = o.getScore(ChatColor.WHITE + "Starting...");
			balAmount.setScore(1);
		} else {
			Score bal = o.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "State: "); //10mins
			bal.setScore(2);

			Score balAmount = o.getScore(ChatColor.WHITE + "Waiting...");
			balAmount.setScore(1);
		}

		Score space1 = o.getScore("");
		space1.setScore(0);

		player.setScoreboard(b);
	}

	public void setWorldMode(Player player) {
		new BukkitRunnable() {
			@Override public void run() {
				if (player.getLocation().getWorld().getName().equalsIgnoreCase("world")) {
					player.setGameMode(GameMode.CREATIVE);
				} else {
					player.setGameMode(GameMode.SURVIVAL);
				}
			}
		}.runTaskLaterAsynchronously(plugin, 10);
	}

	public void setupPlayers() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			PlayerManager pm = new PlayerManager(player, false, false, true);
			plugin.playermanager.put(pm.getUuid(), pm);
		}
	}

	public void setupOptions(Player player) {
		PlayerManager pm = plugin.playermanager.get(player.getUniqueId());

		if (response.equalsIgnoreCase("beds")) {
			Location plocation = player.getLocation();
			Location location = new Location(player.getWorld(), plocation.getBlockX(), plocation.getBlockY(),
					plocation.getBlockZ());

			Bed bed = new Bed(player, location);
			plugin.beds.add(bed);

			player.sendMessage("\n");
			player.sendMessage(plugin.green + "Your bed has been setup.");
			player.sendMessage("\n");

			optSelected = false;
			pm.setSetupMode(false);
		} else if (response.equalsIgnoreCase("generators")) {
			player.sendMessage(
					plugin.yellow + "------< " + ChatColor.BLUE + ChatColor.BOLD + "GENERATOR TYPE" + ChatColor.WHITE
							+ " >------"); //30 GENERATOR TYPE //14
			player.sendMessage(
					plugin.yellow + "Generator options (type in chat): " + ChatColor.GRAY + ChatColor.BOLD + "IRON"
							+ ChatColor.WHITE + ", " + ChatColor.GOLD + ChatColor.BOLD + "GOLD" + ChatColor.WHITE
							+ ", \n" + plugin.yellow + ChatColor.AQUA + ChatColor.BOLD + "DIAMOND" + ChatColor.WHITE
							+ ", " + ChatColor.GREEN + ChatColor.BOLD + "EMERALD" + ChatColor.WHITE + "."); //30
			player.sendMessage(plugin.yellow + "-------------<  >-------------"); //30
			optSelected = true;
		} else if (optSelected) {
			optSelected = false;
			genSetup = true;
			pm.setSetupMode(false);
			setupGenerators(player);
		} else {
			player.sendMessage(
					plugin.yellow + "--------< " + ChatColor.BLUE + ChatColor.BOLD + "SETUP MODE" + ChatColor.WHITE
							+ " >--------"); //30
			player.sendMessage(plugin.yellow + "Interrupted setup mode.");
			pm.setSetupMode(false);
			player.sendMessage(plugin.yellow + "-------------<  >-------------"); //30
		}
	}

	public void setupGenerators(Player player) {
		if (response.equalsIgnoreCase("iron")) {
			player.sendMessage(
					plugin.yellow + "---------< " + ChatColor.BLUE + ChatColor.BOLD + "GENERATOR" + ChatColor.WHITE
							+ " >---------"); //30 GENERATOR TYPE //14
			player.sendMessage(
					plugin.yellow + "Left click to select a location for " + ChatColor.GRAY + ChatColor.BOLD + "IRON "
							+ ChatColor.WHITE + "generator."); //30
			player.sendMessage(plugin.yellow + "-------------<  >-------------"); //30
		} else if (response.equalsIgnoreCase("gold")) {
			player.sendMessage(
					plugin.yellow + "---------< " + ChatColor.BLUE + ChatColor.BOLD + "GENERATOR" + ChatColor.WHITE
							+ " >---------"); //30 GENERATOR TYPE //14
			player.sendMessage(
					plugin.yellow + "Left click to select a location for " + ChatColor.GOLD + ChatColor.BOLD + "GOLD "
							+ ChatColor.WHITE + "generator."); //30
			player.sendMessage(plugin.yellow + "-------------<  >-------------"); //30
		} else if (response.equalsIgnoreCase("diamond")) {
			player.sendMessage(
					plugin.yellow + "---------< " + ChatColor.BLUE + ChatColor.BOLD + "GENERATOR" + ChatColor.WHITE
							+ " >---------"); //30 GENERATOR TYPE //14
			player.sendMessage(plugin.yellow + "Left click to select a location for " + ChatColor.AQUA + ChatColor.BOLD
					+ "DIAMOND \n" + plugin.yellow + "generator."); //30
			player.sendMessage(plugin.yellow + "-------------<  >-------------"); //30
		} else if (response.equalsIgnoreCase("emerald")) {
			player.sendMessage(
					plugin.yellow + "---------< " + ChatColor.BLUE + ChatColor.BOLD + "GENERATOR" + ChatColor.WHITE
							+ " >---------"); //30 GENERATOR TYPE //14
			player.sendMessage(plugin.yellow + "Left click to select a location for " + ChatColor.GREEN + ChatColor.BOLD
					+ "EMERALD \n" + plugin.yellow + "generator."); //30
			player.sendMessage(plugin.yellow + "-------------<  >-------------"); //30
		} else {
			player.sendMessage(
					plugin.yellow + "---------< " + ChatColor.BLUE + ChatColor.BOLD + "GENERATOR" + ChatColor.WHITE
							+ " >---------"); //30 GENERATOR TYPE //14
			player.sendMessage(plugin.yellow + "That is not an appropriate generator type."); //30
			player.sendMessage(plugin.yellow + "-------------<  >-------------"); //30
			genSetup = false;
		}

	}

	public void start() {
		if (!started)
			started = true;
		else
			return;
		new BukkitRunnable() {

			@Override public void run() {
				if (count > 0) {
					Bukkit.broadcastMessage(plugin.yellow + "Game starting in " + count + "...");
					for (Player player : Bukkit.getOnlinePlayers()) {
						setupScoreboard(player);
					}
					count--;
				} else {
					loadGenerators();
					Bukkit.broadcastMessage(plugin.green + "Game has started.");
					this.cancel();
					setCount(5);
					for (Player p : Bukkit.getOnlinePlayers()) {
						PlayerManager m = plugin.playermanager.get(p.getUniqueId());
						m.setIngame(true);
						plugin.playermanager.remove(p.getUniqueId());
						plugin.playermanager.put(p.getUniqueId(), m);
					}
					startGameCounter();
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0, 20);
	}

	public Location parseLocation(int team) {
		Location loc = new Location((getServer().getWorld(cfg.getString("team_" + team + ".forge.world"))),
				cfg.getDouble("team_" + team + ".forge.X"), cfg.getDouble("team_" + team + ".forge.Y"),
				cfg.getDouble("team_" + team + ".forge.Z"), (float) cfg.getDouble("team_" + team + ".forge.yaw"),
				(float) cfg.getDouble("team_" + team + ".forge.pitch"));
		return loc;
	}

	public void loadGenerators() {
		List<Location> genlocs = new ArrayList<>();
		for (int i = 1; i <= 8; i++) {
			if (!cfg.contains("team_" + i + ".forge.world")) {
				getServer().broadcastMessage(plugin.red + "Not all generators are set up!");
			} else {
				genlocs.add(parseLocation(i));
			}
		}

		new BukkitRunnable() {
			@Override public void run() {
				for (Location loc : genlocs) {
					getServer().getWorld("world").dropItem(loc, new ItemStack(Material.IRON_INGOT))
							.setVelocity(new Vector(0, 0, 0));
				}
				if (gameMinutes < 0) {
					this.cancel();
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0, 20 * 2);

		new BukkitRunnable() {
			@Override public void run() {
				for (Location loc : genlocs) {
					getServer().getWorld("world").dropItem(loc, new ItemStack(Material.GOLD_INGOT))
							.setVelocity(new Vector(0, 0, 0));
				}
				if (gameMinutes < 0) {
					this.cancel();
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0, 20 * 6);
	}

	public void startGameCounter() {
		new BukkitRunnable() {

			public void run() {
				if (gameSeconds == 0) {
					gameMinutes--;
					gameSeconds = 59;
				} else {
					gameSeconds--;
				}

				for (Player player : Bukkit.getOnlinePlayers()) {
					setupScoreboard(player);
				}

				if (gameMinutes < 0) {
					this.cancel();
					Bukkit.broadcastMessage(plugin.green + "Game has ended!");
					started = false;
					gameMinutes = 10;
					gameSeconds = 0;
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0, 20);
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Boolean getOptSelected() {
		return optSelected;
	}

	public void setOptSelected(Boolean optSelected) {
		this.optSelected = optSelected;
	}

	public Boolean getGenSetup() {
		return genSetup;
	}

	public void setGenSetup(Boolean genSetup) {
		this.genSetup = genSetup;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getGameMinutes() {
		return gameMinutes;
	}

	public void setGameMinutes(int gameMinutes) {
		this.gameMinutes = gameMinutes;
	}

	public int getGameSeconds() {
		return gameSeconds;
	}

	public void setGameSeconds(int gameSeconds) {
		this.gameSeconds = gameSeconds;
	}
}
