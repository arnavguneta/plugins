package me.bukkit.economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;


public class Economy extends JavaPlugin implements Listener {

	
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
	}
	
	public static String coins;
	

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("updatescoreboard")){
				coins = String.valueOf(getConfig().getInt(p.getUniqueId().toString() + ".Coins"));

				ScoreboardManager manager = Bukkit.getScoreboardManager();
				final Scoreboard board = manager.getNewScoreboard();
				final Objective objective = board.registerNewObjective("dummy", "dummy");

				objective.setDisplaySlot(DisplaySlot.SIDEBAR);
				objective.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Statistics");

				Score score = objective.getScore(ChatColor.GREEN + "" + ChatColor.BOLD + "Player name:");
				score.setScore(10);
				
				Score score1 = objective.getScore(ChatColor.YELLOW + p.getName());
				score1.setScore(9);

				Score score2 = objective.getScore(ChatColor.GREEN + "" + ChatColor.BOLD + "Coins:");
				score2.setScore(8);

				// improve

				Score score3 = objective.getScore(ChatColor.YELLOW + coins);
				score3.setScore(7);

				p.setScoreboard(board);
				
				return true;
				
			}
			// start cmd
			if (cmd.getName().equalsIgnoreCase("coins")) {
				// start check OP
				if (p.isOp()) {
					// start al0
					if (args.length == 0) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "Economy"
								+ ChatColor.WHITE + "] " + "The format is \"/coins {action} {player} {amount}!\"");
					}
					// end al0
					// start al1
					if (args.length == 1) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "Economy"
								+ ChatColor.WHITE + "] " + "The format is \"/coins {action} {player} {amount}!\"");
					}
					// end al1
					// start al2
					if (args.length == 2) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "Economy"
								+ ChatColor.WHITE + "] " + "The format is \"/coins {action} {player} {amount}!\"");
					}
					// end al2
					// start al3
					if (args.length == 3) {
						// start set
						if (args[0].equalsIgnoreCase("set")) {
							Player tp = Bukkit.getPlayer(args[1]);
							// start pcheck
							if (tp != null) {
								// start numcheck
								if (isNumeric(args[2])) {
									int args2 = Integer.parseInt(args[2]);
									// start contains
									if (!getConfig().contains(tp.getUniqueId().toString())) {
										getConfig().createSection(p.getUniqueId().toString());
										saveConfig();

									} else {
										getConfig().set(tp.getUniqueId().toString() + ".Coins", args2);
										saveConfig();
										tp.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD
												+ "Economy" + ChatColor.WHITE + "] " + "Your balance was set to "
												+ ChatColor.YELLOW + args[2] + ChatColor.WHITE + " coins!");
										tp.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD
												+ "Economy" + ChatColor.WHITE + "] " + "To check your balance type "
												+ ChatColor.DARK_GREEN + "/balance" + ChatColor.WHITE + "!");

										p.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD
												+ "Economy" + ChatColor.WHITE + "] " + "You set " + ChatColor.DARK_GREEN
												+ "" + tp.getName() + "'s" + ChatColor.WHITE + " to " + ChatColor.YELLOW
												+ args[2] + ChatColor.WHITE + " coins!");
										p.performCommand("updatescoreboard");
										tp.performCommand("updatescoreboard");
									}
									// end contains
								} else {
									p.sendMessage(ChatColor.RED + "");
								}
								// end numcheck
							} else {
								p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "Economy"
										+ ChatColor.WHITE + "] " + "Could not find that player!");
							}
							// end pcheck
						}
						// end set

						// start add
						if (args[0].equalsIgnoreCase("add")) {
							Player tp = Bukkit.getPlayer(args[1]);
							// start pcheck
							if (tp != null) {
								// start numcheck
								if (isNumeric(args[2])) {
									int args2 = Integer.parseInt(args[2]);
									// start contains
									if (!getConfig().contains(tp.getUniqueId().toString())) {
										getConfig().createSection(p.getUniqueId().toString());
										saveConfig();

									} else {
										int coins = getConfig().getInt(tp.getUniqueId().toString() + ".Coins");
										getConfig().set(tp.getUniqueId().toString() + ".Coins", coins + args2);
										saveConfig();

										tp.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD
												+ "Economy" + ChatColor.WHITE + "] " + ChatColor.YELLOW + args[2]
												+ ChatColor.WHITE + " coins were added to your balance!");
										tp.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD
												+ "Economy" + ChatColor.WHITE + "] " + "To check your balance type "
												+ ChatColor.DARK_GREEN + "/balance" + ChatColor.WHITE + "!");

										p.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD
												+ "Economy" + ChatColor.WHITE + "] " + "You added " + ChatColor.YELLOW
												+ args[2] + ChatColor.WHITE + " coins to " + ChatColor.DARK_GREEN
												+ tp.getName() + "'s" + ChatColor.WHITE + " balance!");
										p.performCommand("updatescoreboard");
										tp.performCommand("updatescoreboard");
									}
									// end contains
								} else {
									p.sendMessage(ChatColor.RED + "");
								}
								// end numcheck
							} else {
								p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "Economy"
										+ ChatColor.WHITE + "] " + "Could not find that player!");
							}
							// end pcheck
						}
						// end add

						// start remove
						if (args[0].equalsIgnoreCase("remove")) {
							Player tp = Bukkit.getPlayer(args[1]);
							// start pcheck
							if (tp != null) {
								// start numcheck
								if (isNumeric(args[2])) {
									int args2 = Integer.parseInt(args[2]);
									// start contains
									if (!getConfig().contains(tp.getUniqueId().toString())) {
										getConfig().createSection(p.getUniqueId().toString());
										saveConfig();

									} else {
										int coins = getConfig().getInt(tp.getUniqueId().toString() + ".Coins");
										if (coins >= args2) {
											getConfig().set(tp.getUniqueId().toString() + ".Coins", coins - args2);
											saveConfig();

											tp.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD
													+ "Economy" + ChatColor.WHITE + "] " + ChatColor.YELLOW + args[2]
													+ ChatColor.WHITE + " coins were removed from your balance!");
											tp.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD
													+ "Economy" + ChatColor.WHITE + "] " + "To check your balance type "
													+ ChatColor.DARK_GREEN + "/balance" + ChatColor.WHITE + "!");

											p.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD
													+ "Economy" + ChatColor.WHITE + "] " + "You removed "
													+ ChatColor.YELLOW + args[2] + ChatColor.WHITE + " coins from "
													+ ChatColor.DARK_GREEN + tp.getName() + "'s" + ChatColor.WHITE
													+ " balance!");
											p.performCommand("updatescoreboard");
											tp.performCommand("updatescoreboard");
										} else {
											p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD
													+ "Economy" + ChatColor.WHITE + "] "
													+ "You cannot give someone negative coins!");
										}
									}
									// end contains
								} else {
									p.sendMessage(ChatColor.RED + "");
								}
								// end numcheck
							} else {
								p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "Economy"
										+ ChatColor.WHITE + "] " + "Could not find that player!");
							}
							// end pcheck
						}
						// end remove
					}
					// end all3

				} else {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "Economy"
							+ ChatColor.WHITE + "] " + "You do not have permission to perfom this command!");
				}
				// end check OP
				return true;
			}
			// end cmd

			// start cmd
			if (cmd.getName().equalsIgnoreCase("balance") && sender instanceof Player) {
				// check args
				if (args.length == 0) {
					p.sendMessage(
							ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "Balance" + ChatColor.WHITE
									+ "] " + "Coins: " + getConfig().getInt(p.getUniqueId().toString() + ".Coins"));
				}

				if (args.length == 1) {
					Player tp = Bukkit.getPlayer(args[0]);
					if (tp != null) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "Balance"
								+ ChatColor.WHITE + "] " + "" + args[0] + "'s Coins: "
								+ getConfig().getInt(tp.getUniqueId().toString() + ".Coins"));
					} else {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "Balance"
								+ ChatColor.WHITE + "] " + "Couldn't find that player!");
					}
				}
				return true;
			}
			
		}
		return false;
	}

	public int getCoins(Player p) {
		return (getConfig().getInt(p.getUniqueId().toString() + ".Coins"));
	}

	@SuppressWarnings("unused")
	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public void giveCoins(Player p, int i) {
		getConfig().set(p.getUniqueId().toString() + ".Coins",
				getConfig().getInt(p.getUniqueId().toString() + ".Coins", 0) + i);
		saveConfig();
		

	}

	public void removeCoins(Player p, int i) {
		getConfig().set(p.getUniqueId().toString() + ".Coins",
				getConfig().getInt(p.getUniqueId().toString() + ".Coins", 0) - i);
		saveConfig();

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!getConfig().contains(p.getName())) {
			getConfig().set(p.getUniqueId().toString() + ".Coins", 1000);
		}
	}

	@EventHandler
	public void onKill(EntityDeathEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			removeCoins(player, 200);
			if (player.getKiller() instanceof Player) {
				Player p = player.getKiller();
				giveCoins(p, 200);
				p.performCommand("updatescoreboard");
				player.performCommand("updatescoreboard");

			}
		}

	}
	
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		p.performCommand("updatescoreboard");

	}

}
