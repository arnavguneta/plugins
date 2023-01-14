package me.bukkit.onevone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PvP extends JavaPlugin implements Listener {

	private Map<Player, Player> onevone = new HashMap<Player, Player>();
	private ArrayList<Player> inBattle = new ArrayList<Player>();

	private int number = 3;
	private int num = 3;

	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;

		Location loc = new Location(p.getWorld(), -1856.569, 81, 1113.811);
		Location loc1 = new Location(p.getWorld(), -1855.511, 80, 1013.325);

		int arg = args.length;

		if (label.equalsIgnoreCase("1v1")) {

			if (arg == 0) {
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1" + ChatColor.WHITE
						+ "] " + "The correct format is \"/1v1 {action} <player> ");

			}
			if (arg == 1) {
				if (args[0].equalsIgnoreCase("quit")) {
					if (inBattle.contains(p)) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "1v1"
								+ ChatColor.WHITE + "] " + "You have left the 1v1!");

						inBattle.remove(p);
					} else {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1"
								+ ChatColor.WHITE + "] " + "You are not in a 1v1!");
					}

				} else {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1" + ChatColor.WHITE
							+ "] " + "The correct format is \"/1v1 {action} <player> ");
				}
			}

			if (arg == 2) {

				if (args[0].equalsIgnoreCase("invite")) {

					Player tp = Bukkit.getPlayer(args[1]);

					if (tp == null) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1"
								+ ChatColor.WHITE + "] " + "That player was not found!");
					} else if (tp == p) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1"
								+ ChatColor.WHITE + "] " + "You can't 1v1 yourself!");
					} else {

						if (inBattle.contains(tp)) {
							p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1"
									+ ChatColor.WHITE + "] " + "That player is already in a battle!");
						} else {

							onevone.put(tp, p);

							p.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "1v1"
									+ ChatColor.WHITE + "] " + ChatColor.WHITE + "You have invited " + ChatColor.GREEN
									+ tp.getName() + ChatColor.WHITE + " to a 1v1!");
							tp.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "1v1"
									+ ChatColor.WHITE + "] " + ChatColor.GREEN + p.getName() + ChatColor.WHITE
									+ " has invited you to a 1v1! Type " + ChatColor.DARK_GREEN + "/1v1 accept "
									+ p.getName() + ChatColor.WHITE + " to accept the 1v1 request! To deny it, type "
									+ ChatColor.DARK_GREEN + "/1v1 deny " + p.getName());

						}

					}
				}

				if (args[0].equalsIgnoreCase("accept")) {
					if (onevone.get(p) == null) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1"
								+ ChatColor.WHITE + "] " + "You do not have any 1v1 requests!");
					}
					if (onevone.get(p) != null) {
						Player tp = Bukkit.getPlayer(args[1]);
						if (tp == null) {
							p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1"
									+ ChatColor.WHITE + "] " + "That player was not found!");
						} else if (tp == p) {
							p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1"
									+ ChatColor.WHITE + "] " + "You can't 1v1/accept 1v1s from yourself!");
						} else {

							p.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "1v1"
									+ ChatColor.WHITE + "] " + ChatColor.WHITE + "You have accepted 1v1 invite from "
									+ ChatColor.GREEN + tp.getName() + ChatColor.WHITE + ". To leave the 1v1 type "
									+ ChatColor.DARK_GREEN + "/1v1 quit!");
							tp.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "1v1"
									+ ChatColor.WHITE + "] " + ChatColor.GREEN + p.getName() + ChatColor.WHITE
									+ " has accepted your 1v1 invite, to leave the 1v1 type " + ChatColor.DARK_GREEN
									+ "/1v1 quit" + ChatColor.WHITE + "!");
							p.sendMessage("test");
							tp.sendMessage("test");
							counterStart();
							 p.sendMessage("test1");
							this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
								public void run() {
									if (num != -1) {
										if (num != 0) {
											tp.getWorld().playSound(tp.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 0);
											p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 0);
											p.getLocation().getWorld().playEffect(p.getLocation(),
													Effect.MOBSPAWNER_FLAMES, 2004);
											tp.getLocation().getWorld().playEffect(tp.getLocation(),
													Effect.MOBSPAWNER_FLAMES, 2004);
											tp.playNote(tp.getLocation(), Instrument.PIANO, new Note(1));
											p.playNote(p.getLocation(), Instrument.PIANO, new Note(1));
											num--;
										} else {
											p.sendMessage("");
											tp.sendMessage("");
											num--;
										}

									}
								}

							}, 0L, 20L);

							if (!(p.getLocation() == loc)) {
								p.teleport(loc);
								p.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "1v1"
										+ ChatColor.WHITE + "] " + "You have been teleported!");
								p.getLocation().setYaw(-1.7f);
								p.getLocation().setPitch((float) 179.4);
								p.performCommand("kit");
								p.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "1v1"
										+ ChatColor.WHITE + "] " + "Pick a kit and fight!");

							}

							if (!(tp.getLocation() == loc)) {
								tp.teleport(loc1);
								tp.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "1v1"
										+ ChatColor.WHITE + "] " + "You have been teleported!");
								tp.getLocation().setYaw(-5.3f);
								tp.getLocation().setPitch((float) -1.0);
								tp.performCommand("kit");
								tp.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "1v1"
										+ ChatColor.WHITE + "] " + "Pick a kit and fight!");
								inBattle.add(tp);
								inBattle.add(p);

							}
							onevone.put(p, null);

							// p = gqbe
						}
					}
				}

				if (args[0].equalsIgnoreCase("deny")) {
					if (onevone.get(p) == null) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1"
								+ ChatColor.WHITE + "] " + "You do not have any 1v1 requests!");
					}

					if (onevone.get(p) != null) {
						Player tp = Bukkit.getPlayer(args[1]);
						if (tp == null) {
							p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1"
									+ ChatColor.WHITE + "] " + "That player was not found!");
						} else if (tp == p) {
							p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1"
									+ ChatColor.WHITE + "] " + "You can't 1v1/deny 1v1s from yourself!");
						} else if (tp != null) {

							p.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "1v1"
									+ ChatColor.WHITE + "] " + ChatColor.WHITE + "You have denied the 1v1 invite from "
									+ ChatColor.GREEN + tp.getName() + ChatColor.WHITE + "!");
							tp.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "1v1"
									+ ChatColor.WHITE + "] " + ChatColor.GREEN + p.getName() + ChatColor.WHITE
									+ " has denied your 1v1 invite" + ChatColor.WHITE + "!");

							onevone.put(p, null);

						}
					}

				}

			}

			return true;

		} else {
			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1" + ChatColor.WHITE + "] "
					+ "You are not permitted to use this command!");
		}
		return false;
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (e.getEntity().getKiller() instanceof Player) {
			Player p = e.getEntity().getPlayer();
			Player tp = e.getEntity().getPlayer();

			if (inBattle.contains(p) && inBattle.contains(tp)) {

				inBattle.remove(p);
				inBattle.remove(tp);

			}

		}
	}

	@SuppressWarnings("deprecation")
	public void counterStart() {
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			public void run() {
				if (number != -1) {
					if (number != 0) {
						Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + ""
								+ ChatColor.BOLD + "1v1" + ChatColor.WHITE + "] " + "Starts in " + number + "...");
						number--;
						// TO DO: TEST
					} else {
						Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + ""
								+ ChatColor.BOLD + "1v1" + ChatColor.WHITE + "] " + "1v1 has started, Good luck!");
						number--;
					}

				}
			}

		}, 0L, 20L);
	}

	@EventHandler
	public void onCommandExecute(PlayerCommandPreprocessEvent event) {

		Player p = event.getPlayer();

		if (inBattle.contains(p)) {
			String command[] = event.getMessage().split(" ");
			if (command[0].equalsIgnoreCase("/1v1")) {
				if (command[1].equalsIgnoreCase("quit")) {
					return;
				} else {
					event.setCancelled(true);
					p.sendMessage(
							ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1" + ChatColor.WHITE + "] "
									+ "You are in a 1v1, type " + ChatColor.DARK_GREEN + "/1v1 quit" + ChatColor.WHITE + " to leave!");
				}
			} else {
				event.setCancelled(true);
				p.sendMessage(
						ChatColor.WHITE + "[" + ChatColor.RED + "" + ChatColor.BOLD + "1v1" + ChatColor.WHITE + "] " + "You are in a 1v1, type " + ChatColor.DARK_GREEN + "/1v1 quit" + ChatColor.WHITE
								+ " to leave!");

			}

		}
	}
}
