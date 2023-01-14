package me.bukkit.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Created by arnavguneta.
 */
public class Generator {
	private Location location;
	private Material type;
	private int ticks;

	public int getTicks() {
		if (getType().toString().contains("IRON")) {
			return 20;
		} else if (getType().toString().contains("GOLD")) {
			return 20 * 6;
		} else if (getType().toString().contains("DIAMOND")) {
			return 20 * 20;
		} else 	if (getType().toString().contains("EMERALD")) {
			return 20 * 30;
		}
		return 20 * 200000;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	public Material getType() {
		return type;
	}

	public void setType(Material generatorType) {
		this.type = generatorType;
	}  

	public Location getLocation() {

		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Generator(Location location, Material type) {
		this.location = new Location(location.getWorld(), location.getX() + .5, location.getY() + 1, location.getZ() + .5);
		this.type = type;

		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getServer().getWorld("world").dropItem(getLocation(), new ItemStack(getType()));
				/*List<Entity> items = Bukkit.getServer().getWorld("world").getEntities();

				for (Entity ent : items) {
					if (ent instanceof Item) {
						if (((Item) ent).getItemStack().getType().equals(getType()))
							ent.setVelocity(new Vector(0, 0, 0));
							ent.eject();
					}
				}*/
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0, getTicks());


	}

}
