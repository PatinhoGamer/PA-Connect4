package jogo.iu.gui.controllers;

import javafx.event.ActionEvent;
import jogo.iu.gui.Connect4UI;

import java.io.File;
import java.io.IOException;

public class Menu {
	public void startGame(ActionEvent actionEvent) {
		Connect4UI.getInstance().startGame();
	}
	
	public void watchReplay(ActionEvent actionEvent) {
		Connect4UI.getInstance().changeScene(Connect4UI.FXML_CHOOSING_REPLAY);
	}
	
	public void loadGameFromFile(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
		Connect4UI instance = Connect4UI.getInstance();
		File file = instance.openFileChooser();
		if (file != null)
			instance.loadGameFromFile(file.getPath());
	}
	
	public void exit(ActionEvent actionEvent) throws Exception {
		Connect4UI.getInstance().exit();
	}
}
