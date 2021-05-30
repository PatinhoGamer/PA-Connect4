package jogo.iu.gui.controllers.states;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import jogo.iu.gui.GameWindowStateManager;
import jogo.iu.gui.ResourceLoader;

public class CheckPlayerWantsMiniGame extends AbstractWindowState {
	
	@FXML
	public Label playerNameLabel;
	public Button yesButton;
	public Button noButton;
	
	public CheckPlayerWantsMiniGame(GameWindowStateManager windowStateManager) {
		super(windowStateManager, ResourceLoader.FXML_CHECK_PLAYER_MINIGAME);
	}
	
	@Override
	protected void firstSetupWindow() {
		super.firstSetupWindow();
		
		yesButton.setOnAction(actionEvent -> getWindowStateManager().getStateMachine().acceptMiniGame());
		noButton.setOnAction(actionEvent -> getWindowStateManager().getStateMachine().ignoreMiniGame());
	}
	
	@Override
	public void show() {
		System.out.println("CheckPlayerWantsMiniGame");
		super.show();
		var playerName = getWindowStateManager().getStateMachine().getCurrentPlayerObj().getName();
		playerNameLabel.setText(playerName);
	}
	
	
}
