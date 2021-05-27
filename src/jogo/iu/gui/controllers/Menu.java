package jogo.iu.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import jogo.iu.gui.Connect4UI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Menu implements Initializable {
	public Label gameTitle;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		gameTitle.setAlignment(Pos.CENTER);
		gameTitle.setTextFill(Color.YELLOW);
		gameTitle.setFont(Font.font("Monospace", FontWeight.BOLD, 40));
		gameTitle.setMinHeight(80);
		gameTitle.setMinWidth(300);
		gameTitle.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(20d, 0d, 20d, 0d, false), new Insets(10))));
	}
	
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
