package me.bukkit.clearlag;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Clearlag implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player p = (Player) sender;
		
		if(label.equalsIgnoreCase("clearlag")){
			p.performCommand("kill @e[type=!Player]");
			p.performCommand("kill @e[type=!Player]");
			Bukkit.broadcastMessage(ChatColor.GOLD + "All entities were cleared!");
			return true;
			
		}
		
		return false;
	}

}
