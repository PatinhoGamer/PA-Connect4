package jogo.iu.gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jogo.iu.gui.Connect4UI;
import jogo.logica.dados.Player;
import jogo.logica.dados.PlayerType;

import java.net.URL;
import java.util.ResourceBundle;

public class GameToStart implements Initializable {
	
	public TextField player1Field;
	public ChoiceBox<String> player1Choice;
	public TextField player2Field;
	public ChoiceBox<String> player2Choice;
	public Label feedbackLabel;
	
	public void setPlayers(ActionEvent actionEvent) {
		Connect4UI instance = Connect4UI.getInstance();
		String player1Name = player1Field.getText();
		String player2Name = player2Field.getText();
		
		if (player1Name.isBlank() || player2Name.isBlank()) {
			feedbackLabel.setText("Both players need to have a name");
			return;
		}
		if (player1Name.equals(player2Name)) {
			feedbackLabel.setText("Both players cant have the same name");
			return;
		}
		
		PlayerType player1Type = player1Choice.getSelectionModel().getSelectedIndex() == 0 ? PlayerType.HUMAN : PlayerType.COMPUTER;
		PlayerType player2Type = player2Choice.getSelectionModel().getSelectedIndex() == 0 ? PlayerType.HUMAN : PlayerType.COMPUTER;
		
		instance.startGameWithPlayers(new Player(player1Name, player1Type), new Player(player2Name, player2Type));
	}
	
	public void goBack(ActionEvent actionEvent) {
		Connect4UI.getInstance().goBackToMenu();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		ObservableList<String> choices = FXCollections.observableArrayList("Human", "Computer");
		player1Choice.setItems(choices);
		player2Choice.setItems(choices);
		player1Choice.getSelectionModel().selectFirst();
		player2Choice.getSelectionModel().selectFirst();
	}
}
