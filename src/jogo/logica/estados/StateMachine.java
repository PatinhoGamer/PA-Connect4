package jogo.logica.estados;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.minigames.TimedMiniGame;

import java.io.Serial;
import java.io.Serializable;

public class StateMachine implements Serializable {
	@Serial
	private static final long serialVersionUID = 0L;
	
	private GameState currentState;
	
	public StateMachine(GameState currentState) {
		this.currentState = currentState;
	}
	
	public void playAt(int column) {
		currentState = currentState.playAt(column);
	}
	
	public void clearColumn(int column) {
		currentState = currentState.clearColumn(column);
	}
	
	public void executePlay() {
		currentState = currentState.executePlay();
	}
	
	public void rollback(int amount) {
		currentState = currentState.rollback(amount);
	}
	
	public void startMiniGame() {
		currentState = currentState.startMiniGame();
	}
	
	public void ignoreMiniGame() {
		currentState = currentState.ignoreMiniGame();
	}
	
	public void endMiniGame() {
		currentState = currentState.endMiniGame();
	}
	
	public void restartGame() {
		currentState = currentState.restartGame();
	}
	
	public void startGameWithPlayers(Player player1, Player player2) {
		currentState = currentState.startGameWithPlayers(player1, player2);
	}
	
	public TimedMiniGame getMiniGame() {
		return currentState.getMiniGame();
	}
	
	public Piece getCurrentPlayer() {
		return currentState.getCurrentPlayer();
	}
	
	public Player getCurrentPlayerObj() {
		return currentState.getPlayer(currentState.getCurrentPlayer());
	}
	
	public Player getPlayer(Piece playerPiece) {
		return currentState.getPlayer(playerPiece);
	}
	
	public Piece getWinner() {
		return currentState.getWinner();
	}
	
	public Piece[][] getGameArea() {
		return currentState.getGame().getGameArea();
	}
	
	public Connect4Logic getGame() {
		return currentState.getGame();
	}
	
	public Connect4States getState() {
		return currentState.getState();
	}
}
