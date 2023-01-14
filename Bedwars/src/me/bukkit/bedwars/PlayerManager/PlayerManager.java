package me.bukkit.bedwars.PlayerManager;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by arnavguneta.
 */
public class PlayerManager {
	private String name;
	private UUID uuid;
	private Player player;
	private Boolean ingame;
	private Boolean setup;
	private Boolean canRespawn;

	public PlayerManager(Player player, Boolean ingame, Boolean setup, Boolean canRespawn) {
		this.player = player;
		this.setup = setup;
		this.name = player.getName();
		this.uuid = player.getUniqueId();
		this.canRespawn = canRespawn;
		this.ingame = ingame;
	}

	public Boolean getSetupMode() {
		return setup;
	}

	public void setSetupMode(Boolean setup) {
		this.setup = setup;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getCanRespawn() {
		return canRespawn;
	}

	public void setCanRespawn(Boolean canRespawn) {
		this.canRespawn = canRespawn;
	}

	public Boolean getSetup() {
		return setup;
	}

	public void setSetup(Boolean setup) {
		this.setup = setup;
	}

	public Boolean getIngame() {
		return ingame;
	}

	public void setIngame(Boolean ingame) {
		this.ingame = ingame;
	}
}
