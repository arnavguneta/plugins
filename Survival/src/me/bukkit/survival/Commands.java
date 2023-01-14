package me.bukkit.survival;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Commands implements CommandExecutor {

	private final Main plugin = Main.getPlugin(Main.class);
	private final FileConfiguration cfg = plugin.getConfig();
	private Boolean fly = false;
	private Boolean errorFly = false;

	@SuppressWarnings("null") public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("fly") && sender instanceof Player) {
			fly = true;
			Player player = (Player) sender;

			if (player.getGameMode() == GameMode.CREATIVE) {
				player.setAllowFlight(true);
			}
			new BukkitRunnable() {
				@Override public void run() {

					if (player.getGameMode() != GameMode.CREATIVE) {
						if (player.getLocation().getBlockY() >= 40.00) {
							if (fly == true) {
								player.setAllowFlight(!player.getAllowFlight());
								fly = false;
							}

						} else {
							player.setAllowFlight(false);
							errorFly = true;
						}

						if (errorFly == true) {
							player.sendMessage(plugin.redpref + "To enable fly, you must be above Y: 40");
							errorFly = false;
							this.cancel();
						}
					}
				}

			}.runTaskTimerAsynchronously(plugin, 0, 60);

			new BukkitRunnable() {
				@Override public void run() {
					if (player.getAllowFlight() == true && errorFly == false) {
						player.sendMessage(plugin.greenpref + "Fly has been enabled.");
					}
					if (player.getAllowFlight() == false && errorFly == false) {
						player.sendMessage(plugin.greenpref + "Fly has been disabled.");
					}

				}
			}.runTaskLaterAsynchronously(plugin, 5);

		}

		if (cmd.getName().equalsIgnoreCase("back") && sender != null) {
			Player player = (Player) sender;

			if (plugin.getConfig().contains("back." + player.getDisplayName())) {

				plugin.manager.saveInventory(player);
				player.teleport(plugin.manager.back);
				plugin.manager.setInventory(player);

				plugin.manager.setWorldMode(player);
				player.sendMessage(plugin.yellowpref + "Teleported to previous location.");
				return true;
			} else {
				player.sendMessage(plugin.redpref + "Unable to locate return location.");
				return true;
			}
		}

		if (cmd.getName().equalsIgnoreCase("balance") && sender != null) {
			int arg = args.length;
			Player player = (Player) sender;

			if (arg == 0) {
				// format bal
				player.sendMessage(plugin.greenpref + "Your balance is §e$" + String
						.format("%.2f", (cfg.getDouble("economy." + player.getDisplayName() + ".balance"))));
				return true;
			}
			if (arg == 1) {
				@SuppressWarnings("deprecation") Player rec = Bukkit.getPlayer(args[0]);

				// format bal
				player.sendMessage(plugin.greenpref + rec.getName() + "'s balance is §e$" + String
						.format("%.2f", (cfg.getDouble("economy." + rec.getName() + ".balance"))));
				return true;
			}

			if (arg >= 2) {
				player.sendMessage(plugin.redpref + "Correct usage: /balance [§eplayer§f]");
				return true;
			}

		}

		if (cmd.getName().equalsIgnoreCase("pay") && sender != null) {
			Player player = (Player) sender;

			int arg = args.length;

			if (arg == 0 || arg == 1 || arg >= 3) {
				player.sendMessage(plugin.redpref + "Correct usage: /pay [§eplayer§f] <§eamount§f>");
				return true;
			}

			if (arg == 2) {
				if (args[0].equalsIgnoreCase(player.getDisplayName())) {
					player.sendMessage(plugin.redpref + "Sorry, you can't pay yourself!");
					return true;
				} else {
					@SuppressWarnings("deprecation") Player rec = Bukkit.getPlayer(args[0]);
					if (rec != null) {
						double amnt = Double.parseDouble(args[1]);
						if (amnt <= cfg.getDouble("economy." + player.getDisplayName() + ".balance") && amnt > 0) {
							// add the money to recipients balance
							cfg.set("economy." + rec.getName() + ".balance",
									cfg.getDouble("economy." + rec.getName() + ".balance") + amnt);
							// remove money from payer
							cfg.set("economy." + player.getDisplayName() + ".balance",
									cfg.getDouble("economy." + player.getDisplayName() + ".balance") - amnt);

							player.sendMessage(
									plugin.greenpref + "Payment of §e" + args[1] + " §f has been delivered to " + rec
											.getName());
							rec.sendMessage(
									plugin.greenpref + "Payment of §e" + args[1] + " §f has been recieved from " + player
											.getName());

							plugin.saveConfig();

							plugin.manager.pScoreboard(player);
							plugin.manager.pScoreboard(rec);
						} else {
							player.sendMessage(plugin.redpref + "You do not have sufficient funds!");
						}


						return true;
					} else {
						player.sendMessage(plugin.redpref + "Player is not online.");
						return true;
					}
				}

			}
		}

		if (cmd.getName().equalsIgnoreCase("set") && sender != null) {
			Player player = (Player) sender;

			if (player.hasPermission("plugin.command.set")) {

				int arg = args.length;

				if (arg == 0 || arg == 1 || arg >= 3) {
					player.sendMessage(plugin.redpref + "Correct usage: /set [§eplayer§f] <§eamount§f>");
					return true;
				}

				if (arg == 2) {

					@SuppressWarnings("deprecation") Player rec = Bukkit.getPlayer(args[0]);
					if (rec != null) {
						// add the money to recipients balance
						cfg.set("economy." + rec.getName() + ".balance", Double.parseDouble(args[1]));

						// format bal
						rec.sendMessage(plugin.greenpref + "Balance has been set to §e$" + args[1] + " §fby " + player
								.getName());
						player.sendMessage(
								plugin.greenpref + rec.getName() + "'s balance has been set to §e$" + args[1]);

						plugin.saveConfig();

						plugin.manager.pScoreboard(rec);

						return true;
					} else {
						player.sendMessage(plugin.redpref + "Player is not online.");
						return true;
					}

				}
			} else {
				player.sendMessage(plugin.redpref + "You do not have permission to set balance.");
			}
		}

		if (cmd.getName().equalsIgnoreCase("world") && sender != null) {
			Player player = (Player) sender;

			int arg = args.length;

			if (arg == 0 || arg >= 2) {
				player.sendMessage(plugin.redpref + "Correct usage: /world <§ecreative§f:§esurvival§f:§ecity§f>");
				return true;
			}

			if (arg == 1) {
				if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("cr")) {
					Location location = new Location(Bukkit.getServer().createWorld(new WorldCreator("creative")),
							560.292, 57, -378.475);

					plugin.manager.saveInventory(player);
					player.teleport(location);
					plugin.manager.setInventory(player);

					plugin.manager.pScoreboard(player);
					plugin.manager.setWorldMode(player);

					player.sendMessage(plugin.yellowpref + "Teleported to creative world.");
					return true;
				} else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s")) {

					Location location = new Location(Bukkit.getServer().createWorld(new WorldCreator("survival")),
							cfg.getDouble("spawn.X"), cfg.getDouble("spawn.Y"), cfg.getDouble("spawn.Z"));

					plugin.manager.saveInventory(player);
					player.teleport(location);
					plugin.manager.setInventory(player);

					plugin.manager.setWorldMode(player);
					plugin.manager.pScoreboard(player);

					player.sendMessage(plugin.yellowpref + "Teleported to survival world.");

					return true;
				} else if (args[0].equalsIgnoreCase("city") || args[0].equalsIgnoreCase("c")) {
					Location location = new Location(Bukkit.getServer().createWorld(new WorldCreator("city")), 560.6,
							65, 308.5);

					plugin.manager.saveInventory(player);
					player.teleport(location);
					plugin.manager.setInventory(player);

					plugin.manager.pScoreboard(player);
					plugin.manager.setWorldMode(player);

					player.sendMessage(plugin.yellowpref + "Teleported to city world.");
					return true;
				} else if (args[0].equalsIgnoreCase("pvp") || args[0].equalsIgnoreCase("1v1")) {

					Location location = new Location(Bukkit.getServer().createWorld(new WorldCreator("pvp")),
							-1858.569, 93, 1130.811, 177, -2);

					plugin.manager.saveInventory(player);
					player.teleport(location);
					plugin.manager.setInventory(player);

					plugin.manager.setWorldMode(player);
					plugin.manager.pScoreboard(player);

					player.sendMessage(plugin.yellowpref + "Teleported to PvP world.");

					return true;
				} else {
					player.sendMessage(plugin.redpref + "Correct usage: /world <§ecreative§f:§esurvival§f:§ecity§f>");
					return true;
				}
			}

		}

		if (cmd.getName().equalsIgnoreCase("clearlag") && sender != null) {

			List<World> worlds = Bukkit.getServer().getWorlds();

			for (World world : worlds) {
				List<Entity> entList = world.getEntities();

				for (Entity current : entList) {
					if (current instanceof Item) {
						current.remove();
					}
				}
			}

			Bukkit.broadcastMessage(plugin.yellowpref + "Cleared all entities.");

			return true;
		}

		if (cmd.getName().equalsIgnoreCase("chest") && sender != null) {
			Player player = (Player) sender;

			Location playerLoc = player.getLocation();
			Location chestLoc = new Location(playerLoc.getWorld(), playerLoc.getX(), playerLoc.getY() - 1, playerLoc.getZ());
			Location chestLoc1 = new Location(playerLoc.getWorld(), playerLoc.getX() - 1, playerLoc.getY() - 1, playerLoc.getZ());

			chestLoc.getBlock().setType(Material.CHEST);
			chestLoc1.getBlock().setType(Material.CHEST);

			Chest chest = (Chest) chestLoc.getBlock().getState();

			ItemStack[] playerInv = player.getInventory().getContents();

			for(ItemStack stack : playerInv) {
				if(stack != null) {
					chest.getInventory().addItem(stack);
				}
			}

			player.getInventory().clear();

			player.sendMessage(plugin.greenpref + "Inventory saved!");
		}

		if (cmd.getName().equalsIgnoreCase("kit") && sender != null) {
			Player player = (Player) sender;

			PlayerInventory i = player.getInventory();

			ItemStack sword = new ItemStack(Material.STONE_SWORD);
			i.addItem(sword);

			ItemStack pic = new ItemStack(Material.STONE_PICKAXE);
			i.addItem(pic);

			ItemStack axe = new ItemStack(Material.STONE_AXE);
			i.addItem(axe);

			ItemStack shovel = new ItemStack(Material.STONE_SPADE);
			i.addItem(shovel);

			ItemStack hoe = new ItemStack(Material.STONE_HOE);
			i.addItem(hoe);

			ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
			i.addItem(helm);

			ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			i.addItem(chest);

			ItemStack leg = new ItemStack(Material.LEATHER_LEGGINGS);
			i.addItem(leg);

			ItemStack boot = new ItemStack(Material.LEATHER_BOOTS);
			i.addItem(boot);

			i.addItem(new ItemStack(Material.COOKED_BEEF, 32));

			player.sendMessage(plugin.greenpref + "Starter kit has been loaded succesfully!");

		}

		if (cmd.getName().equalsIgnoreCase("setspawn") && sender != null) {
			Player player = (Player) sender;

			if (player.hasPermission("plugin.command.setspawn")) {

				plugin.getConfig().set("spawn.world", player.getLocation().getWorld().getName());
				plugin.getConfig().set("spawn.X", player.getLocation().getX());
				plugin.getConfig().set("spawn.Y", player.getLocation().getY());
				plugin.getConfig().set("spawn.Z", player.getLocation().getZ());
				plugin.getConfig().set("spawn.yaw", player.getLocation().getYaw());
				plugin.getConfig().set("spawn.pitch", player.getLocation().getPitch());
				plugin.saveConfig();
				player.sendMessage(plugin.greenpref + "Spawn has been set.");
				return true;
			} else {
				player.sendMessage(plugin.redpref + "You do not have permission to set world spawn.");
			}
		}

		if (cmd.getName().equalsIgnoreCase("spawn") && sender != null) {
			Player player = (Player) sender;

			if (plugin.getConfig().contains("spawn")) {
				Location spawn = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("spawn.world")),
						plugin.getConfig().getDouble("spawn.X"), plugin.getConfig().getDouble("spawn.Y"),
						plugin.getConfig().getDouble("spawn.Z"), (float) plugin.getConfig().getDouble("spawn.yaw"),
						(float) plugin.getConfig().getDouble("spawn.pitch"));
				plugin.manager.saveInventory(player);
				player.teleport(spawn);
				plugin.manager.setInventory(player);

				player.sendMessage(plugin.yellowpref + "Teleported to spawn.");
				plugin.manager.pScoreboard(player);
				plugin.manager.setWorldMode(player);
				return true;
			} else {
				player.sendMessage(plugin.redpref + "Unable to locate spawn.");
				return true;
			}
		}

		if (cmd.getName().equalsIgnoreCase("sethome") && sender != null) {
			Player player = (Player) sender;

			plugin.getConfig().set("home." + player.getDisplayName() + ".world", player.getLocation().getWorld().getName());
			plugin.getConfig().set("home." + player.getDisplayName() + ".X", player.getLocation().getX());
			plugin.getConfig().set("home." + player.getDisplayName() + ".Y", player.getLocation().getY());
			plugin.getConfig().set("home." + player.getDisplayName() + ".Z", player.getLocation().getZ());
			plugin.getConfig().set("home." + player.getDisplayName() + ".yaw", player.getLocation().getYaw());
			plugin.getConfig().set("home." + player.getDisplayName() + ".pitch", player.getLocation().getPitch());
			plugin.saveConfig();
			player.sendMessage(plugin.greenpref + "Home has been set.");
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("home") && sender != null) {
			Player player = (Player) sender;

			if (plugin.getConfig().contains("home." + player.getDisplayName())) {
				Location home = new Location(Bukkit.getServer()
						.getWorld(plugin.getConfig().getString("home." + player.getDisplayName() + ".world")),
						plugin.getConfig().getDouble("home." + player.getDisplayName() + ".X"),
						plugin.getConfig().getDouble("home." + player.getDisplayName() + ".Y"),
						plugin.getConfig().getDouble("home." + player.getDisplayName() + ".Z"),
						(float) plugin.getConfig().getDouble("home." + player.getDisplayName() + ".yaw"),
						(float) plugin.getConfig().getDouble("home." + player.getDisplayName() + ".pitch"));
				plugin.manager.saveInventory(player);
				player.teleport(home);
				plugin.manager.setInventory(player);

				plugin.manager.setWorldMode(player);
				player.sendMessage(plugin.yellowpref + "Teleported to home.");
				plugin.manager.pScoreboard(player);
				return true;
			} else {
				player.sendMessage(plugin.redpref + "Unable to locate home.");
			}
		}

		if (cmd.getName().equalsIgnoreCase("broadcast")) {
			Player player = (Player) sender;

			if (player.hasPermission("plugin.command.broadcast")) {

				int arg = args.length;

				if (arg == 0) {
					player.sendMessage(plugin.redpref + "Correct usage: /broadcast <§emessage§f>");
					return true;
				} else {
					String text = "";
					for (int i = 0; i < args.length; i++) {
						text += args[i] + " ";
					}
					Bukkit.getServer().broadcastMessage(
							ChatColor.BLUE + "" + ChatColor.BOLD + "OWNER " + ChatColor.BLUE + "Server"
									+ ChatColor.WHITE + ": " + text);
				}
			} else {
				player.sendMessage(plugin.redpref + "You do not have permission to broadcast a message.");
			}
		}

		if (cmd.getName().equalsIgnoreCase("shop") && sender != null) {
			Player player = (Player) sender;

			int arg = args.length;
			plugin.manager.vplayer = player;

			if (arg == 0) {
				plugin.manager.mainShop(player);
				return true;
			}

			if (arg >= 3) {
				player.sendMessage(plugin.shopred
						+ "Correct usage: /shop <§efarm§f:§espawner§f:§emob§f:§eore§f:§efood§f:§eblock§f>");
				return true;
			}

			if (arg == 2) {
				if (args[0].equalsIgnoreCase("setup") && args[1].equalsIgnoreCase("villagers")) {
					plugin.manager.setUpVillagers();
					return true;

				} else {
					player.sendMessage(plugin.shopred
							+ "Correct usage: /shop <§efarm§f:§espawner§f:§emob§f:§eore§f:§efood§f:§eblock§f>");
					return true;
				}
			}

			if (arg == 1) {
				if (args[0].equalsIgnoreCase("farm")) {
					plugin.manager.farmShop(player);
					player.sendMessage(plugin.shopgreen + "Loaded farm shop.");
					return true;
				}
				if (args[0].equalsIgnoreCase("ore")) {
					plugin.manager.oreShop(player);
					player.sendMessage(plugin.shopgreen + "Loaded ore shop.");
					return true;
				}
				if (args[0].equalsIgnoreCase("food")) {
					plugin.manager.foodShop(player);
					player.sendMessage(plugin.shopgreen + "Loaded food shop.");
					return true;
				}
				if (args[0].equalsIgnoreCase("block")) {
					plugin.manager.blockShop(player);
					player.sendMessage(plugin.shopgreen + "Loaded block shop.");
					return true;
				}
				if (args[0].equalsIgnoreCase("spawner")) {
					plugin.manager.spawnerShop(player);
					player.sendMessage(plugin.shopgreen + "Loaded spawner shop.");
					return true;
				}
				if (args[0].equalsIgnoreCase("mob")) {
					plugin.manager.mobShop(player);
					player.sendMessage(plugin.shopgreen + "Loaded mob drops shop.");
					return true;
				}
				if (!(args[0].equalsIgnoreCase("farm")) && !(args[0].equalsIgnoreCase("spawner")) && !(args[0]
						.equalsIgnoreCase("mob"))) {
					player.sendMessage(plugin.shopred
							+ "Correct usage: /shop <§efarm§f:§espawner§f:§emob§f:§eore§f:§efood§f:§eblock§f>");
					return true;
				}
			}
			return true;

		}

		return true;

	}

}
