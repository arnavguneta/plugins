package me.bukkit.survival;

import me.bukkit.survival.pvp.Battle;
import me.bukkit.survival.pvp.GameState;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "ALL", "WeakerAccess" }) public class Manager implements Listener {

	private final Main plugin = Main.getPlugin(Main.class);
	private final FileConfiguration cfg = plugin.getConfig();
	public Location back;
	public Boolean confirm;
	public Boolean close = false;
	int nullslot = 0;
	int number = 5;
	Location lBlock;
	Location lFood;
	Location lOres;
	// methods for inventory stacks
	Location lMob;
	Location lSpawner;
	Location lFarming;
	BukkitTask timer;
	Player vplayer;
	HashMap<String, Boolean> villagerLocationMode = new HashMap<>();
	String setup;
	int locationNumber = 6;
	String direction;
	Boolean cont;
	int lagcount = 300;
	private ItemStack shopItem;
	private Boolean foundItem = false;
	private String test = plugin.test;

	public static Location deserialize(Map<String, Object> m) {
		World w = Bukkit.getServer().getWorld((String) m.get("world"));
		if (w == null) {
			throw new IllegalArgumentException("non-existent world");
		}
		return new Location(w, (Double) m.get("x"), (Double) m.get("y"), (Double) m.get("z"));
	}

	/*

	END SCOREBOARD

	 */

	/*

	SHOP

	 */

	/*

	SCOREBOARD

	 */
	public void pScoreboard(Player player) {
		ScoreboardManager manager = Bukkit.getServer().getScoreboardManager();
		final Scoreboard b = manager.getNewScoreboard();
		final Objective o = b.registerNewObjective("score", "");

		o.setDisplaySlot(DisplaySlot.SIDEBAR);

		new BukkitRunnable() {
			@Override public void run() {
				if (player.getLocation().getWorld().getName().contains("survival")) {
					o.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Survival");

				} else if (player.getLocation().getWorld().getName().equalsIgnoreCase("creative")) {
					o.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Creative");
				} else if (player.getLocation().getWorld().getName().equalsIgnoreCase("city")) {
					o.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "City");
				} else if (player.getLocation().getWorld().getName().equalsIgnoreCase("pvp")) {
					o.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "PvP");
				}
				Score space2 = o.getScore(ChatColor.RED + "");
				space2.setScore(6);

				Score name = o.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Player: ");
				name.setScore(5);

				Score nameVal = o.getScore(ChatColor.WHITE + player.getDisplayName());
				nameVal.setScore(4);

				Score space = o.getScore(ChatColor.WHITE + "");
				space.setScore(3);

				Score bal = o.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Balance: ");
				bal.setScore(2);

				Score balAmount = o.getScore(ChatColor.WHITE + "$" + String
						.format("%.2f", (cfg.getDouble("economy." + player.getDisplayName() + ".balance"))));
				balAmount.setScore(1);

				Score space1 = o.getScore("");
				space1.setScore(0);

				if (plugin.pmanager.isInBattle(player)) {

					Battle b = plugin.pmanager.getBattle(player);

					if (plugin.pmanager.getState() == GameState.WAITING) {
						//7,8
						Score space3 = o.getScore(ChatColor.AQUA + "");
						space3.setScore(9);

						Score time = o.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Timer: ");
						time.setScore(8);

						Score timeVal = o.getScore(ChatColor.WHITE + String.valueOf(plugin.pmanager.getCount()));
						timeVal.setScore(7);

					} else if (plugin.pmanager.getState() == GameState.IN_GAME) {
						//7,8
						Score space3 = o.getScore(ChatColor.AQUA + "");
						space3.setScore(9);

						Score opp = o.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Opponent: ");
						opp.setScore(8);

						if (player.getDisplayName().equalsIgnoreCase(b.getPlayer1())) {
							Score oppVal = o.getScore(ChatColor.WHITE + b.getPlayer2());
							oppVal.setScore(7);
						} else if (player.getDisplayName().equalsIgnoreCase(b.getPlayer2())) {
							Score oppVal = o.getScore(ChatColor.WHITE + b.getPlayer1());
							oppVal.setScore(7);
						}

					}
				}

				player.setScoreboard(b);
			}
		}.runTaskLater(plugin, 10);

	}

	public ItemStack shopStack(ItemStack stack, String name) {
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		return stack;
	}

	public void buyItem(Player player, ItemStack istack, String shopType, String pathName) {
		String name = player.getDisplayName();
		PlayerInventory inv = player.getInventory();
		ItemStack[] contents = inv.getContents();
		ItemStack air = new ItemStack(Material.AIR);

		int amount = Integer.valueOf(cfg.get("shop." + shopType + "." + pathName + ".amount").toString());
		double balance = Double.valueOf(cfg.get("economy." + name + ".balance").toString());
		double price = Double.valueOf(cfg.get("shop." + shopType + "." + pathName + ".buy").toString());

		if (contents.length == 36) {
			for (int i = 0; i <= 35; i++) {
				if (inv.getItem(i) == null) {
					nullslot++;
				}
			}

			if (nullslot == 0) {
				player.sendMessage(plugin.shopred + "Your inventory is full!");
				return;
			}

			nullslot = 0;

		} else if (price > balance) {
			player.sendMessage(plugin.shopred + "You don't have enough money to buy this item.");
			return;
		}

		for (int i = 0; i <= 35; i++) {

			if (inv.getItem(i) == null) {
				if (cfg.contains("shop." + shopType + "." + pathName + ".damage")) {
					if (istack.hasItemMeta()) {
						ItemStack stackItem = new ItemStack(istack.getType(), amount);
						ItemMeta stackMeta = stackItem.getItemMeta();
						stackMeta.setDisplayName(istack.getItemMeta().getDisplayName());
						stackItem.setItemMeta(stackMeta);
						inv.setItem(i, new ItemStack(istack.getType(), amount,
								Byte.valueOf(cfg.get("shop." + shopType + "." + pathName + ".damage").toString())));
					} else {
						inv.setItem(i, new ItemStack(istack.getType(), amount,
								Byte.valueOf(cfg.get("shop." + shopType + "." + pathName + ".damage").toString())));
					}

				} else {

					if (istack.getItemMeta().getDisplayName().contains("Spawner")) {
						ItemStack stackItem = new ItemStack(istack.getType(), amount);
						ItemMeta stackMeta = stackItem.getItemMeta();
						stackMeta.setDisplayName(istack.getItemMeta().getDisplayName());
						stackItem.setItemMeta(stackMeta);
						inv.setItem(i, stackItem);

					} else {
						inv.setItem(i, new ItemStack(istack.getType(), amount));
					}

				}

				cfg.set("economy." + name + ".balance", balance - price);
				plugin.saveConfig();

				pScoreboard(player);

				player.sendMessage(
						plugin.shopgreen + "Bought " + ChatColor.RED + amount + " x " + ChatColor.RED + ChatColor
								.stripColor(istack.getItemMeta().getDisplayName()) + ChatColor.WHITE + " for "
								+ ChatColor.RED + "$" + (int) price + ".");
				return;
			}
		}
	}

	public void sellItem(Player player, ItemStack istack, String shopType, String pathName) {
		String name = player.getDisplayName();
		PlayerInventory inv = player.getInventory();

		int amount = Integer.valueOf(cfg.get("shop." + shopType + "." + pathName + ".amount").toString());
		double balance = Double.valueOf(cfg.get("economy." + name + ".balance").toString());
		double price = Double.valueOf(cfg.get("shop." + shopType + "." + pathName + ".sell").toString());

		foundItem = false;
		for (int i = 0; i <= 35; i++) {
			ItemStack searchStack = inv.getItem(i);

			if (searchStack == null) {

			} else if ((istack.getType() == searchStack.getType()) && (searchStack.getAmount() >= amount)) {
				int remainingAmount = searchStack.getAmount() - amount;
				inv.clear(i);
				if (remainingAmount > 0) {
					ItemStack addBack;
					if (cfg.contains("shop." + shopType + "." + pathName + ".damage")) {
						addBack = new ItemStack(searchStack.getType(), remainingAmount,
								Byte.valueOf(cfg.get("shop." + shopType + "." + pathName + ".damage").toString()));
					} else {
						addBack = new ItemStack(searchStack.getType(), remainingAmount);
					}

					inv.setItem(i, addBack);
				}

				cfg.set("economy." + name + ".balance", balance + price);
				plugin.saveConfig();

				pScoreboard(player);

				player.sendMessage(
						plugin.shopgreen + "Sold " + ChatColor.GOLD + amount + "x " + ChatColor.WHITE + istack
								.getItemMeta().getDisplayName() + ".");

				foundItem = true;
				return;
			}
		}

		if (foundItem == false) {
			player.sendMessage(
					plugin.shopred + "Could not find " + ChatColor.GOLD + amount + "x " + ChatColor.WHITE + istack
							.getItemMeta().getDisplayName() + ".");
		}
	}

	public void mainShop(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.RED + "" + ChatColor.BOLD + "Shops Menu");

		shopItem = new ItemStack(Material.IRON_HOE);
		createStack(inv, shopItem, "Farming", "farm", 15);
		shopItem = new ItemStack(Material.MOB_SPAWNER);
		createStack(inv, shopItem, "Spawner", "spawner", 14);
		shopItem = new ItemStack(Material.ROTTEN_FLESH);
		createStack(inv, shopItem, "Mob Drops", "mob drops", 13);
		shopItem = new ItemStack(Material.DIAMOND_ORE);
		createStack(inv, shopItem, "Ores", "ores", 12);
		shopItem = new ItemStack(Material.COOKED_BEEF);
		createStack(inv, shopItem, "Food Shop", "food", 11);
		shopItem = new ItemStack(Material.STONE);
		createStack(inv, shopItem, "Blocks", "block", 10);
		shopItem = new ItemStack(Material.ENCHANTMENT_TABLE);
		createStack(inv, shopItem, "Coming soon", "misc", 16);

		player.openInventory(inv);
	}

	public void confirmInv(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.RED + "" + ChatColor.BOLD + "Confirm");

		plugin.manager.confirm = false;
		plugin.manager.close = false;

		shopItem = new ItemStack(Material.STAINED_GLASS, 1, (byte) 5);
		createStack(inv, shopItem, 12);

		shopItem = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);
		createStack(inv, shopItem, 14);

		new BukkitRunnable() {
			@Override public void run() {

				if (plugin.manager.close == true) {
					this.cancel();
					number = 5;
					return;
				}

				if (number == 5 || number == 3 || number == 1) {
					shopItem = new ItemStack(Material.BEDROCK, number);
				} else if (number == 4 || number == 2) {
					shopItem = new ItemStack(Material.OBSIDIAN, number);
				}
				ItemMeta meta = shopItem.getItemMeta();
				lore(ChatColor.BLUE + "AUTO CONFIRMING IN " + number, shopItem, meta);
				meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "AUTO CONFIRM");
				shopItem.setItemMeta(meta);
				createStack(inv, shopItem, 26);
				number--;

				if (number <= 0) {
					this.cancel();
					number = 5;
					plugin.manager.confirm = true;
					new BukkitRunnable() {
						@Override public void run() {
							player.closeInventory();
						}
					}.runTaskLater(plugin, 20);
				}

				player.playSound(player.getLocation(), Sound.CLICK, 1F, 0F);

			}
		}.runTaskTimerAsynchronously(plugin, 0, 20);

		/*shopItem = new ItemStack(Material.NETHER_STAR);
		createStack(inv, shopItem, "Back", 26);*/

		player.openInventory(inv);

	}

	@EventHandler public void checkConfirm(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();

		if (e.getClick().isLeftClick() && e.getInventory().getName()
				.equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "Confirm")) {
			e.setCancelled(true);

			switch (e.getCurrentItem().getType()) {

			case STAINED_GLASS:
				plugin.manager.confirm = true;
				plugin.manager.close = false;
				new BukkitRunnable() {
					@Override public void run() {
						player.closeInventory();
					}
				}.runTaskLater(plugin, 10);
				break;
			case STAINED_CLAY:
				plugin.manager.confirm = false;
				plugin.manager.close = true;
				new BukkitRunnable() {
					@Override public void run() {
						player.closeInventory();
					}
				}.runTaskLater(plugin, 10);

				break;
			default:
				plugin.manager.confirm = false;
				break;
			}
		}
	}

	public void oreShop(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.RED + "" + ChatColor.BOLD + "Ores Shop");

		shopItem = new ItemStack(Material.DIAMOND, 10);
		createStack(inv, shopItem, "Diamond", "ore", "diamond", 9);

		shopItem = new ItemStack(Material.IRON_INGOT, 64);
		createStack(inv, shopItem, "Iron Ingot", "ore", "iron", 10);

		shopItem = new ItemStack(Material.GOLD_INGOT, 64);
		createStack(inv, shopItem, "Gold Ingot", "ore", "gold", 11);

		shopItem = new ItemStack(Material.LAPIS_BLOCK, 7);
		createStack(inv, shopItem, "Lapis Lazuli Block", "ore", "lapis", 12);

		shopItem = new ItemStack(Material.REDSTONE, 64);
		createStack(inv, shopItem, "Redstone", "ore", "redstone", 13);

		shopItem = new ItemStack(Material.FLINT, 64);
		createStack(inv, shopItem, "Flint", "ore", "flint", 14);

		shopItem = new ItemStack(Material.COAL, 64);
		createStack(inv, shopItem, "Coal", "ore", "coal", 15);

		shopItem = new ItemStack(Material.EMERALD, 64);
		createStack(inv, shopItem, "Emerald", "ore", "emerald", 16);

		shopItem = new ItemStack(Material.QUARTZ, 64);
		createStack(inv, shopItem, "Nether Quartz", "ore", "nquartz", 17);

		shopItem = new ItemStack(Material.NETHER_STAR);
		createStack(inv, shopItem, "Back", 26);

		player.openInventory(inv);
	}

	public void spawnerShop(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.RED + "" + ChatColor.BOLD + "Spawner Shop");

		createStack(inv, new ItemStack(Material.MOB_SPAWNER, 1), "Cow Spawner", "spawner", "cow", 9);
		createStack(inv, new ItemStack(Material.MOB_SPAWNER, 1), "Pig Spawner", "spawner", "pig", 10);
		createStack(inv, new ItemStack(Material.MOB_SPAWNER, 1), "Zombie Spawner", "spawner", "zombie", 11);
		createStack(inv, new ItemStack(Material.MOB_SPAWNER, 1), "Skeleton Spawner", "spawner", "skeleton", 12);
		createStack(inv, new ItemStack(Material.MOB_SPAWNER, 1), "Enderman Spawner", "spawner", "enderman", 13);
		createStack(inv, new ItemStack(Material.MOB_SPAWNER, 1), "Zombie Pigman Spawner", "spawner", "pzombie", 14);
		createStack(inv, new ItemStack(Material.MOB_SPAWNER, 1), "Creeper Spawner", "spawner", "creeper", 15);
		createStack(inv, new ItemStack(Material.MOB_SPAWNER, 1), "Iron Golem Spawner", "spawner", "golem", 16);
		createStack(inv, new ItemStack(Material.MOB_SPAWNER, 1), "Blaze Spawner", "spawner", "blaze", 17);
		shopItem = new ItemStack(Material.NETHER_STAR);
		createStack(inv, shopItem, "Back", 26);

		player.openInventory(inv);
	}

	public void farmShop(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.RED + "" + ChatColor.BOLD + "Farm Shop");

		// apple, cane, pumpkin, carrot, potato, melon, wheat, seeds, wart, cactus, cocoabeans

		shopItem = new ItemStack(Material.APPLE, 64);
		createStack(inv, shopItem, "Apples", "farm", "apple", 0);

		shopItem = new ItemStack(Material.SUGAR_CANE, 64);
		createStack(inv, shopItem, "Sugar Cane", "farm", "cane", 1);

		shopItem = new ItemStack(Material.PUMPKIN, 64);
		createStack(inv, shopItem, "Pumpkin", "farm", "pumpkin", 2);

		shopItem = new ItemStack(Material.PUMPKIN_SEEDS, 64);
		createStack(inv, shopItem, "Pumpkin Seeds", "farm", "pumpkinseeds", 3);

		shopItem = new ItemStack(Material.CARROT_ITEM, 64);
		createStack(inv, shopItem, "Carrot", "farm", "carrot", 4);

		shopItem = new ItemStack(Material.POTATO_ITEM, 64);
		createStack(inv, shopItem, "Potato", "farm", "potato", 5);

		shopItem = new ItemStack(Material.MELON, 64);
		createStack(inv, shopItem, "Melon", "farm", "melon", 6);

		shopItem = new ItemStack(Material.MELON_SEEDS, 64);
		createStack(inv, shopItem, "Melon Seeds", "farm", "melonseeds", 7);

		shopItem = new ItemStack(Material.WHEAT, 64);
		createStack(inv, shopItem, "Wheat", "farm", "wheat", 8);

		shopItem = new ItemStack(Material.SEEDS, 64);
		createStack(inv, shopItem, "Seeds", "farm", "seeds", 9);

		shopItem = new ItemStack(Material.NETHER_STALK, 64);
		createStack(inv, shopItem, "Nether Wart", "farm", "nwart", 10);

		shopItem = new ItemStack(Material.CACTUS, 64);
		createStack(inv, shopItem, "Cactus", "farm", "cactus", 11);

		shopItem = new ItemStack(Material.INK_SACK, 64, (byte) 3);
		createStack(inv, shopItem, "Cocoa Beans", "farm", "cocoa", 12);

		shopItem = new ItemStack(Material.NETHER_STAR);
		createStack(inv, shopItem, "Back", 26);

		player.openInventory(inv);
	}

	public void mobShop(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.RED + "" + ChatColor.BOLD + "Mob Drops Shop");

		// bone slime epearl gtear string feather gunpowder inksak rblaze rpotato rflesh spidereye wskull

		shopItem = new ItemStack(Material.BONE, 64);
		createStack(inv, shopItem, "Bone", "mob", "bone", 0);

		shopItem = new ItemStack(Material.SLIME_BALL, 64);
		createStack(inv, shopItem, "Slime Ball", "mob", "slime", 1);

		shopItem = new ItemStack(Material.ENDER_PEARL, 16);
		createStack(inv, shopItem, "Ender Pearl", "mob", "epearl", 2);

		shopItem = new ItemStack(Material.GHAST_TEAR, 64);
		createStack(inv, shopItem, "Ghast Tear", "mob", "gtear", 3);

		shopItem = new ItemStack(Material.STRING, 64);
		createStack(inv, shopItem, "String", "mob", "string", 4);

		shopItem = new ItemStack(Material.FEATHER, 64);
		createStack(inv, shopItem, "Feather", "mob", "feather", 5);

		shopItem = new ItemStack(Material.SULPHUR, 64);
		createStack(inv, shopItem, "Gunpowder", "mob", "gunpowder", 6);

		shopItem = new ItemStack(Material.INK_SACK, 64);
		createStack(inv, shopItem, "Ink Sack", "mob", "inksack", 7);

		shopItem = new ItemStack(Material.BLAZE_ROD, 64);
		createStack(inv, shopItem, "Blaze Rod", "mob", "rblaze", 8);

		shopItem = new ItemStack(Material.POISONOUS_POTATO, 64);
		createStack(inv, shopItem, "Poisonous Potato", "mob", "rpotato", 9);

		shopItem = new ItemStack(Material.ROTTEN_FLESH, 64);
		createStack(inv, shopItem, "Rotten Flesh", "mob", "rflesh", 10);

		shopItem = new ItemStack(Material.SPIDER_EYE, 64);
		createStack(inv, shopItem, "Spider Eye", "mob", "spidereye", 11);

		shopItem = new ItemStack(Material.SKULL_ITEM, 1, (byte) 1);
		createStack(inv, shopItem, "Wither Skull", "mob", "wskull", 12);

		shopItem = new ItemStack(Material.NETHER_STAR);
		createStack(inv, shopItem, "Back", 26);

		player.openInventory(inv);
	}

	public void foodShop(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.RED + "" + ChatColor.BOLD + "Food Shop");
		// bread cpork gapple csalmon cfish cookie steak chicken bpotato pumpkinpie crabbit cmutton gcarrot

		shopItem = new ItemStack(Material.BREAD, 64);
		createStack(inv, shopItem, "Bread", "food", "bread", 0);

		shopItem = new ItemStack(Material.GRILLED_PORK, 64);
		createStack(inv, shopItem, "Cooked Porkchop", "food", "cpork", 1);

		shopItem = new ItemStack(Material.GOLDEN_APPLE, 16);
		createStack(inv, shopItem, "Golden Apple", "food", "gapple", 2);

		shopItem = new ItemStack(Material.COOKED_FISH, 64);
		createStack(inv, shopItem, "Cooked Fish", "food", "cfish", 3);

		shopItem = new ItemStack(Material.COOKIE, 64);
		createStack(inv, shopItem, "Cookie", "food", "cookie", 4);

		shopItem = new ItemStack(Material.COOKED_BEEF, 64);
		createStack(inv, shopItem, "Steak", "food", "steak", 5);

		shopItem = new ItemStack(Material.COOKED_CHICKEN, 64);
		createStack(inv, shopItem, "Cooked Chicken", "food", "chicken", 6);

		shopItem = new ItemStack(Material.BAKED_POTATO, 64);
		createStack(inv, shopItem, "Baked Potato", "food", "bpotato", 7);

		shopItem = new ItemStack(Material.PUMPKIN_PIE, 64);
		createStack(inv, shopItem, "Pumpkin Pie", "food", "pumpkinpie", 8);

		shopItem = new ItemStack(Material.COOKED_RABBIT, 64);
		createStack(inv, shopItem, "Cooked Rabbit", "food", "crabbit", 9);

		shopItem = new ItemStack(Material.COOKED_MUTTON, 64);
		createStack(inv, shopItem, "Cooked Mutton", "food", "cmutton", 10);

		shopItem = new ItemStack(Material.GOLDEN_CARROT, 64);
		createStack(inv, shopItem, "Golden Carrot", "food", "gcarrot", 11);

		shopItem = new ItemStack(Material.NETHER_STAR);
		createStack(inv, shopItem, "Back", 26);

		player.openInventory(inv);
	}
	// TODO block shop

	public void blockShop(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.RED + "" + ChatColor.BOLD + "Block Shop");

		shopItem = new ItemStack(Material.NETHER_STAR);
		createStack(inv, shopItem, "Back", 26);

		player.openInventory(inv);
	}

	public void createStack(Inventory inv, ItemStack item, String name, String ctype, String cname, int slot) {
		ItemMeta meta = item.getItemMeta();

		lore(ChatColor.GRAY + "Buy: " + ChatColor.RED + "$" + cfg.getInt("shop." + ctype + "." + cname + ".buy"),
				ChatColor.GRAY + "Sell: " + ChatColor.GREEN + "$" + cfg.getInt("shop." + ctype + "." + cname + ".sell"),
				item, meta);

		meta.setDisplayName(ChatColor.GOLD + name);
		item.setItemMeta(meta);

		inv.setItem(slot, item);

	}

	public void createStack(Inventory inv, ItemStack item, String name, String shopType, int slot) {
		ItemMeta meta = item.getItemMeta();

		lore(ChatColor.BLUE + "Click this to open the " + shopType + " shop.", item, meta);

		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + name);
		item.setItemMeta(meta);

		inv.setItem(slot, item);
	}

	public void createStack(Inventory inv, ItemStack item, String name, int slot) {
		ItemMeta meta = item.getItemMeta();

		lore(ChatColor.BLUE + "Click this to go back", item, meta);

		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + name);
		item.setItemMeta(meta);

		inv.setItem(slot, item);
	}

	public void createStack(Inventory inv, ItemStack item, int slot) {
		ItemMeta meta = item.getItemMeta();

		if (item.getType() == Material.STAINED_GLASS) {
			lore(ChatColor.BLUE + "Click this to confirm your purchase", item, meta);
		} else if (item.getType() == Material.STAINED_CLAY) {
			lore(ChatColor.BLUE + "Click this to deny your purchase", item, meta);
		}

		if (item.getType() == Material.STAINED_GLASS) {
			meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CONFIRM");
		} else if (item.getType() == Material.STAINED_CLAY) {
			meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "DENY");
		}

		if (item.getType() == Material.STAINED_GLASS) {
			item.setItemMeta(meta);
		} else if (item.getType() == Material.STAINED_CLAY) {
			item.setItemMeta(meta);
		}

		inv.setItem(slot, item);
	}

	public void lore(String loreString, String loreString1, ItemStack item, ItemMeta meta) {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(loreString);
		lore.add(loreString1);
		lore.add(ChatColor.BLUE + "Click with MMB to sell all");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	/*

	END SHOP

	 */

	/*

	SPAWNER MANAGER

	 */

	public void lore(String loreString, ItemStack item, ItemMeta meta) {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(loreString);
		lore.add("");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public void setSpawner(Block block, String name) {
		BlockState blockState = block.getState();
		CreatureSpawner spawner = ((CreatureSpawner) blockState);
		spawner.setSpawnedType(checkSpawner(name));
		blockState.update();
	}

	/*

	END SPAWNER MANAGER

	 */

	/*

	INVENTORY MANAGER

	 */

	public EntityType checkSpawner(String name) {
		//ig blaze enderman creeper cow pig skeleton zombie zombiepigman
		if (name.equalsIgnoreCase(ChatColor.GOLD + "Iron Golem Spawner")) {
			return EntityType.IRON_GOLEM;
		}

		if (name.equalsIgnoreCase(ChatColor.GOLD + "Blaze Spawner")) {
			return EntityType.BLAZE;
		}

		if (name.equalsIgnoreCase(ChatColor.GOLD + "Creeper Spawner")) {
			return EntityType.CREEPER;
		}

		if (name.equalsIgnoreCase(ChatColor.GOLD + "Cow Spawner")) {
			return EntityType.COW;
		}

		if (name.equalsIgnoreCase(ChatColor.GOLD + "Pig Spawner")) {
			return EntityType.PIG;
		}

		if (name.equalsIgnoreCase(ChatColor.GOLD + "Skeleton Spawner")) {
			return EntityType.SKELETON;
		}

		if (name.equalsIgnoreCase(ChatColor.GOLD + "Zombie Spawner")) {
			return EntityType.ZOMBIE;
		}

		if (name.equalsIgnoreCase(ChatColor.GOLD + "Zombie Pigman Spawner")) {
			return EntityType.PIG_ZOMBIE;
		}

		if (name.equalsIgnoreCase(ChatColor.GOLD + "Enderman Spawner")) {
			return EntityType.ENDERMAN;
		}

		return EntityType.PIG;
	}

	public void saveInventory(Player player) {

		if (player.getWorld().getName().contains("survival")) {

			cfg.set("inventory." + player.getDisplayName() + "." + "survival" + "",
					plugin.deserializer.InventoryToString(player.getInventory(), player));

		} else if (player.getWorld().getName().equalsIgnoreCase("pvp")) {
			cfg.set("inventory." + player.getDisplayName() + "." + "pvp" + "", "36;");
		} else {

			cfg.set("inventory." + player.getDisplayName() + "." + player.getWorld().getName() + "",
					plugin.deserializer.InventoryToString(player.getInventory(), player));

		}

		plugin.saveConfig();
	}

	/*

	END INVENTORY MANAGER

	 */

	/*

	BACK MANAGER

	 */

	public void setInventory(Player player) {

		if (player.getWorld().getName().contains("survival")) {

			if (cfg.contains("inventory." + player.getDisplayName() + "." + "survival")) {

				player.getInventory().clear();
				player.getInventory().setArmorContents(null);

				plugin.deserializer
						.StringToInventory(cfg.get("inventory." + player.getDisplayName() + "." + "survival").toString(),
								player);

			}

		} else if (player.getWorld().getName().equalsIgnoreCase("pvp")) {

			player.getInventory().clear();
			player.getInventory().setArmorContents(null);

			cfg.set("inventory." + player.getDisplayName() + "." + "pvp" + "", "36;");

		} else {

			if (cfg.contains("inventory." + player.getDisplayName() + "." + player.getWorld().getName())) {

				player.getInventory().clear();
				player.getInventory().setArmorContents(null);

				plugin.deserializer.StringToInventory(
						cfg.getString("inventory." + player.getDisplayName() + "." + player.getWorld().getName()), player);

			}
		}
	}

	/*

	END BACK MANAGER

	 */

	public void back(Player player) {

		plugin.getConfig().set("back." + player.getDisplayName() + ".world", player.getLocation().getWorld().getName());
		plugin.getConfig().set("back." + player.getDisplayName() + ".X", player.getLocation().getX());
		plugin.getConfig().set("back." + player.getDisplayName() + ".Y", player.getLocation().getY());
		plugin.getConfig().set("back." + player.getDisplayName() + ".Z", player.getLocation().getZ());
		plugin.getConfig().set("back." + player.getDisplayName() + ".yaw", player.getLocation().getYaw());
		plugin.getConfig().set("back." + player.getDisplayName() + ".pitch", player.getLocation().getPitch());

		plugin.saveConfig();

		plugin.manager.back = new Location(
				Bukkit.getServer().getWorld(plugin.getConfig().getString("back." + player.getDisplayName() + ".world")),
				plugin.getConfig().getDouble("back." + player.getDisplayName() + ".X"),
				plugin.getConfig().getDouble("back." + player.getDisplayName() + ".Y"),
				plugin.getConfig().getDouble("back." + player.getDisplayName() + ".Z"),
				(float) plugin.getConfig().getDouble("back." + player.getDisplayName() + ".yaw"),
				(float) plugin.getConfig().getDouble("back." + player.getDisplayName() + ".pitch"));

	}

	public void setWorldMode(Player player) {
		new BukkitRunnable() {
			@Override public void run() {
				if (player.getLocation().getWorld().getName().equalsIgnoreCase("creative")) {
					player.setGameMode(GameMode.CREATIVE);
				} else if (player.getLocation().getWorld().getName().equalsIgnoreCase("city")) {
					player.setGameMode(GameMode.CREATIVE);
				} else {
					player.setGameMode(GameMode.SURVIVAL);
				}
			}
		}.runTaskLaterAsynchronously(plugin, 10);
	}

	public void setUpVillagers() { //vplayer is command sender
		String path = "shop.villagers";

		if (cfg.contains(path + ".blocks") && cfg.contains(path + ".food") && cfg.contains(path + ".ores") && cfg
				.contains(path + ".mobdrops") && cfg.contains(path + ".spawners") && cfg.contains(path + ".farming")) {
			//spawn villagers in here
			vplayer.sendMessage(plugin.shopyellow + "Spawning in villagers...");

			new BukkitRunnable() {
				@Override public void run() {
					lBlock = loadVillagerLocations(lBlock, "blocks");
					lFood = loadVillagerLocations(lFood, "food");
					lOres = loadVillagerLocations(lOres, "ores");
					lMob = loadVillagerLocations(lMob, "mobdrops");
					lSpawner = loadVillagerLocations(lSpawner, "spawners");
					lFarming = loadVillagerLocations(lFarming, "farming");

					spawnVillager("Block Shop", lBlock);
					spawnVillager("Food Shop", lFood);
					spawnVillager("Ore Shop", lOres);
					spawnVillager("Drop Shop", lMob);
					spawnVillager("Spawner Shop", lSpawner);
					spawnVillager("Farm Shop", lFarming);

					vplayer.sendMessage(plugin.shopyellow + "Villagers have been spawned.");

					cfg.set("shop.villagers", "");
					plugin.saveConfig();

				}
			}.runTaskLaterAsynchronously(plugin, 10);

		} else {
			villagerLocationSetup();
		}

	}

	public void villagerLocationSetup() {
		vplayer.sendMessage(plugin.shopyellow + "Preparing setup...");
		IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a(" [{\"text\":\"§e§lSHOP > §fWould you like to \"},{\"text\":\"§a§lCONTINUE \",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"continue\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click to §a§lCONTINUE §fsetup\"}},{\"text\":\"§for \"},{\"text\":\"§c§lEXIT \",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"exit\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click to §c§lEXIT §fsetup\"}},{\"text\":\"§fsetup?\"}] ");
		PacketPlayOutChat chat = new PacketPlayOutChat(comp);
		((CraftPlayer)vplayer).getHandle().playerConnection.sendPacket(chat);
		villagerLocationMode.put(vplayer.getDisplayName(), true);
	}

	@SuppressWarnings("deprecation") public void savingVillagerLocation(Block block, String pathName) {
		Location clicked = block.getLocation();
		clicked.getBlock().setType(Material.IRON_BLOCK);

		Location slab1 = new Location(block.getWorld(), block.getX() - 2, block.getY(), block.getZ() + 1);
		slab1.getBlock().setType(Material.DOUBLE_STEP);

		Location slab2 = new Location(block.getWorld(), block.getX() - 2, block.getY(), block.getZ() - 1);
		slab2.getBlock().setType(Material.DOUBLE_STEP);

		Location slab3 = new Location(block.getWorld(), block.getX() + 2, block.getY(), block.getZ() - 1);
		slab3.getBlock().setType(Material.DOUBLE_STEP);

		Location slab4 = new Location(block.getWorld(), block.getX() + 2, block.getY(), block.getZ() + 1);
		slab4.getBlock().setType(Material.DOUBLE_STEP);

		Location slab5 = new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ() + 2);
		slab5.getBlock().setType(Material.DOUBLE_STEP);

		Location slab6 = new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ() + 2);
		slab6.getBlock().setType(Material.DOUBLE_STEP);

		Location slab7 = new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ() - 2);
		slab7.getBlock().setType(Material.DOUBLE_STEP);

		Location slab8 = new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ() - 2);
		slab8.getBlock().setType(Material.DOUBLE_STEP);

		Location log1 = new Location(block.getWorld(), block.getX() + 2, block.getY(), block.getZ() - 2);
		log1.getBlock().setType(Material.LOG);

		Location log2 = new Location(block.getWorld(), block.getX() + 2, block.getY(), block.getZ() + 2);
		log2.getBlock().setType(Material.LOG);

		Location log3 = new Location(block.getWorld(), block.getX() - 2, block.getY(), block.getZ() - 2);
		log3.getBlock().setType(Material.LOG);

		Location log4 = new Location(block.getWorld(), block.getX() - 2, block.getY(), block.getZ() + 2);
		log4.getBlock().setType(Material.LOG);

		Location fence1 = new Location(block.getWorld(), block.getX() - 2, block.getY() + 1, block.getZ() + 1);
		fence1.getBlock().setType(Material.FENCE);

		Location fence2 = new Location(block.getWorld(), block.getX() - 2, block.getY() + 1, block.getZ() - 1);
		fence2.getBlock().setType(Material.FENCE);

		Location fence3 = new Location(block.getWorld(), block.getX() + 2, block.getY() + 1, block.getZ() - 1);
		fence3.getBlock().setType(Material.FENCE);

		Location fence4 = new Location(block.getWorld(), block.getX() + 2, block.getY() + 1, block.getZ() + 1);
		fence4.getBlock().setType(Material.FENCE);

		Location fence5 = new Location(block.getWorld(), block.getX() - 1, block.getY() + 1, block.getZ() + 2);
		fence5.getBlock().setType(Material.FENCE);

		Location fence6 = new Location(block.getWorld(), block.getX() + 1, block.getY() + 1, block.getZ() + 2);
		fence6.getBlock().setType(Material.FENCE);

		Location fence7 = new Location(block.getWorld(), block.getX() - 1, block.getY() + 1, block.getZ() - 2);
		fence7.getBlock().setType(Material.FENCE);

		Location fence8 = new Location(block.getWorld(), block.getX() + 1, block.getY() + 1, block.getZ() - 2);
		fence8.getBlock().setType(Material.FENCE);

		Location fence9 = new Location(block.getWorld(), block.getX() + 2, block.getY() + 1, block.getZ() - 2);
		fence9.getBlock().setType(Material.FENCE);

		Location fence10 = new Location(block.getWorld(), block.getX() + 2, block.getY() + 1, block.getZ() + 2);
		fence10.getBlock().setType(Material.FENCE);

		Location fence11 = new Location(block.getWorld(), block.getX() - 2, block.getY() + 1, block.getZ() - 2);
		fence11.getBlock().setType(Material.FENCE);

		Location fence12 = new Location(block.getWorld(), block.getX() - 2, block.getY() + 1, block.getZ() + 2);
		fence12.getBlock().setType(Material.FENCE);

		Location fence13 = new Location(block.getWorld(), block.getX() - 2, block.getY() + 1, block.getZ());
		fence13.getBlock().setType(Material.FENCE);

		Location fence14 = new Location(block.getWorld(), block.getX() + 2, block.getY() + 1, block.getZ());
		fence14.getBlock().setType(Material.FENCE);

		Location fence15 = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ() + 2);
		fence15.getBlock().setType(Material.FENCE);

		Location fence16 = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ() - 2);
		fence16.getBlock().setType(Material.FENCE);

		Location cobble1 = new Location(block.getWorld(), block.getX() - 2, block.getY(), block.getZ());
		cobble1.getBlock().setType(Material.COBBLESTONE);

		Location cobble2 = new Location(block.getWorld(), block.getX() + 2, block.getY(), block.getZ());
		cobble2.getBlock().setType(Material.COBBLESTONE);

		Location cobble3 = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() + 2);
		cobble3.getBlock().setType(Material.COBBLESTONE);

		Location cobble4 = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() - 2);
		cobble4.getBlock().setType(Material.COBBLESTONE);

		Location stairNorth = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() + 1);
		stairNorth.getBlock().setType(Material.SMOOTH_STAIRS);
		stairNorth.getBlock().setData((byte) 2);

		Location stairSouth = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() - 1);
		stairSouth.getBlock().setType(Material.SMOOTH_STAIRS);
		stairSouth.getBlock().setData((byte) 3);

		Location stairsWest = new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ());
		stairsWest.getBlock().setType(Material.SMOOTH_STAIRS);
		stairsWest.getBlock().setData((byte) 0);

		Location stairsEast = new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ());
		stairsEast.getBlock().setType(Material.SMOOTH_STAIRS);
		stairsEast.getBlock().setData((byte) 1);

		Location stairsNorth1 = new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ() - 1);
		stairsNorth1.getBlock().setType(Material.QUARTZ_STAIRS);
		stairsNorth1.getBlock().setData((byte) 2);

		Location stairsNorth2 = new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ() + 1);
		stairsNorth2.getBlock().setType(Material.QUARTZ_STAIRS);
		stairsNorth2.getBlock().setData((byte) 3);

		Location stairsSouth1 = new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ() + 1);
		stairsSouth1.getBlock().setType(Material.QUARTZ_STAIRS);
		stairsSouth1.getBlock().setData((byte) 3);

		Location stairsSouth2 = new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ() - 1);
		stairsSouth2.getBlock().setType(Material.QUARTZ_STAIRS);
		stairsSouth2.getBlock().setData((byte) 2);

		cfg.set("shop.villagers." + pathName + ".world", block.getWorld().getName());
		cfg.set("shop.villagers." + pathName + ".X", block.getX());
		cfg.set("shop.villagers." + pathName + ".Y", block.getY() + 1);
		cfg.set("shop.villagers." + pathName + ".Z", block.getZ());

		plugin.saveConfig();
	}

	public Location loadVillagerLocations(Location location, String path) {
		float yaw = -90;

		if (setup.equalsIgnoreCase("east")) {
			yaw = -90;
		} else if (setup.equalsIgnoreCase("west")) {
			yaw = 90;
		} else if (setup.equalsIgnoreCase("north")) {
			yaw = -180;
		} else if (setup.equalsIgnoreCase("south")) {
			yaw = 0;
		}
		location = new Location(
				Bukkit.getServer().getWorld(plugin.getConfig().getString("shop.villagers." + path + ".world")),
				plugin.getConfig().getDouble("shop.villagers." + path + ".X") + .5,
				plugin.getConfig().getDouble("shop.villagers." + path + ".Y"),
				plugin.getConfig().getDouble("shop.villagers." + path + ".Z") + .5, yaw, (float) -5);
		return location;
	}

	public void spawnVillager(String name, Location loc) {

		Entity ent = Bukkit.getServer().getWorld(plugin.getConfig().getString("shop.villagers.blocks.world"))
				.spawnEntity(loc, EntityType.VILLAGER);
		((Villager) ent).setCustomName(ChatColor.YELLOW + "" + ChatColor.BOLD + name);
		((Villager) ent).setCustomNameVisible(true);
		((Villager) ent).setProfession(Villager.Profession.LIBRARIAN);
		freezeEntity(ent);

	}

	public void freezeEntity(Entity en) {
		net.minecraft.server.v1_8_R3.Entity nmsEn = ((CraftEntity) en).getHandle();
		NBTTagCompound compound = new NBTTagCompound();
		nmsEn.c(compound);
		compound.setByte("NoAI", (byte) 1);
		nmsEn.f(compound);

	}

	public void autoClearLag() {
		List<World> worlds = Bukkit.getServer().getWorlds();

		new BukkitRunnable() {
			@Override public void run() {
				lagcount--;

				for (World world : worlds) {
					List<Entity> entList = world.getEntities();

					for (Entity current : entList) {
						if (current instanceof Item) {
							try {
								Field itemField = current.getClass().getDeclaredField("item");
								Field ageField;
								Object entityItem;

								itemField.setAccessible(true);
								entityItem = itemField.get(current);

								ageField = entityItem.getClass().getDeclaredField("age");
								ageField.setAccessible(true);
								ageField.set(entityItem, 1);
							} catch (NoSuchFieldException | IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					}

				}

				//Bukkit.broadcastMessage(plugin.test + "Setting ticks lived to 1");

				if (lagcount == 60) {
					Bukkit.broadcastMessage(
							plugin.yellowpref + "All items will be cleared in " + ChatColor.YELLOW + lagcount
									+ ChatColor.WHITE + " seconds.");
				}

				if (lagcount == 30) {
					Bukkit.broadcastMessage(
							plugin.yellowpref + "All items will be cleared in " + ChatColor.YELLOW + lagcount
									+ ChatColor.WHITE + " seconds.");
				}

				if (lagcount == 10) {
					Bukkit.broadcastMessage(
							plugin.yellowpref + "All items will be cleared in " + ChatColor.YELLOW + lagcount
									+ ChatColor.WHITE + " seconds.");
				}

				if (lagcount <= 5 && lagcount > 0) {
					Bukkit.broadcastMessage(
							plugin.yellowpref + "All items will be cleared in " + ChatColor.YELLOW + lagcount
									+ ChatColor.WHITE + " seconds.");
				}
				if (lagcount <= 0) {

					lagcount = 300;

					for (World world : worlds) {
						List<Entity> entList = world.getEntities();

						for (Entity current : entList) {
							if (current instanceof Item) {
								current.remove();

							}
						}
					}

					Bukkit.broadcastMessage(plugin.yellowpref + "Cleared all entities.");
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0, 20);
	}

	public void save(Object o, File f) {
		try {
			if (!(f.exists()))
				f.createNewFile();
			;
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(o);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object load(File f) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			Object result = ois.readObject();
			ois.close();
			return result;
		} catch (Exception e) {
			return e.fillInStackTrace();
		}
	}

	public Map<String, Object> serialize(Location location) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("world", location.getWorld().getName());
		m.put("x", location.getX());
		m.put("y", location.getY());
		m.put("z", location.getZ());
		return m;
	}

	public void saveOnDisable() {
		HashMap<String, ArrayList<Map<String, Object>>> serializedMap = new HashMap<>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			ArrayList<Map<String, Object>> serializedLocations = new ArrayList<>();
			ArrayList<Location> listoflocs = plugin.gmanager.protectedBlocks.get(p.getName());

			for (Location location : listoflocs) {
				serializedLocations.add(serialize(location));
			}
			serializedMap.put(p.getName(), serializedLocations);
		}
		save(serializedMap, new File(plugin.getDataFolder(), "guard.dat"));
	}

	public void loadOnEnable() {
		HashMap<String, ArrayList<Map<String, Object>>> serializedMap = (HashMap<String, ArrayList<Map<String, Object>>>) load(
				new File(plugin.getDataFolder(), "guard.dat"));
		ArrayList<Map<String, Object>> serializedLocations = new ArrayList<>();

		for (Player p : Bukkit.getOnlinePlayers()) {
			ArrayList<Location> locations = new ArrayList<>();
			serializedLocations = serializedMap.get(p.getName());
			for (Map<String, Object> location : serializedLocations) {
				locations.add(deserialize(location));
			}
			plugin.gmanager.protectedBlocks.put(p.getName(), locations);
		}
	}
}
