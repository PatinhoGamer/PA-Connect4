package jogo.iu.gui;

import javafx.stage.Stage;
import jogo.iu.gui.controllers.StateWindow;
import jogo.iu.gui.controllers.states.*;
import jogo.logica.StateMachine;
import jogo.logica.estados.Connect4States;
import jogo.logica.dados.observables.StateMachineObservable;
import jogo.logica.estados.GameState;

import java.util.HashMap;

public class GameWindowStateManager {
	
	private final Stage stage;
	private StateMachineObservable stateMachine;
	
	private HashMap<Connect4States, StateWindow> stateWindows = new HashMap<>();
	private StateWindow currentStateWindow;
	
	public GameWindowStateManager(Stage stage) throws Exception {
		this(stage, new jogo.logica.estados.GameToStart());
	}
	
	public GameWindowStateManager(Stage stage, GameState gameState) throws Exception {
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
		
		this.stateMachine.addChangeListener(StateMachineObservable.CHANGE_STATE, (propertyChangeEvent) -> {
			try {
				changeToRightState();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		changeToRightState();
	}
	
	private void changeToRightState() throws Exception {
		var stateWindow = stateWindows.get(stateMachine.getState());
		if (stateWindow == null) throw new Exception("No Window for current state");
		
		if (currentStateWindow != null)
			currentStateWindow.hide();
		stateWindow.show();
		
		currentStateWindow = stateWindow;
		stage.setScene(stateWindow.getScene());
	}
	
	public StateMachineObservable getStateMachine() {
		return stateMachine;
	}
}
