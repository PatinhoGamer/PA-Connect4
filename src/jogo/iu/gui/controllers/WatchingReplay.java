package jogo.iu.gui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import jogo.iu.gui.GameBoardNode;
import jogo.iu.gui.Connect4UI;
import jogo.logica.Replayer;

import java.net.URL;
import java.util.ResourceBundle;

public class WatchingReplay implements Initializable {
	
	public BorderPane root;
	private Connect4UI app;
	private boolean finishedWatching = false;
	private Replayer replayer;
	private Runnable scheduler;
	private GameBoardNode board;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		app = Connect4UI.getInstance();
		replayer = new Replayer(app.getReplayToWatch());
		
		scheduler = () -> {
			try {
				Thread.sleep(750);
				Platform.runLater(this::moveForward);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		
		board = new GameBoardNode(null, replayer.getGameViewer());
		root.setCenter(board);
		
		moveForward();
	}
	
	private void moveForward() {
		if (!replayer.moveToNextStep() || finishedWatching) {
			finishedWatching = true;
			return;
		}
		if (replayer.getLastMessage() == null) {
			board.updateBoard(replayer.getGameArea());
		} else {
			app.openMessageDialog(Alert.AlertType.INFORMATION, "Replay", replayer.getLastMessage());
		}
		
		new Thread(scheduler).start();
	}
	
	public void backToMenu(ActionEvent actionEvent) {
		finishedWatching = true;
		app.goBackToMenu();
	}
}
