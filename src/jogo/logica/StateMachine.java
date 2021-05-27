package jogo.logica;

import jogo.logica.dados.GameDataViewer;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.dados.PlayerViewer;
import jogo.logica.estados.Connect4States;
import jogo.logica.estados.GameState;
import jogo.logica.estados.GameToStart;
import jogo.logica.minigames.TimedMiniGame;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serial;
import java.io.Serializable;

public class StateMachine implements Serializable {
	@Serial
	private static final long serialVersionUID = 0L;
	
	private GameState currentState;
	
	public StateMachine(GameState currentState) {
		this.currentState = currentState;
	}
	
	public StateMachine(){
		this.currentState = new GameToStart();
	}
	
	public void playAt(int column) {
		setState(currentState.playAt(column));
	}
	
	public void clearColumn(int column) {
		setState(currentState.clearColumn(column));
	}
	
	public void executePlay() {
		setState(currentState.executePlay());
	}
	
	public void rollback(int amount) {
		setState(currentState.rollback(amount));
	}
	
	public void startMiniGame() {
		setState(currentState.startMiniGame());
	}
	
	public void ignoreMiniGame() {
		setState(currentState.ignoreMiniGame());
	}
	
	public void endMiniGame() {
		setState(currentState.endMiniGame());
	}
	
	public void startGameWithPlayers(Player player1, Player player2) {
		currentState = currentState.startGameWithPlayers(player1, player2);
	}
	
	private void setState(GameState newState) {
		currentState = newState;
	}
	
	public TimedMiniGame getMiniGame() {
		return currentState.getMiniGame();
	}
	
	public Piece getCurrentPlayer() {
		return currentState.getCurrentPlayerPiece();
	}
	
	public PlayerViewer getCurrentPlayerObj() {
		return currentState.getPlayer(currentState.getCurrentPlayerPiece());
	}
	
	public PlayerViewer getPlayer(Piece playerPiece) {
		return currentState.getPlayer(playerPiece);
	}
	
	public Piece getWinner() {
		return currentState.getWinner();
	}
	
	public Piece[][] getGameArea() {
		return currentState.getGameArea();
	}
	
	public GameDataViewer getGame() {
		return currentState.getGameViewer();
	}
	
	public Connect4States getState() {
		return currentState.getState();
	}
	
	public GameState getUnderlyingGameState(){return currentState;}
}
