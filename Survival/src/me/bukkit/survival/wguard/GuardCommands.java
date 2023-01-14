package me.bukkit.survival.wguard;

import me.bukkit.survival.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by arnavguneta.
 */

public class GuardCommands implements CommandExecutor {

	private Main plugin = Main.getPlugin(Main.class);
	//private FileConfiguration guard = plugin.guard.cfg;

	@Override public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

		if (cmd.getName().equalsIgnoreCase("guard")) {
			if (!(sender instanceof Player))
				return false;

			Player player = (Player) sender;
			int arg = args.length;

			if (arg == 0 || arg >= 2) {
				player.sendMessage(
						plugin.guardred + "Correct usage: /guard <§ewand§f:§esetwand§f:§eprotect§f:§eclear>");
				return true;
			}

			if (arg == 1) {
				if (args[0].equalsIgnoreCase("wand")) {
					ItemStack wandStack = new ItemStack(Material.getMaterial(plugin.gmanager.getWand(player)));
					ItemMeta wandMeta = wandStack.getItemMeta();
					wandMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Guard Wand");
					wandStack.setItemMeta(wandMeta);

					player.getInventory().addItem(wandStack);
					player.sendMessage(plugin.guardgreen + "Guard wand loaded.");
					player.sendMessage(plugin.guardyellow
							+ "Left click to select first position, right click to select second position.");
				} else if (args[0].equalsIgnoreCase("setwand")) {
					if (!(player.isOp())) {
						player.sendMessage(plugin.guardred + "You don't have permission to do this.");
						return true;
					}
					if (player.getItemInHand().getType() == Material.AIR) {
						player.sendMessage(plugin.guardred + "Can't bind " + ChatColor.YELLOW + "AIR " + ChatColor.WHITE + "to guard wand.");
						return true;
					}
					player.sendMessage(
							plugin.guardyellow + "To confirm binding the item in your hand to the guard wand, type "
									+ ChatColor.GREEN + ChatColor.BOLD + "YES" + ChatColor.WHITE + " or "
									+ ChatColor.RED + ChatColor.BOLD + "NO");
					plugin.gmanager.wandMode.put(player.getName(), true);
				} else if (args[0].equalsIgnoreCase("protect")) {
					if (plugin.gmanager.pos1.get(player.getName()) != null && plugin.gmanager.pos2.get(player.getName()) != null) {
						plugin.gmanager.protectArea(player);
					} else {
						player.sendMessage(plugin.guardred + "Positions were not found.");
						return true;
					}

				} else if (args[0].equalsIgnoreCase("clear")) {
					if (!plugin.gmanager.protectedBlocks.get(player.getName()).isEmpty()) {
						plugin.gmanager.protectedBlocks.put(player.getName(), new ArrayList<>());
						player.sendMessage(plugin.guardgreen + "Protected areas have been deleted.");
					} else {
						player.sendMessage(plugin.guardred + "No protected regions were found.");
						return true;
					}

				} else {
					player.sendMessage(
							plugin.guardred + "Correct usage: /guard <§ewand§f:§esetwand§f:§eprotect§f:§eclear>");
					return true;
				}
			}
			return true;
		}
		return false;
	}
}
