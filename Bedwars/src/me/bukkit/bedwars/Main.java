package me.bukkit.bedwars;

import me.bukkit.bedwars.PlayerManager.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by arnavguneta.
 */
public class Main extends JavaPlugin {

	public HashMap<UUID, PlayerManager> playermanager = new HashMap<UUID, PlayerManager>();
	public ArrayList<Bed> beds = new ArrayList<>();
	final public String red = ChatColor.RED + "" + ChatColor.BOLD + "BEDWARS > " + ChatColor.WHITE;
	final public String green = ChatColor.GREEN + "" + ChatColor.BOLD + "BEDWARS > " + ChatColor.WHITE;
	final public String yellow = ChatColor.YELLOW + "" + ChatColor.BOLD + "BEDWARS > " + ChatColor.WHITE;
	public Manager manager;
	public CFile cfg;

	public void onEnable() {
		registerEvents();
		registerCommands();
		instantiateClass();
		makeFiles();
		saveConfig();

		if (!(getDataFolder().exists())) {
			getDataFolder().mkdir();
		}

		manager.setupPlayers();
		getServer().getConsoleSender()
				.sendMessage(green + ChatColor.WHITE + "" + ChatColor.BOLD + "Surival plugin has been enabled");
	}

	public void onDisable(){
		getServer().getConsoleSender()
				.sendMessage(green + ChatColor.WHITE + "" + ChatColor.BOLD + "Surival plugin has been disabled");
	}

	public void registerEvents() {
		getServer().getPluginManager().registerEvents(new Mechanics(), this);
	}

	public void registerCommands(){
		getCommand("bedwars").setExecutor(new Commands());
	}

	public void instantiateClass() {
		manager = new Manager();
	}

	private void makeFiles() {
		cfg = new CFile(this,"cfg.yml");
	}

}
