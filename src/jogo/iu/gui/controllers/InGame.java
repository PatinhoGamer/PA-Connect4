package jogo.iu.gui.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import jogo.iu.gui.Board;
import jogo.iu.gui.Connect4UI;
import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.estados.Connect4States;
import jogo.logica.estados.StateMachine;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static jogo.iu.gui.Connect4UI.*;

public class InGame implements Initializable {
	
	
	@FXML
	public BorderPane root;
	public Label specialPiecesLabel;
	public Label playerNameLabel;
	public TextField rollbackAmountTextField;
	public ToggleButton clearColumnToggleButton;
	public ToggleButton normalPlayToggleButton;
	public Label playerRollbackAmount;
	
	private Board board;
	
	private Connect4UI app;
	
	private boolean clearColumnPlayType = false;
	private RollbackTextPropertyListener rollbackTextFieldListener;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		app = Connect4UI.getInstance();
		
		playerNameLabel.setPadding(new Insets(10));
		
		rollbackTextFieldListener = new RollbackTextPropertyListener();
		rollbackAmountTextField.textProperty().addListener(rollbackTextFieldListener);
		
		board = new Board(column -> gridPressedColumn(column));
		root.setCenter(board);
		
		updateAfterPlay();
	}
	
	private void updateFields() {
		StateMachine machine = getMachine();
		Player player = machine.getCurrentPlayerObj();
		
		playerNameLabel.setText(player.getName());
		if (machine.getCurrentPlayer() == Piece.PLAYER1)
			Connect4UI.changeBackground(playerNameLabel, Board.PLAYER1_COLOR, Board.ROUND_CORNER);
		else Connect4UI.changeBackground(playerNameLabel, Board.PLAYER2_COLOR, Board.ROUND_CORNER);
		
		String playerRollbacks = Integer.toString(player.getRollbacks());
		playerRollbackAmount.setText(playerRollbacks);
		
		int maxRollbacks = Math.min(player.getRollbacks(), getMachine().getGame().getAvailableRollbacks());
		rollbackAmountTextField.setText(Integer.toString(maxRollbacks));
		rollbackTextFieldListener.setMaxValue(maxRollbacks);
		
		specialPiecesLabel.setText(Integer.toString(player.getSpecialPieces()));
	}
	
	void gridPressedColumn(int column) {
		StateMachine stateMachine = getMachine();
		
		if (stateMachine.getState() == Connect4States.WaitingPlayerMove) {
			
			if (clearColumnPlayType) {
				stateMachine.clearColumn(column);
				selectedNormalPlay(null);
			} else
				stateMachine.playAt(column);
			
			updateAfterPlay();
		} else {
			System.out.println("something is wrong with the gridPressedColumn. this isnt the right type of window for this");
		}
	}
	
	void updateAfterPlay() {
		updateFields();
		board.updateBoard(getMachine().getGameArea());
		
		StateMachine machine = getMachine();
		switch (machine.getState()) {
			case WaitingPlayerMove -> {
			}
			case ComputerPlays -> {
				new Thread(() -> {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Platform.runLater(() -> {
						machine.executePlay();
						updateAfterPlay();
					});
				}).start();
			}
			case CheckPlayerWantsMiniGame -> {
				Player player = machine.getCurrentPlayerObj();
				boolean wantsMiniGame = app.openMessageWithCancel("Minigame Opportunity", "'" + player.getName() + "' do you want to play the minigame?");
				if (!wantsMiniGame) {
					machine.ignoreMiniGame();
					Platform.runLater(this::updateAfterPlay);
					return;
				}
				
				machine.startMiniGame();
				try {
					app.runMiniGame();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Platform.runLater(this::updateAfterPlay);
			}
			case GameFinished -> {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				var winner = machine.getWinner();
				if (winner == null) {
					app.openMessageDialog(Alert.AlertType.INFORMATION, "Game is Finished", "It appears that no one won this round");
				} else {
					Player player = machine.getPlayer(winner);
					app.openMessageDialog(Alert.AlertType.INFORMATION, "Game is Finished", "It appears that '" + player.getName() + "' has won this round");
				}
				app.goBackToMenu();
			}
		}
	}
	
	private StateMachine getMachine() {
		return app.getStateMachine();
	}
	
	public void goBack(ActionEvent actionEvent) {
		System.out.println("Back to main menu");
		app.goBackToMenu();
	}
	
	public void saveCurrentGame(ActionEvent actionEvent) {
		File file = app.saveFileChooser();
		if (file == null) return;
		try {
			app.saveCurrentStateMachine(file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			app.openMessageDialog(Alert.AlertType.ERROR, "Error Saving Game", "An unexpected error happened when saving the game");
		}
	}
	
	public void loadSavedGame(ActionEvent actionEvent) {
		File file = app.openFileChooser();
		if (file == null) return;
		try {
			app.loadGameFromFile(file.getAbsolutePath());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			app.openMessageDialog(Alert.AlertType.ERROR, "Error Loading Game", "An unexpected error happened when loading the saved game");
		}
	}
	
	public void rollback(ActionEvent actionEvent) {
		StateMachine machine = getMachine();
		String text = rollbackAmountTextField.getText();
		if (text.isBlank())
			return;
		int amount = Integer.parseInt(text);
		
		if (amount > machine.getGame().getAvailableRollbacks())
			app.openMessageDialog(Alert.AlertType.INFORMATION, "Too many rollbacks", "The game has not evolved enough to go back that much");
		
		machine.rollback(amount);
		updateAfterPlay();
	}
	
	public void selectedClearColumn(ActionEvent actionEvent) {
		if (getMachine().getCurrentPlayerObj().getSpecialPieces() <= 0) {
			clearColumnToggleButton.setSelected(false);
			normalPlayToggleButton.setSelected(true);
			return;
		}
		normalPlayToggleButton.setSelected(false);
		clearColumnToggleButton.setSelected(true);
		clearColumnPlayType = true;
	}
	
	public void selectedNormalPlay(ActionEvent actionEvent) {
		clearColumnToggleButton.setSelected(false);
		normalPlayToggleButton.setSelected(true);
		clearColumnPlayType = false;
	}
	
	class RollbackTextPropertyListener implements ChangeListener<String> {
		private int maxValue = 0;
		
		public void setMaxValue(int maxValue) {
			this.maxValue = maxValue;
		}
		
		public RollbackTextPropertyListener() {
		}
		
		@Override
		public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
			if (newValue.isBlank() || maxValue == 0) {
				rollbackAmountTextField.setText("");
				return;
			}
			if (!newValue.matches("\\d*")) {
				rollbackAmountTextField.setText(oldValue);
				return;
			}
			try {
				int value = Integer.parseInt(newValue);
				if (value <= 0 || value > maxValue) {
					int old = Integer.parseInt(oldValue);
					if (old <= 0 || old > maxValue)
						rollbackAmountTextField.setText("0");
					else
						rollbackAmountTextField.setText(oldValue);
				}
			} catch (Exception e) {
				rollbackAmountTextField.setText(oldValue);
			}
		}
	}
}
