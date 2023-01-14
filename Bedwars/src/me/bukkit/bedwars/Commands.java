package me.bukkit.bedwars;

import me.bukkit.bedwars.PlayerManager.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by arnavguneta.
 */
public class Commands implements CommandExecutor {

	private Main plugin = Main.getPlugin(Main.class);

	@Override public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("bedwars")) {

			if (!(sender instanceof Player))
				return true;

			Player player = (Player) sender;
			PlayerManager pm = plugin.playermanager.get(player.getUniqueId());

			int arg = args.length;

			// /bedwars setup generator,spawn iron,gold,emerald,diamond
			if (arg == 0 || arg >= 2) {
				player.sendMessage(plugin.red + "Correct usage: /bedwars <§esetup§f:§estart§f>");
				return true;
			} else {
				if (args[0].equalsIgnoreCase("setup")) {
					player.sendMessage(plugin.yellow + "--------< " + ChatColor.BLUE + ChatColor.BOLD + "SETUP MODE" + ChatColor.WHITE + " >--------"); //30
					player.sendMessage(
							plugin.yellow + "Setup options (type in chat): " + ChatColor.GREEN + ChatColor.BOLD
									+ "GENERATORS" + ChatColor.WHITE + ", " + ChatColor.GREEN + ChatColor.BOLD + "BEDS"
									+ ChatColor.WHITE + "."); //30
					pm.setSetupMode(true);
					player.sendMessage(plugin.yellow + "-------------<  >-------------"); //30

				} else if (args[0].equalsIgnoreCase("start")) {
					plugin.manager.start();
				} else {
					player.sendMessage(plugin.red + "Correct usage: /bedwars <§esetup§f:§estart§f>");
					return true;
				}
			}

		}
		return true;
	}
}
