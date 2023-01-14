package me.bukkit.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Util {

	public static ChatColor getColor(Player player) {
		if (player.getName().equals("Gqbe") || player.getName().equals("arguneta")) {
			return ChatColor.GREEN;
		}

		if (player.getName().equals("iiTagz")) {
			return ChatColor.AQUA;
		}

		return ChatColor.GRAY;

	}

}
