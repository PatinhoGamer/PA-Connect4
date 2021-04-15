package jogo.logica.estados;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Player;
import jogo.logica.dados.PlayerPiece;
import jogo.logica.estados.Connect4States;
import jogo.logica.estados.GameAbstractState;

public class StateMachine {
	
	private GameAbstractState currentState;
	
	public StateMachine(GameAbstractState currentState) {
		this.currentState = currentState;
	}
	
	public void playAt(int column) {
		currentState = currentState.playAt(column);
	}
	
	public void clearColumn(int column) {
		currentState = currentState.clearColumn(column);
	}
	
	public void setPlayers(Player player1, Player player2) {
		currentState = currentState.setPlayers(player1, player2);
	}
	
	public PlayerPiece getCurrentPlayer() {
		return currentState.getCurrentPlayer();
	}
	
	public Player getPlayer(PlayerPiece playerPiece) {
		return currentState.getPlayer(playerPiece);
	}
	
	public PlayerPiece getWinner() {
		return currentState.getWinner();
	}
	
	public PlayerPiece[][] getGameArea() {
		return currentState.getGame().getGameArea();
	}
	
	public Connect4Logic getGame() {
		return currentState.getGame();
	}
	
	public Connect4States getState() {
		return currentState.getState();
	}
}
