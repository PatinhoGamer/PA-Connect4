package jogo.iu.gui;

import javafx.application.Platform;
import javafx.stage.Stage;
import jogo.iu.gui.controllers.states.*;
import jogo.logica.dados.observables.StateMachineObservable;
import jogo.logica.estados.Connect4States;
import jogo.logica.estados.GameState;

import java.util.HashMap;

public class GameWindowStateManager {
	
	private final Stage stage;
	private final StateMachineObservable stateMachine;
	
	private final HashMap<Connect4States, StateWindow> stateWindows = new HashMap<>();
	private StateWindow currentStateWindow;
	
	public GameWindowStateManager(Stage stage) {
		this(stage, new jogo.logica.estados.GameToStart());
	}
	
	public GameWindowStateManager(Stage stage, GameState gameState) {
		this.stage = stage;
		this.stateMachine = new StateMachineObservable(gameState);
		
		StateWindow inGameState = new InGame(this);
		StateWindow playingMiniGame = new PlayingMiniGame(this);
		StateWindow gameToStart = new GameToStart(this);
		StateWindow checkPlayerWantsMiniGame = new CheckPlayerWantsMiniGame(this);
		StateWindow gameFinished = new GameFinished(this);
		
		stateWindows.put(Connect4States.ComputerPlays, inGameState);
		stateWindows.put(Connect4States.WaitingPlayerMove, inGameState);
		stateWindows.put(Connect4States.PlayingMiniGame, playingMiniGame);
		stateWindows.put(Connect4States.GameToStart, gameToStart);
		stateWindows.put(Connect4States.CheckPlayerWantsMiniGame, checkPlayerWantsMiniGame);
		stateWindows.put(Connect4States.GameFinished, gameFinished);
		
		stateMachine.addChangeListener(StateMachineObservable.CHANGE_STATE, (event) -> changeToRightState());
		
		changeToRightState();
	}
	
	private void changeToRightState() {
		var newStateWindow = stateWindows.get(stateMachine.getState());
		if (newStateWindow == null) throw new IllegalStateException("No Window for current state");

		if (currentStateWindow != null)
			currentStateWindow.hide();
		newStateWindow.show();
		
		currentStateWindow = newStateWindow;
		Platform.runLater(() -> stage.setScene(newStateWindow.getScene()));
	}
	
	public StateMachineObservable getStateMachine() {
		return stateMachine;
	}
}
