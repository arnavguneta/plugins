package me.bukkit.survival.pvp.Kit;

import me.bukkit.survival.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;

@SuppressWarnings({ "ALL", "WeakerAccess" }) public class KitMechanics implements Listener {

	private Main plugin = Main.getPlugin(Main.class);

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {

		if (e.getInventory().getName().equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "Kits")) {

			Player p = (Player) e.getWhoClicked();
			PlayerInventory i = p.getInventory();

			e.setCancelled(true);

			switch (e.getCurrentItem().getType()) {

			case FISHING_ROD:
				plugin.kmanager.mcsg(p, i);
				break;
			case GOLDEN_APPLE:
				plugin.kmanager.gapple(p, i);
				break;
			case BOW:
				plugin.kmanager.archer(p, i);
				break;
			case DIAMOND_BARDING:
				plugin.kmanager.horse(p, i);
				break;
			case POTION:
				plugin.kmanager.pot(p, i);
				break;
			case LEATHER_CHESTPLATE:
				plugin.kmanager.melee(p, i);
				break;
			default:
				break;
			}

		}
	}

}
