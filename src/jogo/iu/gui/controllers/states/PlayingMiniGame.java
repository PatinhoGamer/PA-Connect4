package jogo.iu.gui.controllers.states;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jogo.iu.gui.Connect4UI;
import jogo.iu.gui.GameWindowStateManager;
import jogo.iu.gui.ResourceLoader;
import jogo.logica.minigames.TimedMiniGame;

public class PlayingMiniGame extends AbstractWindowState implements Initializable {
	
	public Label feedBackLabel;
	public Label gameObjectiveLabel;
	public Label questionLabel;
	public TextField answerTextField;
	public Button startButton;
	public Label timerLabel;
	
	private TimedMiniGame miniGame;
	private boolean isFinished = false;
	private boolean hasStarted = false;
	private Thread timerThread;
	
	private final Connect4UI app;
	
	public PlayingMiniGame(GameWindowStateManager windowStateManager) {
		super(windowStateManager, ResourceLoader.FXML_PLAYING_MINIGAME);
		app = Connect4UI.getInstance();
	}
	
	@Override
	public void show() {
		System.out.println("PlayingMiniGame");
		super.show();
		
		miniGame = getWindowStateManager().getStateMachine().getMiniGame();
		gameObjectiveLabel.setText(miniGame.getGameObjective());
		timerLabel.setText(Integer.toString(miniGame.availableTime()));
	}
	
	@FXML
	public void triedToAnswer(ActionEvent actionEvent) {
		if (!hasStarted)
			return;
		String answer = answerTextField.getText();
		if (miniGame.checkAnswer(answer))
			feedBackLabel.setText("You got it");
		else
			feedBackLabel.setText("It was not that");
		
		if (miniGame.isFinished()) {
			finishedMiniGame();
		} else {
			questionLabel.setText(miniGame.getQuestion());
			answerTextField.clear();
		}
	}
	
	
	private void finishedMiniGame() {
		miniGame.stop();
		isFinished = true;
		timerThread.interrupt();
		
		if (miniGame.playerManagedToDoIt())
			app.openMessageDialog(Alert.AlertType.INFORMATION, "Minigame Result", "You won the minigame");
		else
			app.openMessageDialog(Alert.AlertType.INFORMATION, "Minigame Result", "You lost, the minigame and your turn");
		
		getWindowStateManager().getStateMachine().endMiniGame();
	}
	
	@FXML
	public void startMiniGame(ActionEvent actionEvent) {
		startButton.setDisable(true);
		miniGame.start();
		hasStarted = true;
		
		questionLabel.setText(miniGame.getQuestion());
		
		timerThread = new Thread(() -> {
			try {
				int remainingTime = miniGame.availableTime();
				while (remainingTime > 0) {
					Thread.sleep(1000);
					int finalRemainingTime = --remainingTime;
					Platform.runLater(() -> {
						timerLabel.setText(Integer.toString(finalRemainingTime));
					});
				}
				if (!isFinished)
					Platform.runLater(this::finishedMiniGame);
			} catch (InterruptedException ignored) {
			}
		});
		timerThread.setDaemon(true);
		timerThread.start();
	}
}
