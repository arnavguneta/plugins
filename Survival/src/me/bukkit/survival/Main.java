package me.bukkit.survival;

import me.bukkit.survival.pvp.Kit.KitManager;
import me.bukkit.survival.pvp.Kit.KitMechanics;
import me.bukkit.survival.pvp.PvP.PvPCommand;
import me.bukkit.survival.pvp.PvP.PvPManager;
import me.bukkit.survival.pvp.PvP.PvPMechanics;
import me.bukkit.survival.wguard.GuardCommands;
import me.bukkit.survival.wguard.GuardManager;
import me.bukkit.survival.wguard.GuardMechanics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {

	// TODO prices
	// TODO blockShop

	final public String pvpred = ChatColor.RED + "" + ChatColor.BOLD + "1v1 > " + ChatColor.WHITE;
	final public String pvpgreen = ChatColor.GREEN + "" + ChatColor.BOLD + "1v1 > " + ChatColor.WHITE;
	final public String pvpyellow = ChatColor.YELLOW + "" + ChatColor.BOLD + "1v1 > " + ChatColor.WHITE;
	final public String guardred = ChatColor.RED + "" + ChatColor.BOLD + "GUARD > " + ChatColor.WHITE;
	final public String guardgreen = ChatColor.GREEN + "" + ChatColor.BOLD + "GUARD > " + ChatColor.WHITE;
	final public String guardyellow = ChatColor.YELLOW + "" + ChatColor.BOLD + "GUARD > " + ChatColor.WHITE;
	final String redpref = ChatColor.RED + "" + ChatColor.BOLD + "INFO > " + ChatColor.WHITE;
	final String greenpref = ChatColor.GREEN + "" + ChatColor.BOLD + "INFO > " + ChatColor.WHITE;
	final String yellowpref = ChatColor.YELLOW + "" + ChatColor.BOLD + "INFO > " + ChatColor.WHITE;
	final String test = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "TEST ➤ " + ChatColor.LIGHT_PURPLE;
	final String shopred = ChatColor.RED + "" + ChatColor.BOLD + "SHOP > " + ChatColor.WHITE;
	final String shopgreen = ChatColor.GREEN + "" + ChatColor.BOLD + "SHOP > " + ChatColor.WHITE;
	final String shopyellow = ChatColor.YELLOW + "" + ChatColor.BOLD + "SHOP > " + ChatColor.WHITE;
	public Manager manager;
	public PvPManager pmanager;
	public KitManager kmanager;
	public GuardManager gmanager;
	public CFile guard;
	public CFile shop;
	public CFile cfg;
	public CFile economy;

	InventoryStringDeSerializer deserializer;

	public void onEnable() {
		makeFiles();
		commandsExec();
		instanceClasses();
		registerEvents();
		saveConfig();

		manager.autoClearLag();

		if (!(getDataFolder().exists())) {
			getDataFolder().mkdir();
		}

		guard.cfg.set("default_wand", "GOLD_SPADE");
		guard.save();

		manager.loadOnEnable();

		if (gmanager.protectedBlocks == null) {
			gmanager.protectedBlocks = new HashMap<>();
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			manager.villagerLocationMode.put(player.getDisplayName(), false);
			gmanager.wandMode.put(player.getDisplayName(), false);
			//gmanager.protectedBlocks.put(player.getDisplayName(), new ArrayList<>());
		}

		getServer().getConsoleSender()
				.sendMessage(greenpref + ChatColor.BLUE + "" + ChatColor.BOLD + "Surival plugin has been enabled");
	}

	public void onDisable() {
		manager.saveOnDisable();
		getServer().getConsoleSender()
				.sendMessage(greenpref + ChatColor.BLUE + "" + ChatColor.BOLD + "Surival plugin has been disabled");
	}

	private void instanceClasses() {
		manager = new Manager();
		deserializer = new InventoryStringDeSerializer();
		pmanager = new PvPManager();
		kmanager = new KitManager();
		gmanager = new GuardManager();
	}

	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new Mechanics(), this);
		getServer().getPluginManager().registerEvents(new Manager(), this);
		getServer().getPluginManager().registerEvents(new PvPMechanics(), this);
		getServer().getPluginManager().registerEvents(new KitMechanics(), this);
		getServer().getPluginManager().registerEvents(new GuardMechanics(), this);
	}

	private void commandsExec() {
		getCommand("shop").setExecutor(new Commands());
		getCommand("pay").setExecutor(new Commands());
		getCommand("set").setExecutor(new Commands());
		getCommand("balance").setExecutor(new Commands());
		getCommand("kit").setExecutor(new Commands());
		getCommand("setspawn").setExecutor(new Commands());
		getCommand("spawn").setExecutor(new Commands());
		getCommand("sethome").setExecutor(new Commands());
		getCommand("home").setExecutor(new Commands());
		getCommand("back").setExecutor(new Commands());
		getCommand("world").setExecutor(new Commands());
		getCommand("broadcast").setExecutor(new Commands());
		getCommand("fly").setExecutor(new Commands());
		getCommand("1v1").setExecutor(new PvPCommand());
		getCommand("clearlag").setExecutor(new Commands());
		getCommand("chest").setExecutor(new Commands());
		getCommand("guard").setExecutor(new GuardCommands());
	}

	private void makeFiles() {
		guard = new CFile(this, "guard.yml");
		shop = new CFile(this, "shop.yml");
		cfg = new CFile(this, "cfg.yml");
		economy = new CFile(this, "economy.yml");
	}

}
