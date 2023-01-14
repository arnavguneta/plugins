package me.bukkit.tnttag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MainClass extends JavaPlugin {

	public HashMap<UUID, PlayerManager> playermanager = new HashMap<UUID, PlayerManager>();
	public ArrayList<Player> playersInGame = new ArrayList<Player>();
	public ArrayList<Player> playersLeftGame = new ArrayList<Player>();
	
	public String redpref = ChatColor.GRAY + "[" + ChatColor.RED + "" + ChatColor.BOLD + "TAG" + ChatColor.GRAY + "] "
			+ ChatColor.WHITE;
	public String greenpref = ChatColor.GRAY + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "TAG" + ChatColor.GRAY
			+ "] " + ChatColor.WHITE;
	public String yellowpref = ChatColor.GRAY + "[" + ChatColor.YELLOW + "" + ChatColor.BOLD + "TAG" + ChatColor.GRAY
			+ "] " + ChatColor.WHITE;
	
	public GameMechanics gameMechanics;
	public GameManager gameManager;

	public void onEnable() {
		loadConfig();
		instanceClasses();
		
		getCommand("game").setExecutor(new CommandsManager());
		getCommand("setspawn").setExecutor(new CommandsManager());
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nTNT RUN has been Enabled\n\n");
		getServer().getPluginManager().registerEvents(new GameMechanics(), this);
		
		playermanager.clear();
		playersInGame.clear();
		playersLeftGame.clear();
		
	}

	public void onDisable() {
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n\nTNT RUN has been Disabled\n\n");
	}

	public void loadConfig() {
		saveConfig();
	}

	public void instanceClasses() {
		gameMechanics = new GameMechanics();
		gameManager = new GameManager();
	}

}
