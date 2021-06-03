package jogo.iu.gui.controllers.states;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import jogo.iu.gui.Connect4UI;
import jogo.iu.gui.GameBoardNode;
import jogo.iu.gui.GameWindowStateManager;
import jogo.iu.gui.ResourceLoader;
import jogo.logica.dados.Piece;


public class GameFinished extends AbstractWindowState {
	
	@FXML
	public BorderPane root;
	public Label playerNameLabel;
	
	private GameBoardNode board;
	
	public GameFinished(GameWindowStateManager windowStateManager) {
		super(windowStateManager, ResourceLoader.FXML_GAME_FINISHED);
	}
	
	@Override
	protected void firstSetupWindow() {
		super.firstSetupWindow();
		
		board = new GameBoardNode(getWindowStateManager().getStateMachine().getGame());
		root.setCenter(board);
	}
	
	@Override
	public void show() {
		System.out.println("GameFinished");
		super.show();
		
		var stateMachine = getWindowStateManager().getStateMachine();
		var winnerPiece = stateMachine.getWinner();
		
		String winnerName = winnerPiece == null ? "no one" : stateMachine.getPlayer(winnerPiece).getName();
		
		playerNameLabel.setText(winnerName);
		
		board.updateBoard(stateMachine.getGameArea());
	}
	
	
	@FXML
	public void backToMenu(ActionEvent actionEvent) {
		getWindowStateManager().leave();
	}
}
