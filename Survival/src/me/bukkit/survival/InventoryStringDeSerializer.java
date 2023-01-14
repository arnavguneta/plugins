package me.bukkit.survival;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings({ "ALL", "WeakerAccess", "deprecation" }) public class InventoryStringDeSerializer {

	private Main plugin = Main.getPlugin(Main.class);

	public String InventoryToString(Inventory invInventory, Player player) {
		// beginning string (-5)
		String serialization = invInventory.getSize() + ";";
		for (int i = 0; i < 36; i++) {
			// scan for every item
			ItemStack is = invInventory.getItem(i);
			// if item isnt null
			if (is != null) {
				// new string
				String serializedItemStack = new String();

				serializedItemStack = serialize(serializedItemStack, is, player);
				// puts it together with the slot seperated by # and its values
				// each stack is split up by ;
				serialization += i + "#" + serializedItemStack + ";";
			}
		}
		// contents in left hand
		/*if (player.getInventory().getItemInOffHand().getType() != Material.AIR) {
			String serializedItemStack = new String();
			ItemStack offHand = new ItemStack(player.getInventory().getItemInOffHand());

			serializedItemStack = serialize(serializedItemStack, offHand, player);

			serialization += "o" + "#" + serializedItemStack + ";";
		}*/

			String serializedItemStack = new String();
			ItemStack[] armor = player.getInventory().getArmorContents();
			for (ItemStack stack : armor) {
				//if (stack != null) {

					if (stack.getType().toString().contains("BOOTS")) {

						serializedItemStack = serialize(serializedItemStack, stack, player);

						serialization += "b" + "#" + serializedItemStack + ";";


					}

					if (stack.getType().toString().contains("LEGGINGS")) {

						serializedItemStack = serialize(serializedItemStack, stack, player);

						serialization += "p" + "#" + serializedItemStack + ";";


					}

					if (stack.getType().toString().contains("CHESTPLATE")) {

						serializedItemStack = serialize(serializedItemStack, stack, player);

						serialization += "c" + "#" + serializedItemStack + ";";


					}

					if (stack.getType().toString().contains("HELMET")) {

						serializedItemStack = serialize(serializedItemStack, stack, player);

						serialization += "h" + "#" + serializedItemStack + ";";


					}

				//}
			}
			/*String serializedItemStack = new String();

			if (player.getInventory().getHelmet() != null) {
				ItemStack stack = player.getInventory().getHelmet();

				serializedItemStack = serialize(serializedItemStack, stack, player);

				serialization += "h" + "#" + serializedItemStack + ";";

			}

			if (player.getInventory().getChestplate() != null) {
				ItemStack stack = player.getInventory().getChestplate();

				serializedItemStack = serialize(serializedItemStack, stack, player);

				serialization += "c" + "#" + serializedItemStack + ";";

			}

			if (player.getInventory().getLeggings() != null) {
				ItemStack stack = player.getInventory().getLeggings();

				serializedItemStack = serialize(serializedItemStack, stack, player);

				serialization += "p" + "#" + serializedItemStack + ";";

			}

			if (player.getInventory().getBoots() != null) {
				ItemStack stack = player.getInventory().getBoots();

				serializedItemStack = serialize(serializedItemStack, stack, player);

				serialization += "b" + "#" + serializedItemStack + ";";

			}*/

		return serialization;
	}

	public void StringToInventory(String invString, Player player) {
		// first half is the size of the inventory
		String[] serializedBlocks = invString.split(";");
		//o#t@364:a@53
		// int for inv
		String invInfo = serializedBlocks[0];
		// creates an inv with the slot number from string to int
		Inventory deserializedInventory = player.getInventory();

		// starts on 1 bc 0 is for inv, 1 is for first item stack
		for (int i = 1; i < serializedBlocks.length; i++) {
			// split the item from rest
			// [0] is the slot number
			// [1] is the info for the stack
			String[] serializedBlock = serializedBlocks[i].split("#");

			Boolean offHand = false;
			Boolean helm = false;
			Boolean chest = false;
			Boolean pant = false;
			Boolean boot = false;
			int stackPosition = -1;

			if (serializedBlock[0].equalsIgnoreCase("h")) {
				helm = true;
			} else if (serializedBlock[0].equalsIgnoreCase("c")) {
				chest = true;
			} else if (serializedBlock[0].equalsIgnoreCase("p")) {
				pant = true;
			} else if (serializedBlock[0].equalsIgnoreCase("b")) {
				boot = true;
			} else {
				// offhand
				stackPosition = Integer.valueOf(serializedBlock[0]);
			}
			ItemStack is = null;
			Boolean createdItemStack = false;

			// split the item stacks by
			String[] serializedItemStack = serializedBlock[1].split(":");
			// for every string in the array of string (t@10,a@10,d@10) etc
			for (String itemInfo : serializedItemStack) {
				// split the string from @, [0] is type [1] quantity/ int
				String[] itemAttribute = itemInfo.split("@");
				// for type id
				if (itemAttribute[0].equals("t")) {
					// get material from id and make new stack
					is = new ItemStack(Material.getMaterial(Integer.valueOf(itemAttribute[1])));
					createdItemStack = true;
				} else if (itemAttribute[0].equals("d") && createdItemStack) {
					// sets durability
					is.setDurability(Short.valueOf(itemAttribute[1]));
				} else if (itemAttribute[0].equals("a") && createdItemStack) {
					is.setAmount(Integer.valueOf(itemAttribute[1]));
				} else if (itemAttribute[0].equals("e") && createdItemStack) {
					is.addEnchantment(Enchantment.getById(Integer.valueOf(itemAttribute[1])),
							Integer.valueOf(itemAttribute[2]));
				}
			}
			/*if (offHand == true) {
				player.getInventory().setItemInOffHand(is);
			}*/
			if (helm == true) {
				player.getInventory().setHelmet(is);
			}
			if (chest == true) {
				player.getInventory().setChestplate(is);
			}
			if (pant == true) {
				player.getInventory().setLeggings(is);
			}
			if (boot == true) {
				player.getInventory().setBoots(is);
			}
			if (stackPosition > -1) {
				deserializedInventory.setItem(stackPosition, is);
			}

		}

	}

	public String serialize(String serializedItemStack, ItemStack stack, Player player) {
		serializedItemStack = new String();
		// gets the numerial id of the block and makes it into a string
		String isType = String.valueOf(stack.getType().getId());
		// t@ is followed by the type of the material in the string
		serializedItemStack += "t@" + isType;

		// if it is damaged
		if (stack.getDurability() != 0) {
			// @d
			// a new string that converts durability of it to string and adds it to type
			String isDurability = String.valueOf(stack.getDurability());
			serializedItemStack += ":d@" + isDurability;
		}
		// if more than one item is present
		if (stack.getAmount() != 1) {
			// @a
			// new string for the amount added to everything else
			String isAmount = String.valueOf(stack.getAmount());
			serializedItemStack += ":a@" + isAmount;
		}

		// gets the enchantments of the stack
		Map<Enchantment, Integer> isEnch = stack.getEnchantments();
		if (isEnch.size() > 0) {
			// if it does contain enchants
			// for every enchantment in all of the enchantments
			for (Entry<Enchantment, Integer> ench : isEnch.entrySet()) {
				// @e type of enchantment followed by number
				serializedItemStack += ":e@" + ench.getKey().getId() + "@" + ench.getValue();
			}
		}

		return serializedItemStack;
	}
}