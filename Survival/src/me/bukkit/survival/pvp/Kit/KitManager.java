package me.bukkit.survival.pvp.Kit;

import me.bukkit.survival.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;

@SuppressWarnings({ "ALL", "WeakerAccess" }) public class KitManager {

	private Main plugin = Main.getPlugin(Main.class);

	void pot(Player p, PlayerInventory i) {

		p.setGameMode(GameMode.SURVIVAL);
		i.clear();
		p.setHealth(20.0);

		i.setHelmet(null);
		i.setChestplate(null);
		i.setLeggings(null);
		i.setBoots(null);

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET);
		helm.addEnchantment(Enchantment.DURABILITY, 3);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		i.setHelmet(helm);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
		chest.addEnchantment(Enchantment.DURABILITY, 3);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		i.setChestplate(chest);

		ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS);
		pants.addEnchantment(Enchantment.DURABILITY, 3);
		pants.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		i.setLeggings(pants);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
		boots.addEnchantment(Enchantment.DURABILITY, 3);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		i.setBoots(boots);

		ItemStack swordd1 = new ItemStack(Material.DIAMOND_SWORD);
		swordd1.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		swordd1.addEnchantment(Enchantment.DURABILITY, 3);
		ItemMeta swordmeta111 = swordd1.getItemMeta();
		swordmeta111.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Sword");
		swordd1.setItemMeta(swordmeta111);
		i.addItem(swordd1);

		i.addItem(new ItemStack(Material.ENDER_PEARL, 16));
		i.addItem(new ItemStack(Material.GOLDEN_CARROT, 64));

		ItemStack item = new ItemStack(Material.POTION);
		Potion pot = new Potion(1);
		pot.setType(PotionType.INSTANT_HEAL);
		pot.setSplash(true);
		pot.setLevel(2);
		pot.apply(item);
		p.getInventory().addItem(item);

		for (int i1 = 0; i1 < 32; i1++) {
			i.addItem(item);
		}
		// Etc

		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));
		p.closeInventory();
		p.sendMessage(plugin.pvpgreen + "Potion kit has been loaded succesfully!");

	}

	void melee(Player p, PlayerInventory i) {

		p.setGameMode(GameMode.SURVIVAL);
		i.clear();
		p.setHealth(20.0);

		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));

		ItemStack sword11 = new ItemStack(Material.DIAMOND_SWORD);
		sword11.addEnchantment(Enchantment.DAMAGE_ALL, 2);
		sword11.addEnchantment(Enchantment.DURABILITY, 3);
		ItemMeta swordmeta1111 = sword11.getItemMeta();
		swordmeta1111.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Sword");
		sword11.setItemMeta(swordmeta1111);
		i.addItem(sword11);

		ItemStack rod1 = new ItemStack(Material.FISHING_ROD);
		rod1.addEnchantment(Enchantment.DURABILITY, 3);
		ItemMeta rodMeta1 = rod1.getItemMeta();
		rodMeta1.setDisplayName(ChatColor.RED + "Rod");
		rod1.setItemMeta(rodMeta1);
		i.addItem(rod1);

		PlayerInventory i11 = p.getInventory();
		i11.setHelmet(null);
		i11.setChestplate(null);
		i11.setLeggings(null);
		i11.setBoots(null);

		ItemStack helm1111 = new ItemStack(Material.LEATHER_HELMET);
		helm1111.addEnchantment(Enchantment.DURABILITY, 3);
		helm1111.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		i.setHelmet(helm1111);

		ItemStack chest1111 = new ItemStack(Material.LEATHER_CHESTPLATE);
		chest1111.addEnchantment(Enchantment.DURABILITY, 3);
		chest1111.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		i.setChestplate(chest1111);

		ItemStack pants1111 = new ItemStack(Material.LEATHER_LEGGINGS);
		pants1111.addEnchantment(Enchantment.DURABILITY, 3);
		pants1111.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		i.setLeggings(pants1111);

		ItemStack boots11111 = new ItemStack(Material.LEATHER_BOOTS);
		boots11111.addEnchantment(Enchantment.DURABILITY, 3);
		boots11111.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		i.setBoots(boots11111);

		p.closeInventory();
		p.sendMessage(plugin.pvpgreen + "Melee kit has been loaded succesfully!");

	}

	void gapple(Player p, PlayerInventory i) {

		p.setGameMode(GameMode.SURVIVAL);
		i.clear();
		p.setHealth(20.0);

		i.setHelmet(null);
		i.setChestplate(null);
		i.setLeggings(null);
		i.setBoots(null);

		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET);
		helm.addEnchantment(Enchantment.DURABILITY, 3);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		i.setHelmet(helm);

		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
		chest.addEnchantment(Enchantment.DURABILITY, 3);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		i.setChestplate(chest);

		ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS);
		pants.addEnchantment(Enchantment.DURABILITY, 3);
		pants.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		i.setLeggings(pants);

		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
		boots.addEnchantment(Enchantment.DURABILITY, 3);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		i.setBoots(boots);

		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));
		p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100000, 1));

		ItemStack swordd1 = new ItemStack(Material.DIAMOND_SWORD);
		swordd1.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		swordd1.addEnchantment(Enchantment.DURABILITY, 3);
		ItemMeta swordmeta111 = swordd1.getItemMeta();
		swordmeta111.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Sword");
		swordd1.setItemMeta(swordmeta111);
		i.addItem(swordd1);

		i.addItem(new ItemStack(Material.GOLDEN_APPLE, 64, (short) 1));
		i.addItem(new ItemStack(Material.ENDER_PEARL, 64, (short) 1));

		i.addItem(boots);
		i.addItem(pants);
		i.addItem(chest);
		i.addItem(helm);

		p.sendMessage(plugin.pvpgreen + "Gapple kit has been loaded succesfully!");
		p.closeInventory();

	}

	/*void soup(Player p, PlayerInventory i) {

		p.setGameMode(GameMode.SURVIVAL);
		i.clear();
		p.setHealth(20.0);
		i.addItem(new ItemStack(Material.DIAMOND_SWORD));
		ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP);
		for (int i1 = 0; i1 < 35; i1++) {
			i.addItem(soup);
		}

		PlayerInventory i1 = p.getInventory();
		i1.setHelmet(null);
		i1.setChestplate(null);
		i1.setLeggings(null);
		i1.setBoots(null);

		ItemStack helm11 = new ItemStack(Material.IRON_HELMET);
		helm11.addEnchantment(Enchantment.DURABILITY, 3);
		i.setHelmet(helm11);

		ItemStack chest11 = new ItemStack(Material.IRON_CHESTPLATE);
		chest11.addEnchantment(Enchantment.DURABILITY, 3);
		chest11.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		i.setChestplate(chest11);

		ItemStack pants11 = new ItemStack(Material.IRON_LEGGINGS);
		pants11.addEnchantment(Enchantment.DURABILITY, 3);
		i.setLeggings(pants11);

		ItemStack boots111 = new ItemStack(Material.IRON_BOOTS);
		boots111.addEnchantment(Enchantment.DURABILITY, 3);
		i.setBoots(boots111);

		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));
		p.closeInventory();
		p.sendMessage(ChatColor.GOLD + "SoupPvP kit has been loaded succesfully!");

	}*/

	void horse(Player p, PlayerInventory i) {

		i.clear();
		p.setGameMode(GameMode.SURVIVAL);
		p.setHealth(20.0);

		Horse h = (Horse) p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);

		h.setAdult();
		h.setCustomName(ChatColor.GOLD + p.getName() + "'s horse");
		h.setCustomNameVisible(true);
		h.setMaxHealth(40.0);
		h.setHealth(40.0);
		h.setJumpStrength(1.0);
		h.setTamed(true);
		h.setVariant(Horse.Variant.HORSE);
		h.setPassenger(p.getPlayer());
		h.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
		h.getInventory().setSaddle(new ItemStack(Material.SADDLE));

		i.setHelmet(null);
		i.setChestplate(null);
		i.setLeggings(null);
		i.setBoots(null);

		ItemStack helm1 = new ItemStack(Material.IRON_HELMET);
		helm1.addEnchantment(Enchantment.DURABILITY, 3);
		i.setHelmet(helm1);

		ItemStack chest1 = new ItemStack(Material.IRON_CHESTPLATE);
		chest1.addEnchantment(Enchantment.DURABILITY, 3);
		i.setChestplate(chest1);

		ItemStack pants1 = new ItemStack(Material.IRON_LEGGINGS);
		pants1.addEnchantment(Enchantment.DURABILITY, 3);
		i.setLeggings(pants1);

		ItemStack boots1 = new ItemStack(Material.IRON_BOOTS);
		boots1.addEnchantment(Enchantment.DURABILITY, 3);
		i.setBoots(boots1);

		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		sword.addEnchantment(Enchantment.DURABILITY, 3);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
		ItemMeta swordmeta = sword.getItemMeta();
		swordmeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Sword");
		sword.setItemMeta(swordmeta);
		i.addItem(sword);

		ItemStack bow11 = new ItemStack(Material.BOW);
		bow11.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		bow11.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
		ItemMeta bowmeta11 = bow11.getItemMeta();
		bowmeta11.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Bow");
		bow11.setItemMeta(bowmeta11);
		i.addItem(bow11);

		i.addItem(new ItemStack(Material.GOLDEN_APPLE, 2));
		i.addItem(new ItemStack(Material.ARROW, 1));
		i.addItem(new ItemStack(Material.WHEAT, 32));

		p.closeInventory();
		p.sendMessage(plugin.pvpgreen + "Horse kit has been loaded succesfully!");

	}

	void archer(Player p, PlayerInventory i) {

		p.setGameMode(GameMode.SURVIVAL);
		i.clear();
		p.setHealth(20.0);

		i.setHelmet(null);
		i.setChestplate(null);
		i.setLeggings(null);
		i.setBoots(null);

		ItemStack helm2 = new ItemStack(Material.IRON_HELMET);
		helm2.addEnchantment(Enchantment.DURABILITY, 3);
		i.setHelmet(helm2);

		ItemStack chest2 = new ItemStack(Material.IRON_CHESTPLATE);
		chest2.addEnchantment(Enchantment.DURABILITY, 3);
		i.setChestplate(chest2);

		ItemStack pants2 = new ItemStack(Material.IRON_LEGGINGS);
		pants2.addEnchantment(Enchantment.DURABILITY, 3);
		i.setLeggings(pants2);

		ItemStack boots2 = new ItemStack(Material.IRON_BOOTS);
		boots2.addEnchantment(Enchantment.DURABILITY, 3);
		i.setBoots(boots2);

		ItemStack bow1 = new ItemStack(Material.BOW);
		bow1.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		bow1.addEnchantment(Enchantment.ARROW_DAMAGE, 4);
		bow1.addEnchantment(Enchantment.DURABILITY, 3);
		ItemMeta bowmeta1 = bow1.getItemMeta();
		bowmeta1.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Bow");
		bow1.setItemMeta(bowmeta1);
		i.addItem(bow1);

		i.addItem(new ItemStack(Material.ENDER_PEARL, 16));
		i.addItem(new ItemStack(Material.ARROW, 1));

		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));

		p.closeInventory();
		p.sendMessage(plugin.pvpgreen + "Archer kit has been loaded succesfully!");

	}

	void mcsg(Player p, PlayerInventory i) {

		p.setGameMode(GameMode.SURVIVAL);
		i.clear();
		p.setHealth(20.0);

		i.setHelmet(null);
		i.setChestplate(null);
		i.setLeggings(null);
		i.setBoots(null);

		ItemStack helm111 = new ItemStack(Material.LEATHER_HELMET);
		helm111.addEnchantment(Enchantment.DURABILITY, 3);
		i.setHelmet(helm111);

		ItemStack chest111 = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		chest111.addEnchantment(Enchantment.DURABILITY, 3);
		i.setChestplate(chest111);

		ItemStack pants111 = new ItemStack(Material.IRON_LEGGINGS);
		pants111.addEnchantment(Enchantment.DURABILITY, 3);
		i.setLeggings(pants111);

		ItemStack boots1111 = new ItemStack(Material.GOLD_BOOTS);
		boots1111.addEnchantment(Enchantment.DURABILITY, 3);
		i.setBoots(boots1111);

		ItemStack swordd = new ItemStack(Material.STONE_SWORD);
		swordd.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		swordd.addEnchantment(Enchantment.DURABILITY, 3);
		ItemMeta swordmeta11 = swordd.getItemMeta();
		swordmeta11.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Sword");
		swordd.setItemMeta(swordmeta11);
		i.addItem(swordd);

		ItemStack rod = new ItemStack(Material.FISHING_ROD);
		rod.addEnchantment(Enchantment.DURABILITY, 3);
		ItemMeta rodMeta = rod.getItemMeta();
		rodMeta.setDisplayName(ChatColor.RED + "Rod");
		rod.setItemMeta(rodMeta);
		i.addItem(rod);

		ItemStack bow = new ItemStack(Material.BOW);
		bow.addEnchantment(Enchantment.DURABILITY, 3);
		ItemMeta bowmeta = bow.getItemMeta();
		bowmeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Bow");
		bow.setItemMeta(bowmeta);
		i.addItem(bow);

		i.addItem(new ItemStack(Material.GOLDEN_APPLE, 2));
		i.addItem(new ItemStack(Material.ARROW, 12));

		p.sendMessage(plugin.pvpgreen + "MCSG kit has been loaded succesfully!");
		p.closeInventory();

	}

	public void openGUI(Player player) {
		player.getInventory().clear();
		ItemStack[] stacks = {new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)};

		player.getInventory().setArmorContents(stacks);
		Inventory inv = Bukkit.createInventory(null, 18, ChatColor.RED + "" + ChatColor.BOLD + "Kits");

		ItemStack pot = new ItemStack(Material.POTION);
		createStack(inv, pot, "Potions Kit", "potions", 0);

		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
		createStack(inv, chest, "Melee Kit", "melee", 8);

		ItemStack mcsg = new ItemStack(Material.FISHING_ROD);
		createStack(inv, mcsg, "MCSG Kit", "mcsg", 4);

		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE);
		createStack(inv, gapple, "Gapple Kit", "gapple", 10);

		ItemStack bow = new ItemStack(Material.BOW);
		createStack(inv, bow, "Archer Kit", "archer", 2);

		ItemStack horse = new ItemStack(Material.DIAMOND_BARDING);
		createStack(inv, horse, "Horse Kit", "horse", 6);

		ItemStack glass = new ItemStack(Material.THIN_GLASS);

		inv.setItem(1, glass);
		inv.setItem(3, glass);
		inv.setItem(5, glass);
		inv.setItem(7, glass);
		inv.setItem(9, glass);
		inv.setItem(11, glass);
		inv.setItem(13, glass);
		inv.setItem(15, glass);
		inv.setItem(17, glass);

		player.openInventory(inv);

	}

	private void createStack(Inventory inv, ItemStack item, String name, String kitType, int slot) {
		ItemMeta meta = item.getItemMeta();

		lore(ChatColor.BLUE + "Click to equip the " + kitType + " kit.", item, meta);

		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + name);
		item.setItemMeta(meta);

		inv.setItem(slot, item);
	}

	private void lore(String loreString, ItemStack item, ItemMeta meta) {
		ArrayList<String> lore = new ArrayList<>();
		lore.add(loreString);
		lore.add("");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

}
