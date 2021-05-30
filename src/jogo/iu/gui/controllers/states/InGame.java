package jogo.iu.gui.controllers.states;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import jogo.iu.gui.GameBoardNode;
import jogo.iu.gui.Connect4UI;
import jogo.iu.gui.GameWindowStateManager;
import jogo.iu.gui.ResourceLoader;
import jogo.logica.dados.observables.GameDataObservable;
import jogo.logica.dados.Piece;
import jogo.logica.estados.Connect4States;
import jogo.logica.dados.observables.StateMachineObservable;

import java.io.File;
import java.io.IOException;

public class InGame extends AbstractWindowState {
	@FXML
	public BorderPane root;
	public Label specialPiecesLabel;
	public Label playerNameLabel;
	public TextField rollbackAmountTextField;
	public ToggleButton clearColumnToggleButton;
	public ToggleButton normalPlayToggleButton;
	public Label playerRollbackAmount;
	
	private GameBoardNode gameBoardBoard;
	
	private boolean clearColumnPlayType = false;
	private RollbackTextPropertyListener rollbackTextFieldListener;
	
	private final Connect4UI app;
	
	public InGame(GameWindowStateManager windowStateManager) {
		super(windowStateManager, ResourceLoader.FXML_BOARD);
		app = Connect4UI.getInstance();
	}
	
	private StateMachineObservable getMachine() {
		return getWindowStateManager().getStateMachine();
	}
	
	@Override
	public void show() {
		System.out.println("InGame");
		super.show();
		updateFields();
		var stateMachine = getMachine();
		
		if (stateMachine.getState() == Connect4States.ComputerPlays) {
			Platform.runLater(() -> {
				try {
					Thread.sleep(250);
					stateMachine.executePlay();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		} else {
			gameBoardBoard.updateBoard(stateMachine.getGameArea());
		}
	}
	
	@Override
	public void firstSetupWindow() {
		playerNameLabel.setPadding(new Insets(10));
		
		rollbackTextFieldListener = new RollbackTextPropertyListener();
		rollbackAmountTextField.textProperty().addListener(rollbackTextFieldListener);
		
		gameBoardBoard = new GameBoardNode(this::gridPressedColumn, getMachine().getGame());
		root.setCenter(gameBoardBoard);
		
		registerListeners();
	}
	
	private void registerListeners() {
		var stateMachine = getMachine();
		var observableGame = stateMachine.getGame();
		observableGame.addChangeListener(GameDataObservable.Changes.BoardChanged, evt -> {
			if (evt.getNewValue() instanceof Piece[][])
				gameBoardBoard.updateBoard((Piece[][]) evt.getNewValue());
		});
		
		observableGame.addChangeListener(GameDataObservable.Changes.PlayerClearedColumn, evt -> {
			specialPiecesLabel.setText(Integer.toString(stateMachine.getCurrentPlayerObj().getSpecialPieces()));
		});
		
		observableGame.addChangeListener(GameDataObservable.Changes.PlayerRollback, evt -> {
			var player = getMachine().getCurrentPlayerObj();
			String playerRollbacks = Integer.toString(player.getRollbacks());
			playerRollbackAmount.setText(playerRollbacks);
			
			int maxRollbacks = Math.min(player.getRollbacks(), getMachine().getGame().getAvailableRollbacks());
			rollbackAmountTextField.setText(Integer.toString(maxRollbacks));
			rollbackTextFieldListener.setMaxValue(maxRollbacks);
		});
		
		observableGame.addChangeListener(GameDataObservable.Changes.PlayerClearedColumn, evt -> {
			specialPiecesLabel.setText(Integer.toString(stateMachine.getCurrentPlayerObj().getSpecialPieces()));
		});
	}
	
	private void updateFields() {
		var machine = getMachine();
		var player = machine.getCurrentPlayerObj();
		
		playerNameLabel.setText(player.getName());
		if (machine.getCurrentPlayer() == Piece.PLAYER1)
			Connect4UI.changeBackground(playerNameLabel, GameBoardNode.PLAYER1_COLOR, GameBoardNode.ROUND_CORNER);
		else Connect4UI.changeBackground(playerNameLabel, GameBoardNode.PLAYER2_COLOR, GameBoardNode.ROUND_CORNER);
		
		specialPiecesLabel.setText(Integer.toString(player.getSpecialPieces()));
	}
	
	void gridPressedColumn(int column) {
		var stateMachine = getMachine();
		
		if (stateMachine.getState() == Connect4States.WaitingPlayerMove) {
			
			if (clearColumnPlayType) {
				stateMachine.clearColumn(column);
				selectedNormalPlay(null);
			} else
				stateMachine.playAt(column);
			
		} else if (stateMachine.getState() == Connect4States.ComputerPlays) {
		} else {
			System.out.println("Error. Reached column pressed and is not either player move or computer move");
		}
	}
	
	@FXML
	public void goBack(ActionEvent actionEvent) {
		System.out.println("Back to main menu");
		app.goBackToMenu();
	}
	
	@FXML
	public void saveCurrentGame(ActionEvent actionEvent) {
		File file = app.saveFileChooser();
		if (file == null) return;
		try {
			app.saveCurrentStateMachine(file.getAbsolutePath(), getMachine().getUnderlyingGameState());
		} catch (IOException e) {
			e.printStackTrace();
			app.openMessageDialog(Alert.AlertType.ERROR, "Error Saving Game", "An unexpected error happened when saving the game");
		}
	}
	
	@FXML
	public void loadSavedGame(ActionEvent actionEvent) {
		File file = app.openFileChooser();
		if (file == null) return;
		try {
			var manager = getWindowStateManager();
			manager.getStateMachine().setGameState(app.loadGameFromFile(file.getAbsolutePath()));
			//gameBoardBoard.updateBoard(manager.getStateMachine().getGameArea()); // No need, the state changes and interface gets updated
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			app.openMessageDialog(Alert.AlertType.ERROR, "Error Loading Game", "An unexpected error happened when loading the saved game");
		}
	}
	
	public void rollback(ActionEvent actionEvent) {
		var machine = getMachine();
		String text = rollbackAmountTextField.getText();
		if (text.isBlank())
			return;
		int amount = Integer.parseInt(text);
		
		if (amount > machine.getGame().getAvailableRollbacks())
			app.openMessageDialog(Alert.AlertType.INFORMATION, "Too many rollbacks", "The game has not evolved enough to go back that much");
		
		machine.rollback(amount);
	}
	
	@FXML
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
	
	@FXML
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
