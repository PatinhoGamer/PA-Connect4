package jogo.logica.observables;

import javafx.stage.Stage;
import jogo.logica.GameDataObservable;
import jogo.logica.StateMachine;
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

public class StateMachineObservable {
	
	public static final String CHANGE_STATE = "CHANGE_STATE";
	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	
	private StateMachine stateMachine;
	
	private GameState lastState = null;
	
	public StateMachineObservable(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	
	public StateMachineObservable() {
		this.stateMachine = new StateMachine(new GameToStart(new GameDataObservable()));
	}
	
	public void addChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}
	
	public void checkChangedState() {
		if (lastState != stateMachine.getUnderlyingGameState()) {
			changeSupport.firePropertyChange(CHANGE_STATE, null, getState());
			lastState = stateMachine.getUnderlyingGameState();
		}
	}
	
	public void setStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	
	public StateMachine getObservedMachine() {
		return stateMachine;
	}
	
	public void playAt(int column) {
		stateMachine.playAt(column);
		checkChangedState();
	}
	
	public void clearColumn(int column) {
		stateMachine.clearColumn(column);
		checkChangedState();
	}
	
	public void executePlay() {
		stateMachine.executePlay();
		checkChangedState();
	}
	
	public void rollback(int amount) {
		stateMachine.rollback(amount);
		checkChangedState();
	}
	
	public void startMiniGame() {
		stateMachine.startMiniGame();
		checkChangedState();
	}
	
	public void ignoreMiniGame() {
		stateMachine.ignoreMiniGame();
		checkChangedState();
	}
	
	public void endMiniGame() {
		stateMachine.endMiniGame();
		checkChangedState();
	}
	
	public void startGameWithPlayers(Player player1, Player player2) {
		stateMachine.startGameWithPlayers(player1, player2);
		checkChangedState();
	}
	
	public TimedMiniGame getMiniGame() {
		return stateMachine.getMiniGame();
	}
	
	public Piece getCurrentPlayer() {
		return stateMachine.getCurrentPlayer();
	}
	
	public PlayerViewer getCurrentPlayerObj() {
		return stateMachine.getCurrentPlayerObj();
	}
	
	public PlayerViewer getPlayer(Piece playerPiece) {
		return stateMachine.getPlayer(playerPiece);
	}
	
	public Piece getWinner() {
		return stateMachine.getWinner();
	}
	
	public Piece[][] getGameArea() {
		return stateMachine.getGameArea();
	}
	
	public GameDataViewer getGame() {
		return stateMachine.getGame();
	}
	
	public Connect4States getState() {
		return stateMachine.getState();
	}
}
