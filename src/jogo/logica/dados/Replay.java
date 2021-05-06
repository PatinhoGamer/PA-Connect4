package jogo.logica.dados;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Replay {
	private final List<String> gameActions;
	private final String winner;
	private final String loser;
	private final String date;
	
	public Replay(List<String> gameActions, String winner, String loser, Date moment) {
		this.gameActions = gameActions;
		this.winner = winner;
		this.loser = loser;
		date = new SimpleDateFormat("HH:mm:ss").format(moment);
	}
	
	public List<String> getGameActions() {
		return gameActions;
	}
	
	public String getWinner() {
		return winner;
	}
	
	public String getLoser() {
		return loser;
	}
	
	public String getDate() {
		return date;
	}
	
	@Override
	public String toString() {
		return date + " -> " + winner + " vs " + loser;
	}
}
