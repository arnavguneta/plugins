package me.bukkit.chatbot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.security.auth.login.LoginException;

public class Mechanics extends ListenerAdapter implements Listener {
	public Main plugin;
	public JDA jda;
	public Mechanics(Main main) {
		this.plugin = main;
		startBot();
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
		jda.addEventListener(this);
	}

	private void startBot() {
		try {
			jda = new JDABuilder(AccountType.BOT).setToken("NjA5OTAzNTU1NTMyNDIzMTc4.XU9p4A.P9c_9aWbWYjr0C-0QtNP1_WJDzU").buildBlocking();
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void playerChatEvent(AsyncPlayerChatEvent e){
		String message = e.getMessage();
		TextChannel textChannel = jda.getTextChannelsByName("minecraft-chat",true).get(0);
		textChannel.sendMessage("**"+e.getPlayer().getName()+":** "+message).queue();
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if(event.getAuthor().isBot() || event.getAuthor().isFake() || event.isWebhookMessage())return;
		String message = event.getMessage().getContentRaw();
		User user = event.getAuthor();
		Bukkit.broadcastMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "DISCORD " + ChatColor.YELLOW + user.getName() + ChatColor.WHITE + ": " + message);
	}
}

