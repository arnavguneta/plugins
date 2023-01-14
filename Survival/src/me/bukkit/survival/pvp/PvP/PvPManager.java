package me.bukkit.survival.pvp.PvP;

import me.bukkit.survival.Main;
import me.bukkit.survival.pvp.Battle;
import me.bukkit.survival.pvp.GameState;
import me.bukkit.survival.pvp.Requests;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static me.bukkit.survival.pvp.GameState.*;

public class PvPManager {

	List<String> waiting = new ArrayList<>();
	private Main plugin = Main.getPlugin(Main.class);
	private List<String> battling = new ArrayList<>();
	private List<Battle> battles = new ArrayList<>();
	public List<Requests> requests = new ArrayList<>();
	private GameState state;
	private int count = 10;

	private Location spawnpoint1 = new Location(Bukkit.getServer().createWorld(new WorldCreator("pvp")), -1856.569, 81,
			1113.811);
	private Location spawnpoint2 = new Location(Bukkit.getServer().createWorld(new WorldCreator("pvp")), -1855.511, 80,
			1013.325);

	public Boolean isInBattle(Player who) {
		return battling.contains(who.getName());
	}

	/*public List<Battle> getBattles() {
		return battles;
	}*/

	public Battle getBattle(Player who) {
		for (Battle b : battles) {
			if (b.getPlayer1().equalsIgnoreCase(who.getName()) || b.getPlayer2().equalsIgnoreCase(who.getName())) {
				return b;
			}
		}
		return null;
	}

	// save inventory

	@SuppressWarnings("unused") public void countdown(Player player1, Player player2) {

		setState(WAITING);

		String name1 = player1.getName();
		String name2 = player2.getName();

		waiting.add(name1);
		waiting.add(name2);

		plugin.manager.saveInventory(player1);
		plugin.manager.saveInventory(player2);

		player1.teleport(spawnpoint1);
		player2.teleport(spawnpoint2);

		plugin.manager.setInventory(player1);
		plugin.manager.setInventory(player2);

		plugin.kmanager.openGUI(player1);
		plugin.kmanager.openGUI(player2);

		player1.getInventory().clear();
		player2.getInventory().clear();
		player1.getInventory().setArmorContents(null);
		player2.getInventory().setArmorContents(null);

		new BukkitRunnable() {
			@Override public void run() {

				if (count > 0) {

					try {

						player1.sendMessage(plugin.pvpyellow + "Battle starting in " + ChatColor.GREEN + ChatColor.BOLD + count + "...");
						player2.sendMessage(plugin.pvpyellow + "Battle starting in " + ChatColor.GREEN + ChatColor.BOLD + count + "...");

						plugin.manager.pScoreboard(player1);
						plugin.manager.pScoreboard(player2);

						count--;

					} catch (NullPointerException e) {
						count = 10;
						this.cancel();

						setState(IN_LOBBY);

						waiting.remove(name1);
						waiting.remove(name2);

						plugin.manager.pScoreboard(player1);
						plugin.manager.pScoreboard(player2);

						e.printStackTrace();
					}

				} else {
					count = 10;
					this.cancel();
					waiting.remove(name1);
					waiting.remove(name2);
					startBattle(player1, player2);
				}

			}
		}.runTaskTimerAsynchronously(plugin, 10, 20);

	}

	private void startBattle(Player player1, Player player2) {
		final Battle b = new Battle(player1, player2);
		setState(IN_GAME);

		player1.sendMessage(plugin.pvpyellow + "Battle has started. Good luck!");
		player2.sendMessage(plugin.pvpyellow + "Battle has started. Good luck!");

		plugin.manager.pScoreboard(player1);
		plugin.manager.pScoreboard(player2);

		battles.add(b);
		battling.add(b.getPlayer2());
		battling.add(b.getPlayer1());
	}

	@SuppressWarnings("deprecation") void endBattle(Battle b, String winner) {
		Player player1 = Bukkit.getPlayer(b.getUuid1());
		Player player2 = Bukkit.getPlayer(b.getUuid2());
		b.setWinner(winner);

		plugin.manager.pScoreboard(player1);
		plugin.manager.pScoreboard(player2);

		setState(IN_LOBBY);

		if (player1 != null && player2 != null) {
			player1.sendMessage(plugin.pvpyellow + "Battle has ended. " + b.getWinner() + " has won!");
			player2.sendMessage(plugin.pvpyellow + "Battle has ended. " + b.getWinner() + " has won!");
		}

		Player pw = Bukkit.getPlayer(b.getWinner());

		addCoins(pw);

		battling.remove(b.getPlayer1());
		battling.remove(b.getPlayer2());
		battles.remove(b);

		player1.getInventory().clear();
		player2.getInventory().clear();
		player1.getInventory().setArmorContents(null);
		player2.getInventory().setArmorContents(null);
	}

	public boolean hasRequest(String who){
		for (Requests request : requests){
			if (request.getRequestee().equalsIgnoreCase(who)){
				return true;
			}
		}
		return false;
	}

	public boolean hasRequested(String who){
		for (Requests request : requests){
			if (request.getRequester().equalsIgnoreCase(who)) return true;
		}
		return false;
	}

	public Requests getCurrentRequested(String player){
		for (Requests request : requests){
			if (request.getRequestee().equalsIgnoreCase(player)) return request;
		}
		return null;
	}

	private void addCoins(Player player) {
		plugin.getConfig().set("economy." + player.getDisplayName() + ".balance", getCoins(player) + 300.0);
		plugin.saveConfig();
	}

	private double getCoins(Player player) {
		return plugin.getConfig().getDouble("economy." + player.getDisplayName() + ".balance");
	}

	public GameState getState() {
		return state;
	}

	private void setState(GameState state) {
		this.state = state;
	}

	public int getCount() {
		return count;
	}
}


