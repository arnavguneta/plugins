package me.bukkit.survival.pvp;

import org.bukkit.entity.Player;

import java.util.UUID;


public class Battle {

	//name, inbattle, coinsearned, dead
	private String player1;
	private String player2;
	private String winner;
	private UUID uuid1;
	private UUID uuid2;


	public Battle(Player player1, Player player2) {
		this.player1 = player1.getName();
		this.player2 = player2.getName();
		this.uuid1 = player1.getUniqueId();
		this.uuid2 = player2.getUniqueId();
	}

	@SuppressWarnings("unused") public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public String getPlayer1() {
		return player1;
	}

	@SuppressWarnings("unused") public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	public String getPlayer2() {
		return player2;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getWinner() {
		return winner;
	}

	public UUID getUuid1() {
		return uuid1;
	}

	public UUID getUuid2() {
		return uuid2;
	}

}
