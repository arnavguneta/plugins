package me.bukkit.tnttag;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsManager implements CommandExecutor {

	private MainClass plugin = MainClass.getPlugin(MainClass.class);

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("setspawn") && sender instanceof Player) {
			
			Player player = (Player) sender;
			int arg = args.length;

			if (arg == 0 || arg >= 2) {
				player.sendMessage(plugin.redpref + "Correct usage: /game <§egame§f:§elobby§f>");
				return true;
			}

			if (arg == 1) {
				if (args[0].equalsIgnoreCase("game")) {
					plugin.getConfig().set("GameSpawn.world", player.getLocation().getWorld().getName());
					plugin.getConfig().set("GameSpawn.X", player.getLocation().getX());
					plugin.getConfig().set("GameSpawn.Y", player.getLocation().getY());
					plugin.getConfig().set("GameSpawn.Z", player.getLocation().getZ());
					plugin.getConfig().set("GameSpawn.yaw", player.getLocation().getYaw());
					plugin.getConfig().set("GameSpawn.pitch", player.getLocation().getPitch());
					plugin.saveConfig();
					player.sendMessage(plugin.greenpref + "Game spawn has been set.");
					return true;
				}
				if (args[0].equalsIgnoreCase("lobby")) {
					plugin.getConfig().set("LobbySpawn.world", player.getLocation().getWorld().getName());
					plugin.getConfig().set("LobbySpawn.X", player.getLocation().getX());
					plugin.getConfig().set("LobbySpawn.Y", player.getLocation().getY());
					plugin.getConfig().set("LobbySpawn.Z", player.getLocation().getZ());
					plugin.getConfig().set("LobbySpawn.yaw", player.getLocation().getYaw());
					plugin.getConfig().set("LobbySpawn.pitch", player.getLocation().getPitch());
					plugin.saveConfig();
					player.sendMessage(plugin.greenpref + "Lobby spawn has been set.");
					return true;
				}
				if (!(args[0].equalsIgnoreCase("game")) && !(args[0].equalsIgnoreCase("lobby"))) {
					player.sendMessage(plugin.redpref + "Correct usage: /game <§egame§f:§elobby§f>");
					return true;
				}
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("game") && sender instanceof Player) {

			Player player = (Player) sender;
			int arg = args.length;

			if (arg == 0 || arg >= 2) {
				player.sendMessage(plugin.redpref + "Correct usage: /game <§estart§f:§estop§f>");
				return true;
			}

			if (arg == 1) {
				if (args[0].equalsIgnoreCase("start")) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						plugin.gameManager.gameStop(p);
					}
					Bukkit.getServer().broadcastMessage(plugin.yellowpref + "Game starting...");
					plugin.gameManager.setupGame();
				}
				if (args[0].equalsIgnoreCase("stop")) {
					plugin.gameManager.explosionCountdown = 0;

					for (Player p : Bukkit.getOnlinePlayers()) {
						plugin.gameManager.gameStop(p);
					}

					Bukkit.getServer().broadcastMessage(plugin.yellowpref + "Game stopping...");
				}
				if (!(args[0].equalsIgnoreCase("start")) && !(args[0].equalsIgnoreCase("stop"))) {
					player.sendMessage(plugin.redpref + "Correct usage: /game <§estart§f:§estop§f>");
					return true;
				}
			} else {

				return true;
			}
			return true;
		}

		return true;
	}

}
