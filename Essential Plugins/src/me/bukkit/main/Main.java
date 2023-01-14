package me.bukkit.main;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.bukkit.clearlag.Clearlag;
import me.bukkit.events.InventoryListener;
import me.bukkit.events.PlayerListener;
import me.bukkit.heal.Heal;

public class Main extends JavaPlugin {

	public void onEnable() {

		registerEvents();

		registerCommands();

		System.out.println("[Essential Plugins] has been enabled!");

	}

	private void registerCommands() {
		getCommand("heal").setExecutor(new Heal());
		getCommand("ci").setExecutor(new Heal());
		getCommand("clearpots").setExecutor(new Heal());
		getCommand("clearlag").setExecutor(new Clearlag());		
	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new InventoryListener(), this);
		pm.registerEvents(new PlayerListener(), this);
	}

	public void onDisable() {
		System.out.println("[Essential Plugins] has been disabled!");
	}

}
