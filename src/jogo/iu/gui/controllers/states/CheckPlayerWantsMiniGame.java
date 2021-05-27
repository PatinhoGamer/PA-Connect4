package jogo.iu.gui.controllers.states;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import jogo.iu.gui.GameWindowStateManager;
import jogo.iu.gui.ResourceLoader;

public class CheckPlayerWantsMiniGame extends AbstractWindowState {
	
	@FXML
	public Label playerNameLabel;
	
	@FXML
	public void playMiniGame(ActionEvent actionEvent) {
		getWindowStateManager().getStateMachine().startMiniGame();
	}
	
	@FXML
	public void ignoreMiniGame(ActionEvent actionEvent) {
		getWindowStateManager().getStateMachine().ignoreMiniGame();
	}
	
	public CheckPlayerWantsMiniGame(GameWindowStateManager windowStateManager) {
		super(windowStateManager, ResourceLoader.FXML_CHECK_PLAYER_MINIGAME);
	}
	
	@Override
	public void show() {
		System.out.println("CheckPlayerWantsMiniGame");
		super.show();
		var playerName = getWindowStateManager().getStateMachine().getCurrentPlayerObj().getName();
		playerNameLabel.setText(playerName);
	}
	
	
}
