package me.bukkit.chatbot;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public ConsoleCommandSender console;
	@Override
	public void onEnable() {
		console = Bukkit.getServer().getConsoleSender();
		console.sendMessage("[ChatBot] Plugin enabled!");
		new Mechanics(this);
	}
	@Override
	public void onDisable() {
		console.sendMessage("[ChatBot] Plugin disabled!");
	}
}