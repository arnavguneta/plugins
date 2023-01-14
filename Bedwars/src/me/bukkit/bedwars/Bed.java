package me.bukkit.bedwars;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

/**
 * Created by arnavguneta.
 */

@SuppressWarnings("deprecation")
public class Bed {
	// todo assign to a team instead of players
	private Player player;
	private Location footLocation;
	private Location headLoc;

	public Bed(Player player, Location footLocation) {
		this.player = player;
		this.footLocation = footLocation;

		Block block = getFootLocation().getBlock();

		BlockState bedFoot = block.getRelative(block.getFace(block)).getState();
		BlockState bedHead = bedFoot.getBlock().getRelative(BlockFace.SOUTH).getState();
		bedFoot.setType(Material.BED_BLOCK);
		bedHead.setType(Material.BED_BLOCK);
		bedFoot.setRawData((byte) 0x0);
		bedHead.setRawData((byte) 0x8);
		bedFoot.update(true, false);
		bedHead.update(true, true);

		setHeadLocation(bedHead.getLocation());

		player.sendMessage(getFootLocation().toString() + "\n\n\n\n" + getHeadLocation());

	}

	public Location getFootLocation() {
		return footLocation;
	}

	public void setFootLocation(Location location) {
		this.footLocation = location;
	}

	public Location getHeadLocation() {
		return headLoc;
	}

	public void setHeadLocation(Location headLoc) {
		this.headLoc = headLoc;
	}

	public Player getPlayer() {

		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
