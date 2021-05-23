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
	private Replayer replayer;
	private boolean finishedWatching = false;
	private Thread clock;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		app = Connect4UI.getInstance();
		replayer = new Replayer(app.getReplayToWatch());
		
		paneArea = new Pane[Connect4Logic.HEIGHT][Connect4Logic.WIDTH];
		
		initializeGrid();
		
		clock = new Thread(() -> {
			while (true) {
				if (finishedWatching)
					return;
				try {
					Thread.sleep(750);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
				Platform.runLater(this::moveForward);
			}
		});
		clock.setDaemon(true);
		clock.start();
	}
	
	private void initializeGrid() {
		VBox[] columns = new VBox[Connect4Logic.WIDTH];
		for (int column = 0; column < Connect4Logic.WIDTH; column++) {
			
			VBox curColumn = new VBox(GRID_PADDING);
			columns[column] = curColumn;
			
			for (int line = 0; line < Connect4Logic.HEIGHT; line++) {
				Pane spot = new Pane();
				spot.setPrefSize(SQUARE_SIZE, SQUARE_SIZE);
				Connect4UI.changeBackground(spot, NOPLAYER_COLOR, ROUND_CORNER);
				
				curColumn.getChildren().add(spot);
				paneArea[line][column] = spot;
			}
		}
		
		Pane area = new HBox(GRID_PADDING);
		gameArea.setCenter(area);
		area.getChildren().addAll(columns);
	}
	
	private void moveForward() {
		if (!replayer.moveToNextStep()) {
			finishedWatching = true;
			app.openMessageDialog(Alert.AlertType.INFORMATION, "Replay", "Finished watching replay");
			return;
		}
		if (replayer.getLastMessage() == null)
			updateBoard();
		else
			app.openMessageDialog(Alert.AlertType.INFORMATION, "Replay", replayer.getLastMessage());
	}// TODO Stop moving forward when messagedialog appears
	// or show message a differrent way
	
	
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
