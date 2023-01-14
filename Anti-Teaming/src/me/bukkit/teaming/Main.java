package me.bukkit.teaming;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin {

	// TODO prices
	// TODO blockShop

	final String redpref = ChatColor.RED + "" + ChatColor.BOLD + "ERROR > " + ChatColor.WHITE;
	final String warningpref = ChatColor.RED + "" + ChatColor.BOLD + "WARNING > " + ChatColor.WHITE;

	final String greenpref = ChatColor.GREEN + "" + ChatColor.BOLD + "SUCCESS > " + ChatColor.WHITE;
	final String yellowpref = ChatColor.YELLOW + "" + ChatColor.BOLD + "INFO > " + ChatColor.WHITE;
	final String test = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "TEST ➤ " + ChatColor.LIGHT_PURPLE;
	public Manager manager;
	public CFile yaml;
	private Map<Boolean, String> toggleMap = new HashMap<>();

	public void onEnable() {
		if (!(getDataFolder().exists())) {
			getDataFolder().mkdir();
		}

		makeFiles();
		commandsExec();
		registerEvents();
		saveConfig();

		manager = new Manager();

		toggleMap.put(true, "on");
		toggleMap.put(false, "off");

		getServer().getConsoleSender()
				.sendMessage(greenpref + ChatColor.BOLD + "Anti-teaming plugin has been enabled");
	}

	public void onDisable() {
		getServer().getConsoleSender()
				.sendMessage(greenpref + ChatColor.BOLD + "Surival plugin has been disabled");
	}

	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new Mechanics(), this);
		getServer().getPluginManager().registerEvents(new Manager(), this);
	}

	private void commandsExec() {
		getCommand("anti_teaming").setExecutor(new Commands());
		getCommand("anti_teaming").setTabCompleter(new TabCompletion());
	}

	private void makeFiles() {
		yaml = new CFile(this, "config.yml");
	}

	public Map<Boolean, String> getToggleMap() {
		return toggleMap;
	}

}
