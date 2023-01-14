package me.bukkit.survival.pvp.PvP;

import me.bukkit.survival.Main;
import me.bukkit.survival.pvp.Battle;
import me.bukkit.survival.pvp.Requests;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class PvPCommand implements CommandExecutor{

	private Main plugin = Main.getPlugin(Main.class);

	@Override public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player player;
		Battle b;

		if (! (sender instanceof Player)) {
			return true;
		} else {
			player = (Player) sender;
		}

		if (cmd.getName().equalsIgnoreCase("1v1")) {
			int arg = args.length;

			if (arg == 0 || arg >= 2) { // if not equal to given args

				player.sendMessage(plugin.pvpred + "Correct usage: /1v1 <§eplayer§f:§eaccept§f:§edeny§f>");

			} else if (arg == 1) { // quit
				if (args[0].equalsIgnoreCase("quit")) {
					if (plugin.pmanager.isInBattle(player)) { // if quit and in battle

						b = plugin.pmanager.getBattle(player);

						String p1 = b.getPlayer1();
						String p2 = b.getPlayer2();

						if (player.getDisplayName().equalsIgnoreCase(p1)) {
							plugin.pmanager.endBattle(b, p2);
						} else if (player.getDisplayName().equalsIgnoreCase(p2)) {
							plugin.pmanager.endBattle(b, p1);
						}

					} else { // if quit and not in battle
						player.sendMessage(plugin.pvpred + "You are not in a battle!");
					}

					return true;

				} else if (args[0].equalsIgnoreCase("accept")) {
					if (plugin.pmanager.hasRequest(sender.getName())) {
						Requests request = plugin.pmanager.getCurrentRequested(sender.getName());
						if (request == null)
							return true;
						if (!plugin.pmanager.hasRequested(request.getRequester()))
							return false;

						Player p1 = Bukkit.getPlayer(request.getRequester());
						Player p2 = Bukkit.getPlayer(request.getRequestee());
						if (p1 == null || p2 == null)
							return false;

						plugin.pmanager.countdown(p1, p2);
					} else
						sender.sendMessage(plugin.pvpred + "You do not have a pending request!");

					return true;
				} else if (args[0].equalsIgnoreCase("deny")) {
					if (plugin.pmanager.hasRequest(sender.getName())) {
						Requests request = plugin.pmanager.getCurrentRequested(sender.getName());
						request.deny();
						plugin.pmanager.requests.remove(request);
						sender.sendMessage(plugin.pvpred + "Denied request.");
					} else
						sender.sendMessage(plugin.pvpred + "You do not have a pending request!");

					return true;

				} else if (args[0].equalsIgnoreCase(sender.getName())) {
					sender.sendMessage(plugin.pvpred + "You can't 1v1 yourself!");
				} else {
					String playerName = args[0];
					if (Bukkit.getPlayer(playerName) == null) {

						sender.sendMessage(plugin.pvpred + "Player is not online!");
						return true;
					} else {
						if (!Bukkit.getPlayer(playerName).isOnline()) {
							sender.sendMessage(plugin.pvpred + "Player is not online!");
							return true;
						}
					}

					//request
					Requests request = new Requests(sender.getName(), playerName);
					sender.sendMessage(plugin.pvpgreen + "Sent request!");

					if (plugin.pmanager.hasRequest(playerName)) {
						plugin.pmanager.requests.remove(plugin.pmanager.getCurrentRequested(playerName));
					}

					plugin.pmanager.requests.add(request);
				}

			}

		}

		return true;

	}
}
