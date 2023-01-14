package me.bukkit.survival.wguard;

import me.bukkit.survival.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arnavguneta.
 */
@SuppressWarnings({ "ALL" }) public class GuardManager {
	public HashMap<String, Boolean> wandMode = new HashMap<>();
	public HashMap<String, ArrayList<Location>> protectedBlocks = new HashMap<>();
	// all protected blocks and owner
	public HashMap<String, ArrayList> locations = new HashMap<>();
	// all of the protected blocks
	public ArrayList<Location> plocations = new ArrayList<>();
	public HashMap<String, Location> pos1 = new HashMap<>();
	public HashMap<String, Location> pos2 = new HashMap<>();

	public String message;
	public int blockCount;

	// TODO save protected blocks
	private Main plugin = Main.getPlugin(Main.class);
	private FileConfiguration guard = plugin.guard.cfg;

	public void setUpWand(Player player) {
		if (message.equalsIgnoreCase("YES")) {
			setWand(player, player.getItemInHand().getType());
			player.sendMessage(plugin.guardgreen + "Item in your hand was binded to guard wand.");
		} else {
			player.sendMessage(plugin.guardred + "Wand setup cancelled.");
			return;
		}
		wandMode.put(player.getName(), false);
	}

	public void setWand(Player player, Material mat) {
		guard.set(player.getName() + ".wand", mat.toString());
		plugin.guard.save();
	}

	public String getWand(Player player) {
		if (guard.contains(player.getName() + ".wand") == true)
			return guard.getString(player.getName() + ".wand");

		return guard.getString("default_wand");

	}

	public void setupLocations(Player player) {
		if (pos1.get(player.getName()) != null && pos2.get(player.getName()) != null) {
			ArrayList<Location> temp = new ArrayList<>();
			temp.add(pos1.get(player.getName()));
			temp.add(pos2.get(player.getName()));
			locations.put(player.getName(), temp);
			player.sendMessage(
					plugin.guardyellow + "To protect the selection, type " + ChatColor.GREEN + "" + ChatColor.BOLD
							+ "/guard protect");
		}
	}

	public void protectArea(Player player) {
		if (protectedBlocks.keySet().contains(player.getName())) {
			plocations = protectedBlocks.get(player.getName());
		}

		int mix, max, miy, may, miz, maz;

		if (pos1.get(player.getName()).getBlockX() < pos2.get(player.getName()).getBlockX()) {
			mix = pos1.get(player.getName()).getBlockX();
			max = pos2.get(player.getName()).getBlockX();
		} else {
			mix = pos2.get(player.getName()).getBlockX();
			max = pos1.get(player.getName()).getBlockX();
		}

		if (pos1.get(player.getName()).getBlockY() < pos2.get(player.getName()).getBlockY()) {
			miy = pos1.get(player.getName()).getBlockY();
			may = pos2.get(player.getName()).getBlockY();
		} else {
			miy = pos2.get(player.getName()).getBlockY();
			may = pos1.get(player.getName()).getBlockY();
		}

		if (pos1.get(player.getName()).getBlockZ() < pos2.get(player.getName()).getBlockZ()) {
			miz = pos1.get(player.getName()).getBlockZ();
			maz = pos2.get(player.getName()).getBlockZ();
		} else {
			miz = pos2.get(player.getName()).getBlockZ();
			maz = pos1.get(player.getName()).getBlockZ();
		}

		pos1.put(player.getName(), null);
		pos2.put(player.getName(), null);

		for (int x = mix; x <= max; x++) {
			for (int y = 0; y <= 256; y++) {
				for (int z = miz; z <= maz; z++) {
					Location loc = new Location(player.getWorld(), x, y, z);
					plocations.add(loc);
					blockCount++;
				}
			}
		}

		protectedBlocks.put(player.getName(), plocations);
		player.sendMessage(plugin.guardgreen + blockCount + " blocks protected!");
		blockCount = 0;
	}
}
