package jogo.logica.dados;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Replay {
	private final List<String> gameActions;
	private final String player1Name;
	private final String player2Name;
	private final String date;
	
	public Replay(List<String> gameActions, String player1Name, String player2Name, Date moment) {
		this.gameActions = gameActions;
		this.player1Name = player1Name;
		this.player2Name = player2Name;
		date = new SimpleDateFormat("HH:mm:ss").format(moment);
	}
	
	public List<String> getGameActions() {
		return gameActions;
	}
	
	public String getPlayer1Name() {
		return player1Name;
	}
	
	public String getPlayer2Name() {
		return player2Name;
	}
	
	public String getDate() {
		return date;
	}
	
	@Override
	public String toString() {
		return date + " -> " + player1Name + " vs " + player2Name;
	}
}
