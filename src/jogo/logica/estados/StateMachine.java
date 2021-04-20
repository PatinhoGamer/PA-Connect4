package jogo.logica.estados;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Player;
import jogo.logica.dados.Piece;
import jogo.logica.estados.connect4.Connect4States;
import jogo.logica.estados.connect4.GameAbstractState;
import jogo.logica.minigames.TimedGame;

import java.io.Serializable;

public class StateMachine implements Serializable {
	
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
	
	public void executePlay() {
		currentState = currentState.executePlay();
	}
	
	public void rollback() {
		currentState = currentState.rollback();
	}
	
	public void startMiniGame() {
		currentState = currentState.startMiniGame();
	}
	
	public void ignoreOrEndMiniGame() {
		currentState = currentState.ignoreOrEndMiniGame();
	}
	
	public void restartGame() {
		currentState = currentState.restartGame();
	}
	
	public void setPlayers(Player player1, Player player2) {
		currentState = currentState.setPlayers(player1, player2);
	}
	
	public TimedGame getMiniGame() {
		return currentState.getMiniGame();
	}
	
	public Piece getCurrentPlayer() {
		return currentState.getCurrentPlayer();
	}
	
	public Player getPlayerObj() {
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
