package me.bukkit.survival;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@SuppressWarnings({ "ALL", "WeakerAccess" }) public class Mechanics implements Listener {

	private final Main plugin = Main.getPlugin(Main.class);
	private final FileConfiguration cfg = plugin.getConfig();
	private String test = plugin.test;
	private int number = 5;
	private int nullslot = 0;

		/*

	BACK HANDLER

	 */

	@SuppressWarnings("deprecation") @EventHandler public void onCommandExecute(PlayerCommandPreprocessEvent event) {

		Player player = event.getPlayer();

		String command = event.getMessage();
		if (command.split(" ")[0].equalsIgnoreCase("/world") || command.equalsIgnoreCase("/spawn") || command
				.equalsIgnoreCase("/home")) {

			plugin.manager.back(player);

			plugin.manager.pScoreboard(player);

			plugin.manager.setWorldMode(player);

		} else {
			return;
		}

	}

	/*

	END BACK HANDLER

	 */

	@EventHandler public void fallDmg(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
			event.setCancelled(true);
		}
	}

	/*

	TELEPORT COMMAND

	 */

	@SuppressWarnings("deprecation") @EventHandler public void onCommandExecuteTP(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String command = event.getMessage();

		if (command.split(" ")[0].equalsIgnoreCase("/tp")) {
			event.setCancelled(true);

			if (player.hasPermission("plugin.command.tp")) {

				String[] commands = command.split(" ");
				int args = commands.length;

				if (args >= 4 || args == 1) {
					player.sendMessage(plugin.redpref + "Correct usage: /tp <§eplayer§f> (and/or) <§eplayer2§f>");
				}

				if (args == 2) {

					if (!(commands[1].equals(player.getDisplayName()))) {

						Player p = (Player) Bukkit.getPlayer(command.split(" ")[1]);
						if (p != null) {

							plugin.manager.saveInventory(player);
							player.teleport(p.getLocation());
							plugin.manager.setInventory(player);
							plugin.manager.setWorldMode(player);
							plugin.manager.back(player);

							player.sendMessage(plugin.yellowpref + "Teleported to " + p.getName() + ".");
							p.sendMessage(plugin.yellowpref + player.getDisplayName() + " teleported to you.");

						} else {
							player.sendMessage(plugin.redpref + "Player could not be located.");
						}

					} else {
						player.sendMessage(plugin.redpref + "You can't teleport to yourself.");
					}

				}

				if (args == 3) {
					Player p = (Player) Bukkit.getPlayer(command.split(" ")[1]);
					Player p1 = (Player) Bukkit.getPlayer(command.split(" ")[2]);
					if (p != null && p1 != null) {

						plugin.manager.saveInventory(p);
						p.teleport(p1.getLocation());
						plugin.manager.setInventory(p);
						plugin.manager.setWorldMode(p);
						plugin.manager.back(p);

						p.sendMessage(plugin.yellowpref + "Teleported to " + p1.getName() + ".");
						p1.sendMessage(plugin.yellowpref + p.getName() + " teleported to you.");

					} else {
						player.sendMessage(plugin.redpref + "Player could not be located");
					}
				}
			} else {
				player.sendMessage(plugin.redpref + "You do not have permission to teleport.");
			}

		}

	}

	/*

	END TELEPORT COMMAND

	 */


	/*

	SPAWNER HANDLER

	 */

	@EventHandler public void onBlockPlaceEvent(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		ItemStack item = p.getInventory().getItemInHand();

		if (event.getBlock().getType().equals(Material.MOB_SPAWNER)) {

			if (item.hasItemMeta()) {
				if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Blaze Spawner")) {
					plugin.manager.setSpawner(event.getBlockPlaced(), ChatColor.GOLD + "Blaze Spawner");
				} else if (item.getItemMeta().getDisplayName()
						.equalsIgnoreCase(ChatColor.GOLD + "Iron Golem Spawner")) {
					plugin.manager.setSpawner(event.getBlockPlaced(), ChatColor.GOLD + "Iron Golem Spawner");
				} else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Creeper Spawner")) {
					plugin.manager.setSpawner(event.getBlockPlaced(), ChatColor.GOLD + "Creeper Spawner");
				} else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Enderman Spawner")) {
					plugin.manager.setSpawner(event.getBlockPlaced(), ChatColor.GOLD + "Enderman Spawner");
				} else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Zombie Spawner")) {
					plugin.manager.setSpawner(event.getBlockPlaced(), ChatColor.GOLD + "Zombie Spawner");
				} else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Skeleton Spawner")) {
					plugin.manager.setSpawner(event.getBlockPlaced(), ChatColor.GOLD + "Skeleton Spawner");
				} else if (item.getItemMeta().getDisplayName()
						.equalsIgnoreCase(ChatColor.GOLD + "Zombie Pigman Spawner")) {
					plugin.manager.setSpawner(event.getBlockPlaced(), ChatColor.GOLD + "Zombie Pigman Spawner");
				} else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Pig Spawner")) {
					plugin.manager.setSpawner(event.getBlockPlaced(), ChatColor.GOLD + "Pig Spawner");
				} else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Cow Spawner")) {
					plugin.manager.setSpawner(event.getBlockPlaced(), ChatColor.GOLD + "Cow Spawner");
				}
			}
		}

	}
	/*

	END SPAWNER HANDLER

	 */

	/*

	DROP HANDLER

	 */

	@EventHandler public void onEntityDeath(EntityDeathEvent event) {
		Entity e = event.getEntity();
		List<ItemStack> drops = event.getDrops();

		// cow chicken pig mooshroom rabbit sheep

		if (event.getEntity().getKiller() instanceof Player && !(e instanceof Player)) {

			Player killer = event.getEntity().getKiller();

			if (e instanceof Cow || e instanceof MushroomCow) {

				eDrop(killer, new ItemStack(Material.COOKED_BEEF), new ItemStack(Material.RAW_BEEF), drops);

			} else if (e instanceof Chicken) {

				eDrop(killer, new ItemStack(Material.COOKED_CHICKEN), new ItemStack(Material.RAW_CHICKEN), drops);

			} else if (e instanceof Pig) {

				eDrop(killer, new ItemStack(Material.GRILLED_PORK), new ItemStack(Material.PORK), drops);

			} else if (e instanceof Rabbit) {

				eDrop(killer, new ItemStack(Material.COOKED_RABBIT), new ItemStack(Material.RABBIT), drops);

			} else if (e instanceof Sheep) {

				eDrop(killer, new ItemStack(Material.COOKED_MUTTON), new ItemStack(Material.MUTTON), drops);

			} else {
				eDrop(killer, drops);
			}

			event.getDrops().clear();

		}

	}
	// TODO redo
	public void eDrop(Player player, ItemStack replaceStack, ItemStack findStack, List<ItemStack> list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			ItemStack stack = list.get(i);
			if (stack.getType() == findStack.getType()) {
				stack = new ItemStack(replaceStack.getType(), stack.getAmount());
				list.remove(i);
				list.add(i, stack);
			}
			player.getInventory().addItem(stack);
		}
	}

	public void eDrop(Player player, List<ItemStack> list) {
		for (ItemStack stack : list) {

			player.getInventory().addItem(stack);

		}
	}

	/*

	END DROP HANDLER

	 */

	/*

	JOIN AND QUIT HANDLER

	 */

	@EventHandler public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		ChatColor color = getColor(player);
		e.setJoinMessage(plugin.yellowpref + player.getDisplayName() + " has joined the game.");
		plugin.manager.villagerLocationMode.put(player.getDisplayName(), false);

		if (player.getDisplayName().equalsIgnoreCase("abguneta") || player.getDisplayName().equalsIgnoreCase("coolkidarnie")) {
			player.setPlayerListName(color + "" + ChatColor.BOLD + "OP " + ChatColor.YELLOW + player.getDisplayName() + ChatColor.WHITE);
		} else {
			player.setPlayerListName(
					color + "" + ChatColor.BOLD + "MEMBER " + color + player.getDisplayName() + ChatColor.WHITE);
		}
		// economy
		if (!cfg.contains("economy." + player.getDisplayName() + ".balance")) {
			cfg.set("economy." + player.getDisplayName() + ".balance", 2000);
			plugin.saveConfig();
		}

		if (!cfg.contains("inventory." + player.getDisplayName() + "." + player.getWorld().getName() + "")) {
			cfg.set("inventory." + player.getDisplayName() + "." + player.getWorld().getName() + "", "36;");
			plugin.saveConfig();
		} else {
			plugin.manager.setInventory(player);
		}

		new BukkitRunnable() {
			@Override public void run() {
				plugin.manager.setWorldMode(player);
			}
		}.runTaskLaterAsynchronously(plugin, 5);

		plugin.manager.pScoreboard(player);

	}

	@EventHandler public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		e.setQuitMessage(
				plugin.yellowpref + ChatColor.BOLD + player.getDisplayName() + ChatColor.WHITE + " has left the game.");

		plugin.manager.saveInventory(player);

	}

	/*

	END JOIN AND QUIT HANDLER

	 */


	/*

	CHAT HANDLER

	 */

	@EventHandler public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		ChatColor color = getColor(player);

		if (plugin.manager.villagerLocationMode.get(player.getDisplayName()) == true) {
			plugin.manager.setup = event.getMessage();
			event.setCancelled(true);
			if (plugin.manager.setup.equalsIgnoreCase("CONTINUE")) {
				player.sendMessage(plugin.shopyellow + "To pick a location, left-click the desired block");
				plugin.manager.cont = true;
			/*
			vplayer.sendMessage(plugin.shopyellow + locationNumber + "/6 are required.");
			// at end set false
			if(locationNumber <= 0) {
				villagerLocationMode.put(vplayer.getDisplayName(), false);
			}*/

			} else if (plugin.manager.setup.equalsIgnoreCase("EXIT")) {
				player.sendMessage(plugin.shopred + "Villager setup was cancelled.");
				plugin.manager.villagerLocationMode.put(player.getDisplayName(), false);
			} else if (plugin.manager.setup.equalsIgnoreCase("EAST")) {
				plugin.manager.villagerLocationMode.put(player.getDisplayName(), false);
				plugin.manager.setUpVillagers();

			} else if (plugin.manager.setup.equalsIgnoreCase("WEST")) {
				plugin.manager.villagerLocationMode.put(player.getDisplayName(), false);
				plugin.manager.setUpVillagers();

			} else if (plugin.manager.setup.equalsIgnoreCase("NORTH")) {
				plugin.manager.villagerLocationMode.put(player.getDisplayName(), false);
				plugin.manager.setUpVillagers();

			} else if (plugin.manager.setup.equalsIgnoreCase("SOUTH")) {
				plugin.manager.villagerLocationMode.put(player.getDisplayName(), false);
				plugin.manager.setUpVillagers();

			} else {
				cfg.set("shop.villagers", "");
				plugin.saveConfig();
			}
		}

		if (player.getDisplayName().equalsIgnoreCase("coolkidarnie") || player.getDisplayName()
				.equalsIgnoreCase("abguneta")) {
			event.setFormat(color + "" + ChatColor.BOLD + "OP " + ChatColor.YELLOW + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
		} else {
			event.setFormat(color + "" + ChatColor.BOLD + "MEMBER " + color + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
		}
	}

	public ChatColor getColor(Player player) {
		if (player.getDisplayName().equals("abguneta")) {
			return ChatColor.AQUA;
		} else if (player.getDisplayName().equals("coolkidarnie")) {
			return ChatColor.RED;
		} else {
			return ChatColor.YELLOW;
		}
	}

	/*

	END CHAT HANDLER

	 */

	/*

	RESPAWN AND DEATH HANDLER

	 */

	@EventHandler public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();

		new BukkitRunnable() {
			@Override public void run() {
				plugin.manager.setInventory(player);
				plugin.manager.setWorldMode(player);
			}
		}.runTaskLaterAsynchronously(plugin, 10);
	}

	@EventHandler public void onDeathEvent(PlayerDeathEvent event) {
		Player player = (Player) event.getEntity();

		plugin.manager.back(player);

		plugin.manager.pScoreboard(player);

		if (player.getWorld().getName().contains("survival")) {

			cfg.set("inventory." + player.getDisplayName() + "." + "survival" + "", "36;");

		} else {

			cfg.set("inventory." + player.getDisplayName() + "." + player.getWorld().getName() + "", "36;");

		}

		plugin.saveConfig();

		plugin.manager.setInventory(player);

	}

	/*

	END RESPAWN AND DEATH HANDLERS

	 */

	/*

	SHOP HANDLER

	 */

	@EventHandler public void onInventoryClick(InventoryClickEvent e) {

		Player player = (Player) e.getWhoClicked();
		PlayerInventory i = player.getInventory();

		if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !e.getCurrentItem()
				.hasItemMeta()) {
			return;
		}
		if (e.getInventory().getName().equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "Shops Menu")) {
			e.setCancelled(true);

			switch (e.getCurrentItem().getType()) {

			case STONE:
				plugin.manager.blockShop(player);
				break;
			case COOKED_BEEF:
				plugin.manager.foodShop(player);
				break;
			case DIAMOND_ORE:
				plugin.manager.oreShop(player);
				break;
			case ROTTEN_FLESH:
				plugin.manager.mobShop(player);
				break;
			case MOB_SPAWNER:
				plugin.manager.spawnerShop(player);
				break;
			case IRON_HOE:
				plugin.manager.farmShop(player);
				break;
			case ENCHANTMENT_TABLE:
				player.sendMessage(plugin.shopgreen + "Shop coming soon.");
				new BukkitRunnable() {
					@Override public void run() {
						player.closeInventory();
					}
				}.runTaskLater(plugin, 10);
				break;
			default:
				break;
			}

		}

		if (e.getInventory().getName().equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "Spawner Shop")) {
			e.setCancelled(true);

			switch (e.getCurrentItem().getType()) {
			// cow pig zombie skeleton enderman zombiepigman creeper ig blaze
			case MOB_SPAWNER:
				if (e.getClick().isRightClick()) {
					if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Cow Spawner")) {
						plugin.manager.sellItem(player,
								plugin.manager.shopStack(new ItemStack(Material.MOB_SPAWNER), "cow spawner"), "spawner",
								"cow");
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Pig Spawner")) {
						plugin.manager.sellItem(player,
								plugin.manager.shopStack(new ItemStack(Material.MOB_SPAWNER), "pig spawner"), "spawner",
								"pig");
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Zombie Spawner")) {
						plugin.manager.sellItem(player,
								plugin.manager.shopStack(new ItemStack(Material.MOB_SPAWNER), "zombie spawner"),
								"spawner", "zombie");
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Skeleton Spawner")) {
						plugin.manager.sellItem(player,
								plugin.manager.shopStack(new ItemStack(Material.MOB_SPAWNER), "skeleton spawner"),
								"spawner", "skeleton");
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Zombie Pigman Spawner")) {
						plugin.manager.sellItem(player,
								plugin.manager.shopStack(new ItemStack(Material.MOB_SPAWNER), "zombie pigman spawner"),
								"spawner", "pzombie");
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Enderman Spawner")) {
						plugin.manager.sellItem(player,
								plugin.manager.shopStack(new ItemStack(Material.MOB_SPAWNER), "enderman spawner"),
								"spawner", "enderman");
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Creeper Spawner")) {
						plugin.manager.sellItem(player,
								plugin.manager.shopStack(new ItemStack(Material.MOB_SPAWNER), "creeper spawner"),
								"spawner", "creeper");
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Iron Golem Spawner")) {
						plugin.manager.sellItem(player,
								plugin.manager.shopStack(new ItemStack(Material.MOB_SPAWNER), "iron golem spawner"),
								"spawner", "golem");
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Blaze Spawner")) {
						plugin.manager.sellItem(player,
								plugin.manager.shopStack(new ItemStack(Material.MOB_SPAWNER), "blaze spawner"),
								"spawner", "blaze");
					}
				} else if (e.getClick().isLeftClick()) {

					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					ItemMeta meta = stack.getItemMeta();
					meta.setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName());
					stack.setItemMeta(meta);

					if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Cow Spawner")) {
						plugin.manager.confirmInv(player);

						player.getOpenInventory().setItem(13, stack);

						new BukkitRunnable() {
							@Override public void run() {

								if (plugin.manager.confirm == true) {
									plugin.manager.buyItem(player, stack, "spawner", "cow");

									this.cancel();
									plugin.manager.close = true;
									number = 5;
									plugin.manager.confirm = false;
									return;
								}
								number--;
								if (number <= 0 || plugin.manager.close == true) {
									this.cancel();
									number = 5;
									plugin.manager.close = true;
									plugin.manager.confirm = false;
									return;
								}
							}
						}.runTaskTimerAsynchronously(plugin, 10, 20);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Pig Spawner")) {
						plugin.manager.confirmInv(player);

						player.getOpenInventory().setItem(13, stack);

						new BukkitRunnable() {
							@Override public void run() {

								if (plugin.manager.confirm == true) {
									plugin.manager.buyItem(player, stack, "spawner", "pig");

									this.cancel();
									plugin.manager.close = true;
									number = 5;
									plugin.manager.confirm = false;
									return;
								}
								number--;
								if (number <= 0 || plugin.manager.close == true) {
									this.cancel();
									number = 5;
									plugin.manager.close = true;
									plugin.manager.confirm = false;
									return;
								}
							}
						}.runTaskTimerAsynchronously(plugin, 10, 20);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Zombie Spawner")) {
						plugin.manager.confirmInv(player);

						player.getOpenInventory().setItem(13, stack);

						new BukkitRunnable() {
							@Override public void run() {

								if (plugin.manager.confirm == true) {
									plugin.manager.buyItem(player, stack, "spawner", "zombie");

									this.cancel();
									plugin.manager.close = true;
									number = 5;
									plugin.manager.confirm = false;
									return;
								}
								number--;
								if (number <= 0 || plugin.manager.close == true) {
									this.cancel();
									number = 5;
									plugin.manager.close = true;
									plugin.manager.confirm = false;
									return;
								}
							}
						}.runTaskTimerAsynchronously(plugin, 10, 20);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Skeleton Spawner")) {
						plugin.manager.confirmInv(player);

						player.getOpenInventory().setItem(13, stack);

						new BukkitRunnable() {
							@Override public void run() {

								if (plugin.manager.confirm == true) {
									plugin.manager.buyItem(player, stack, "spawner", "skeleton");

									this.cancel();
									plugin.manager.close = true;
									number = 5;
									plugin.manager.confirm = false;
									return;
								}
								number--;
								if (number <= 0 || plugin.manager.close == true) {
									this.cancel();
									number = 5;
									plugin.manager.close = true;
									plugin.manager.confirm = false;
									return;
								}
							}
						}.runTaskTimerAsynchronously(plugin, 10, 20);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Zombie Pigman Spawner")) {
						plugin.manager.confirmInv(player);

						player.getOpenInventory().setItem(13, stack);

						new BukkitRunnable() {
							@Override public void run() {

								if (plugin.manager.confirm == true) {
									plugin.manager.buyItem(player, stack, "spawner", "pzombie");

									this.cancel();
									plugin.manager.close = true;
									number = 5;
									plugin.manager.confirm = false;
									return;
								}
								number--;
								if (number <= 0 || plugin.manager.close == true) {
									this.cancel();
									number = 5;
									plugin.manager.close = true;
									plugin.manager.confirm = false;
									return;
								}
							}
						}.runTaskTimerAsynchronously(plugin, 10, 20);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Enderman Spawner")) {
						plugin.manager.confirmInv(player);

						player.getOpenInventory().setItem(13, stack);

						new BukkitRunnable() {
							@Override public void run() {

								if (plugin.manager.confirm == true) {
									plugin.manager.buyItem(player, stack, "spawner", "enderman");

									this.cancel();
									plugin.manager.close = true;
									number = 5;
									plugin.manager.confirm = false;
									return;
								}
								number--;
								if (number <= 0 || plugin.manager.close == true) {
									this.cancel();
									number = 5;
									plugin.manager.close = true;
									plugin.manager.confirm = false;
									return;
								}
							}
						}.runTaskTimerAsynchronously(plugin, 10, 20);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Creeper Spawner")) {
						plugin.manager.confirmInv(player);

						player.getOpenInventory().setItem(13, stack);

						new BukkitRunnable() {
							@Override public void run() {

								if (plugin.manager.confirm == true) {
									plugin.manager.buyItem(player, stack, "spawner", "creeper");

									this.cancel();
									plugin.manager.close = true;
									number = 5;
									plugin.manager.confirm = false;
									return;
								}
								number--;
								if (number <= 0 || plugin.manager.close == true) {
									this.cancel();
									number = 5;
									plugin.manager.close = true;
									plugin.manager.confirm = false;
									return;
								}
							}
						}.runTaskTimerAsynchronously(plugin, 10, 20);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Iron Golem Spawner")) {
						plugin.manager.confirmInv(player);

						player.getOpenInventory().setItem(13, stack);

						new BukkitRunnable() {
							@Override public void run() {

								if (plugin.manager.confirm == true) {
									plugin.manager.buyItem(player, stack, "spawner", "golem");

									this.cancel();
									plugin.manager.close = true;
									number = 5;
									plugin.manager.confirm = false;
									return;
								}
								number--;
								if (number <= 0 || plugin.manager.close == true) {
									this.cancel();
									number = 5;
									plugin.manager.close = true;
									plugin.manager.confirm = false;
									return;
								}
							}
						}.runTaskTimerAsynchronously(plugin, 10, 20);
					} else if (e.getCurrentItem().getItemMeta().getDisplayName()
							.equals(ChatColor.GOLD + "Blaze Spawner")) {
						plugin.manager.confirmInv(player);

						player.getOpenInventory().setItem(13, stack);

						new BukkitRunnable() {
							@Override public void run() {

								if (plugin.manager.confirm == true) {
									plugin.manager.buyItem(player, stack, "spawner", "blaze");

									this.cancel();
									plugin.manager.close = true;
									number = 5;
									plugin.manager.confirm = false;
									return;
								}
								number--;
								if (number <= 0 || plugin.manager.close == true) {
									this.cancel();
									number = 5;
									plugin.manager.close = true;
									plugin.manager.confirm = false;
									return;
								}
							}
						}.runTaskTimerAsynchronously(plugin, 10, 20);
					}
				}
				break;
			case NETHER_STAR:
				plugin.manager.mainShop(player);
				break;
			default:
				break;
			}

		}

		if (e.getInventory().getName().equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "Mob Drops Shop")) {
			e.setCancelled(true);

			switch (e.getCurrentItem().getType()) {
			// bone slime epearl gtear string feather gunpowder inksack rblaze rpotato rflesh spidereye wskull
			case BONE:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.BONE), "bone"), "mob",
									"bone");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {

							if (plugin.manager.confirm == true) {
								plugin.manager
										.buyItem(player, plugin.manager.shopStack(new ItemStack(Material.BONE), "bone"),
												"mob", "bone");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case SLIME_BALL:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.SLIME_BALL), "slime ball"), "mob", "slime");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {

							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.SLIME_BALL), "slime ball"),
										"mob", "slime");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case ENDER_PEARL:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.ENDER_PEARL), "ender pearl"), "mob",
							"epearl");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.ENDER_PEARL), "ender pearl"),
										"mob", "epearl");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case GHAST_TEAR:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.GHAST_TEAR), "ghast tear"), "mob", "gtear");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.GHAST_TEAR), "ghast tear"),
										"mob", "gtear");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case STRING:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.STRING), "string"), "mob",
									"string");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.STRING), "string"), "mob",
										"string");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case FEATHER:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.FEATHER), "feather"),
									"mob", "feather");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.FEATHER), "feather"), "mob",
										"feather");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case SULPHUR:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.SULPHUR), "gunpowder"),
									"mob", "gunpowder");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.SULPHUR), "gunpowder"), "mob",
										"gunpowder");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case INK_SACK:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.INK_SACK), "ink sack"),
									"mob", "inksack");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.INK_SACK), "ink sack"), "mob",
										"inksack");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case BLAZE_ROD:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.BLAZE_ROD), "blaze rod"),
									"mob", "rblaze");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.BLAZE_ROD), "blaze rod"), "mob",
										"rblaze");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case POISONOUS_POTATO:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.POISONOUS_POTATO), "poisonous potato"),
							"mob", "rpotato");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player, plugin.manager
												.shopStack(new ItemStack(Material.POISONOUS_POTATO), "poisonous potato"), "mob",
										"rpotato");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case ROTTEN_FLESH:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.ROTTEN_FLESH), "rotten flesh"), "mob",
							"rflesh");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.ROTTEN_FLESH), "rotten flesh"),
										"mob", "rflesh");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case SPIDER_EYE:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.SPIDER_EYE), "spider eye"), "mob",
							"spidereye");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.SPIDER_EYE), "spider eye"),
										"mob", "spidereye");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case SKULL_ITEM:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.SKULL_ITEM, (byte) 1), "wither skull"),
							"mob", "wskull");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player, plugin.manager
												.shopStack(new ItemStack(Material.SKULL_ITEM, (byte) 1), "wither skull"), "mob",
										"wskull");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case NETHER_STAR:
				plugin.manager.mainShop(player);
				break;
			default:
				break;
			}
		}

		if (e.getInventory().getName().equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "Ores Shop")) {
			e.setCancelled(true);

			switch (e.getCurrentItem().getType()) {
			//diamond iron gold lapis redstone flint coal emerald quartz
			case DIAMOND:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.DIAMOND), "diamond"),
									"ore", "diamond");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {

							if (plugin.manager.confirm == true) {

								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.DIAMOND), "diamond"), "ore",
										"diamond");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case IRON_INGOT:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.IRON_INGOT), "iron ingot"), "ore", "iron");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.IRON_INGOT), "iron ingot"),
										"ore", "iron");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case GOLD_INGOT:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.GOLD_INGOT), "gold ingot"), "ore", "gold");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.GOLD_INGOT), "gold ingot"),
										"ore", "gold");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case LAPIS_BLOCK:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.LAPIS_BLOCK), "lapis block"), "ore",
							"lapis");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.LAPIS_BLOCK), "lapis block"),
										"ore", "lapis");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case REDSTONE:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.REDSTONE), "redstone"),
									"ore", "redstone");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.REDSTONE), "redstone"), "ore",
										"redstone");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case FLINT:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.FLINT), "flint"), "ore",
									"flint");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.REDSTONE), "redstone"), "ore",
										"redstone");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case COAL:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.COAL), "coal"), "ore",
									"coal");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager
										.buyItem(player, plugin.manager.shopStack(new ItemStack(Material.COAL), "coal"),
												"ore", "coal");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case EMERALD:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.EMERALD), "emerald"),
									"ore", "emerald");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {

							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.EMERALD), "emerald"), "ore",
										"emerald");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case QUARTZ:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.QUARTZ), "quartz"), "ore",
									"nquartz");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.QUARTZ), "quartz"), "ore",
										"nquartz");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case NETHER_STAR:
				plugin.manager.mainShop(player);
				break;
			default:
				break;
			}
		}

		if (e.getInventory().getName().equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "Food Shop")) {
			e.setCancelled(true);

			switch (e.getCurrentItem().getType()) {
			// bread cpork gapple csalmon cfish cookie steak chicken bpotato pumpkinpie crabbit cmutton gcarrot
			case BREAD:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.BREAD), "bread"), "food",
									"bread");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.BREAD), "bread"), "food",
										"bread");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);

				}
				break;
			case GRILLED_PORK:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.GRILLED_PORK), "cooked porkchop"), "food",
							"cpork");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player, plugin.manager
												.shopStack(new ItemStack(Material.GRILLED_PORK), "cooked porkchop"), "food",
										"cpork");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case GOLDEN_APPLE:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.GOLDEN_APPLE), "golden apple"), "food",
							"gapple");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.GOLDEN_APPLE), "golden apple"),
										"food", "gapple");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case COOKED_FISH:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.COOKED_FISH), "cooked fish"), "food",
							"cfish");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.COOKED_FISH), "cooked fish"),
										"food", "cfish");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}

				break;
			case COOKIE:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.COOKIE), "cookies"),
							"food", "cookie");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.COOKIE), "cookies"), "food",
										"cookie");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case COOKED_BEEF:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.COOKED_BEEF), "steak"),
									"food", "steak");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.COOKED_BEEF), "steak"), "food",
										"steak");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case COOKED_CHICKEN:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.COOKED_CHICKEN), "cooked chicken"), "food",
							"chicken");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player, plugin.manager
												.shopStack(new ItemStack(Material.COOKED_CHICKEN), "cooked chicken"), "food",
										"chicken");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case BAKED_POTATO:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.BAKED_POTATO), "baked potato"), "food",
							"bpotato");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.BAKED_POTATO), "baked potato"),
										"food", "bpotato");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case PUMPKIN_PIE:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.PUMPKIN_PIE), "pumpkin pie"), "food",
							"pumpkinpie");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.PUMPKIN_PIE), "pumpkin pie"),
										"food", "pumpkinpie");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case COOKED_RABBIT:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.COOKED_RABBIT), "cooked rabbit"), "food",
							"crabbit");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player, plugin.manager
												.shopStack(new ItemStack(Material.COOKED_RABBIT), "cooked rabbit"), "food",
										"crabbit");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case COOKED_MUTTON:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.COOKED_MUTTON), "cooked mutton"), "food",
							"cmutton");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player, plugin.manager
												.shopStack(new ItemStack(Material.COOKED_MUTTON), "cooked mutton"), "food",
										"cmutton");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case GOLDEN_CARROT:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.GOLDEN_CARROT), "golden carrot"), "food",
							"gcarrot");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player, plugin.manager
												.shopStack(new ItemStack(Material.GOLDEN_CARROT), "golden carrot"), "food",
										"gcarrot");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case NETHER_STAR:
				plugin.manager.mainShop(player);
				break;
			default:
				break;
			}

		}

		if (e.getInventory().getName().equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "Block Shop")) {
			e.setCancelled(true);

			switch (e.getCurrentItem().getType()) {

			case NETHER_STAR:
				plugin.manager.mainShop(player);
				break;

			default:
				break;
			}
		}

		if (e.getInventory().getName().equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "Farm Shop")) {

			e.setCancelled(true);

			switch (e.getCurrentItem().getType()) {

			case NETHER_STAR:
				plugin.manager.mainShop(player);
				break;
			case APPLE:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.APPLE), "apple"), "farm",
									"apple");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.APPLE), "apple"), "farm",
										"apple");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case SUGAR_CANE:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.SUGAR_CANE), "sugar cane"), "farm", "cane");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.SUGAR_CANE), "sugar cane"),
										"farm", "cane");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case PUMPKIN:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.PUMPKIN), "pumpkin"),
									"farm", "pumpkin");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {

								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.PUMPKIN), "pumpkin"), "farm",
										"pumpkin");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case PUMPKIN_SEEDS:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.PUMPKIN_SEEDS), "pumpkin seeds"), "farm",
							"pumpkinseeds");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player, plugin.manager
												.shopStack(new ItemStack(Material.PUMPKIN_SEEDS), "pumpkin seeds"), "farm",
										"pumpkinseeds");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case CARROT_ITEM:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.CARROT_ITEM), "carrot"),
									"farm", "carrot");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.CARROT_ITEM), "carrot"), "farm",
										"carrot");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case POTATO_ITEM:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.POTATO_ITEM), "potato"),
									"farm", "potato");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.POTATO_ITEM), "potato"), "farm",
										"potato");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case MELON:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.MELON), "melon"), "farm",
									"melon");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.MELON), "melon"), "farm",
										"melon");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case MELON_SEEDS:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.MELON_SEEDS), "melon seeds"), "farm",
							"melonseeds");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.MELON_SEEDS), "melon seeds"),
										"farm", "melonseeds");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case WHEAT:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.WHEAT), "wheat"), "farm",
									"wheat");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.WHEAT), "wheat"), "farm",
										"wheat");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case SEEDS:
				if (e.getClick().isRightClick()) {
					plugin.manager
							.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.SEEDS), "seeds"), "farm",
									"seeds");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.SEEDS), "seeds"), "farm",
										"seeds");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case NETHER_STALK:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.NETHER_STALK), "nether wart"), "farm",
							"nwart");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.NETHER_STALK), "nether wart"),
										"farm", "nwart");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case CACTUS:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player, plugin.manager.shopStack(new ItemStack(Material.CACTUS), "cactus"),
							"farm", "cactus");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player,
										plugin.manager.shopStack(new ItemStack(Material.CACTUS), "cactus"), "farm",
										"cactus");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			case INK_SACK:
				if (e.getClick().isRightClick()) {
					plugin.manager.sellItem(player,
							plugin.manager.shopStack(new ItemStack(Material.INK_SACK, (byte) 3), "cocoa beans"), "farm",
							"cocoa");
				} else if (e.getClick().isLeftClick()) {
					ItemStack stack = e.getCurrentItem().getData().toItemStack(e.getCurrentItem().getAmount());
					plugin.manager.confirmInv(player);

					player.getOpenInventory().setItem(13, stack);

					new BukkitRunnable() {
						@Override public void run() {
							if (plugin.manager.confirm == true) {
								plugin.manager.buyItem(player, plugin.manager
												.shopStack(new ItemStack(Material.INK_SACK, (byte) 3), "cocoa beans"), "farm",
										"cocoa");
								this.cancel();
								plugin.manager.close = true;
								number = 5;
								plugin.manager.confirm = false;
								return;
							}
							number--;
							if (number <= 0 || plugin.manager.close == true) {
								this.cancel();
								number = 5;
								plugin.manager.close = true;
								plugin.manager.confirm = false;
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 10, 20);
				}
				break;
			default:

				break;
			}
			/*else if (e.getClick() == ClickType.MIDDLE) {

			}*/
		}

	}

	/*

	END SHOP HANDLER

	 */

	/*

	VILLAGER HANDLER

	 */

	@EventHandler public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		Entity entity = event.getRightClicked();
		Player player = event.getPlayer();
		if (entity instanceof Villager) {
			if (entity.getCustomName().equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "Block Shop")) {
				event.setCancelled(true);
				plugin.manager.blockShop(player);
			} else if (entity.getCustomName().equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "Food Shop")) {
				event.setCancelled(true);
				plugin.manager.foodShop(player);
			} else if (entity.getCustomName().equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "Ore Shop")) {
				event.setCancelled(true);
				plugin.manager.oreShop(player);
			} else if (entity.getCustomName().equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "Drop Shop")) {
				event.setCancelled(true);
				plugin.manager.mobShop(player);
			} else if (entity.getCustomName()
					.equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "Spawner Shop")) {
				event.setCancelled(true);
				plugin.manager.spawnerShop(player);
			} else if (entity.getCustomName().equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "Farm Shop")) {
				event.setCancelled(true);
				plugin.manager.farmShop(player);
			}
		}

	}

	@EventHandler public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (plugin.manager.villagerLocationMode.get(player.getDisplayName()) == true && plugin.manager.cont == true) {

			if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
				event.setCancelled(true);
				plugin.manager.locationNumber--;

				if (plugin.manager.locationNumber == 5) {
					Block block = event.getClickedBlock();
					plugin.manager.savingVillagerLocation(block, "blocks");
					player.sendMessage(plugin.shopgreen + "1/6 locations set.");
				} else if (plugin.manager.locationNumber == 4) {
					Block block = event.getClickedBlock();
					plugin.manager.savingVillagerLocation(block, "food");
					player.sendMessage(plugin.shopgreen + "2/6 locations set.");
				} else if (plugin.manager.locationNumber == 3) {
					Block block = event.getClickedBlock();
					plugin.manager.savingVillagerLocation(block, "ores");
					player.sendMessage(plugin.shopgreen + "3/6 locations set.");
				} else if (plugin.manager.locationNumber == 2) {
					Block block = event.getClickedBlock();
					plugin.manager.savingVillagerLocation(block, "mobdrops");
					player.sendMessage(plugin.shopgreen + "4/6 locations set.");
				} else if (plugin.manager.locationNumber == 1) {
					Block block = event.getClickedBlock();
					plugin.manager.savingVillagerLocation(block, "spawners");
					player.sendMessage(plugin.shopgreen + "5/6 locations set.");
				} else if (plugin.manager.locationNumber == 0) {
					Block block = event.getClickedBlock();
					plugin.manager.savingVillagerLocation(block, "farming");
					player.sendMessage(plugin.shopgreen + "6/6 locations set.");
				}

				if (plugin.manager.locationNumber <= 0) {
					IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("[{\"text\":\"§e§lSHOP > §fChoose face direction: \"},{\"text\":\"§a§lEAST\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"east\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click to make villagers face §a§lEAST\"}},{\"text\":\"§f, \"},{\"text\":\"§a§lWEST\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"west\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click to make villagers face §a§lWEST\"}},{\"text\":\"§f, \"},{\"text\":\"§a§lNORTH\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"north\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click to make villagers face §a§lNORTH\"}},{\"text\":\"§f, or \"},{\"text\":\"§a§lSOUTH\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"south\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click to make villagers face §a§lSOUTH\"}},{\"text\":\"§f.\"}]");
					PacketPlayOutChat chat = new PacketPlayOutChat(comp);
					((CraftPlayer)player).getHandle().playerConnection.sendPacket(chat);
					//plugin.manager.villagerLocationMode.put(player.getDisplayName(), false);
					plugin.manager.locationNumber = 6;
					plugin.manager.cont = false;
					//plugin.manager.setUpVillagers();
					return;
				}

			} else {
				player.sendMessage(plugin.shopred + "You can only left-click blocks.");
				return;
			}

		}

	}

	@EventHandler public void onVillagerDamage(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Villager && (
				entity.getCustomName().equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "Block Shop") || entity
						.getCustomName().equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "Food Shop")
						|| entity.getCustomName().equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "Ore Shop")
						|| entity.getCustomName().equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "Drop Shop")
						|| entity.getCustomName()
						.equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "Spawner Shop") || entity
						.getCustomName().equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "Farm Shop"))) {
			event.setCancelled(true);
		}
	}
	
	/*

	END VILLAGER HANDLER

	 */

	/*@EventHandler public void onInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		Player player = event.getPlayer();


		if ((action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && player.isSneaking()) {
			if (player.getItemInHand().getType().equals(Material.DIAMOND_PICKAXE)) {
				if (player.hasPermission("permission"))
					player.sendMessage(plugin.greenpref + "TEST 1");
				else
					player.sendMessage(plugin.redpref + "TEST 2");
			}

		}
	}*/

}
