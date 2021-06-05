package jogo.iu.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import jogo.iu.gui.Connect4UI;
import jogo.logica.GameSaver;
import jogo.logica.dados.Replay;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChoosingReplay implements Initializable{
	
	public ListView<String> replaysList;
	private Connect4UI app;
	
	public void goBack(ActionEvent actionEvent) {
		app.goBackToMenu();
	}
	
	public void start(ActionEvent actionEvent) {
		int selectedIndex = replaysList.getSelectionModel().getSelectedIndex();
		Replay replay = GameSaver.getInstance().getReplays().get(selectedIndex);
		
		app.watchReplay(replay);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		app = Connect4UI.getInstance();
		List<Replay> replays = GameSaver.getInstance().getReplays();
		
		for (Replay replay : replays) {
			String element = replay.getDate() + " -> Winner: " + replay.getWinner() + " vs Loser: " + replay.getLoser();
			replaysList.getItems().add(element);
		}
	}
	
}
