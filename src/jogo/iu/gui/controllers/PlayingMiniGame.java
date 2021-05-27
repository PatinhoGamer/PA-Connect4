package jogo.iu.gui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jogo.iu.gui.Connect4UI;
import jogo.logica.minigames.TimedMiniGame;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayingMiniGame implements Initializable {
	
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
	private Connect4UI app;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		app = Connect4UI.getInstance();
		miniGame = app.getStateMachine().getMiniGame();
		
		gameObjectiveLabel.setText(miniGame.getGameObjective());
		timerLabel.setText(Integer.toString(miniGame.availableTime()));
	}
	
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
		
		app.getStateMachine().endMiniGame();
		
		Stage stage = (Stage) questionLabel.getScene().getWindow();
		stage.close();
	}
	
	
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
