package jogo.iu.gui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import jogo.iu.gui.Connect4UI;
import jogo.logica.Connect4Logic;
import jogo.logica.Replayer;

import java.net.URL;
import java.util.ResourceBundle;

import static jogo.iu.gui.Connect4UI.*;

public class WatchingReplay implements Initializable {
	
	
	public BorderPane gameArea;
	private Connect4UI app;
	private Pane[][] paneArea;
	private boolean finishedWatching = false;
	private Replayer replayer;
	private Runnable scheduler;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		app = Connect4UI.getInstance();
		replayer = new Replayer(app.getReplayToWatch());
		
		paneArea = new Pane[Connect4Logic.HEIGHT][Connect4Logic.WIDTH];
		
		initializeGrid();
		
		scheduler = () -> {
			try {
				Thread.sleep(750);
				Platform.runLater(this::moveForward);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		
		moveForward();
	}
	
	private void initializeGrid() {
		VBox[] columns = new VBox[Connect4Logic.WIDTH];
		for (int column = 0; column < Connect4Logic.WIDTH; column++) {
			
			VBox curColumn = new VBox(GRID_PADDING);
			columns[column] = curColumn;
			
			Connect4UI.fillGridLine(column, curColumn,paneArea);
		}
		
		Pane area = new HBox(GRID_PADDING);
		gameArea.setCenter(area);
		area.getChildren().addAll(columns);
	}
	
	private void moveForward() {
		if (!replayer.moveToNextStep() || finishedWatching) {
			finishedWatching = true;
			return;
		}
		if (replayer.getLastMessage() == null)
			updateBoard();
		else {
			app.openMessageDialog(Alert.AlertType.INFORMATION, "Replay", replayer.getLastMessage());
		}
		
		new Thread(scheduler).start();
	}
	//TODO make the animation thing
	//TODO make watching replay prettier
	
	
	private void updateBoard() {
		Platform.runLater(() -> {
			Connect4UI.updateBoard(replayer.getGameArea(), paneArea);
		});
	}
	
	public void backToMenu(ActionEvent actionEvent) {
		finishedWatching = true;
		app.goBackToMenu();
	}
}
