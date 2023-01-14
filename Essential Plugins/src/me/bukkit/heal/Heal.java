package me.bukkit.heal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

public class Heal implements CommandExecutor {

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (cmd.getName().equalsIgnoreCase("heal") && sender instanceof Player) {

			Player player = (Player) sender;
			
			
			

			int length = args.length;
			
			if (length == 0){
				player.setHealth(20.0);
				player.setFoodLevel((int) 20.0);
				player.sendMessage(ChatColor.GREEN
						+ "You were healed succesfully!");
			}

			if (length == 1) {

				boolean playerFound = false;

				for (Player playerToHeal : Bukkit.getServer()
						.getOnlinePlayers()) {

					if (playerToHeal.getName().equalsIgnoreCase(args[0])) {
						playerToHeal.setHealth(20.0);
						playerToHeal.setFoodLevel((int) 20.0);
						playerToHeal.sendMessage(ChatColor.GREEN
								+ "You have been healed by " + ChatColor.DARK_GREEN + "" +  player.getName()
								+ "!");
						player.sendMessage(ChatColor.DARK_GREEN
								+ playerToHeal.getName() + ChatColor.GREEN 
								+ " was healed succesfully!");
						playerFound = true;
						break;
					}

				}

				if (playerFound == false) {

					player.sendMessage(ChatColor.RED + args[0]
							+ " was not found!");

				}

			} 
			return true;
			
		}
		
		if(label.equalsIgnoreCase("ci") && sender instanceof Player){
			Player p = (Player) sender;
			PlayerInventory pi = p.getInventory();
			
			if (args.length == 0){
			pi.clear();
			
			p.sendMessage(ChatColor.GREEN + "Your inventory was cleared!");
			
			} 
			
			if (args.length == 1){
				Player tp = Bukkit.getPlayer(args[0]);
				if(tp != null){
					PlayerInventory pii = tp.getInventory();
					
					pii.clear();
					
					p.sendMessage(ChatColor.GREEN + "You cleared " + ChatColor.DARK_GREEN + args[0] + "'s" + ChatColor.GREEN + " inventory!");
					tp.sendMessage(ChatColor.GREEN + "Your inventory was cleared!");
				}
			}
			return true;
		}
		
		if(label.equalsIgnoreCase("clearpots") && sender instanceof Player){
			Player player = (Player) sender;
			
			if (args.length == 0){
			
			for (PotionEffect effect : player.getActivePotionEffects())
		        player.removePotionEffect(effect.getType());
			
			player.sendMessage(ChatColor.GREEN + "All your potion effects were cleared!");
			
			} 
			if (args.length == 1){
				Player tp = Bukkit.getPlayer(args[0]);
				if (tp != null){
					for (PotionEffect effect : tp.getActivePotionEffects())
				        tp.removePotionEffect(effect.getType());
					player.sendMessage(ChatColor.GREEN + "You cleared " + ChatColor.DARK_GREEN + args[0] + "'s" + ChatColor.GREEN + " potion effects!");
					tp.sendMessage(ChatColor.GREEN + "All your potion effects were cleared!");
				}
			}
			return true;
		}

		return false;
	}

}
