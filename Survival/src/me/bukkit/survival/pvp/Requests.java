package me.bukkit.survival.pvp;

import me.bukkit.survival.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Requests {

	public BukkitTask timer;
	public int time = 200;
	private String requester;
	private String requestee;
	private Requests instance;
	private Main plugin = Main.getPlugin(Main.class);

	public Requests(String requester, String requestee) {
		this.requester = requester;
		this.requestee = requestee;

		this.startTimer();
	}

	public void startTimer() {
		Player sent = Bukkit.getPlayer(requestee);
		if (sent != null)
			sent.sendMessage(
					plugin.pvpyellow + "You have received a 1v1 request from " + requester + "!");
		sent.sendMessage(
				plugin.pvpyellow + "To accept the request, type " + ChatColor.GREEN + ChatColor.BOLD + "/1v1 accept"
						+ ChatColor.WHITE + ".");
		sent.sendMessage(plugin.pvpyellow + "To deny the request, type " + ChatColor.RED + ChatColor.BOLD + "/1v1 deny"
				+ ChatColor.WHITE + ".");
		sent.sendMessage(plugin.pvpyellow + "Request will time out in 200 seconds.");

		timer = new BukkitRunnable() {
			public void run() {
				if (time <= 0) {
					Player prequester = Bukkit.getPlayer(requester);
					if (prequester == null)
						return;

					prequester.sendMessage(plugin.pvpred + "Request timed out.");
					plugin.pmanager.requests.remove(instance);
					timer.cancel();
				}
			}
		}.runTaskTimer(plugin, 20L, 20L);
	}

	public String getRequester() {
		return requester;
	}

	public String getRequestee() {
		return requestee;
	}

	public void deny() {
		timer.cancel();
	}

	public String serialize() {
		return "Request: \nRequester: " + requester + "\nRequestee: " + requestee + "\nTime left: " + String
				.valueOf(time);
	}
}
