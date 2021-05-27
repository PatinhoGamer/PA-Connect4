package jogo.iu.gui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import jogo.logica.GameData;
import jogo.logica.dados.Piece;

public class Board extends FlowPane {
	
	public static final int SQUARE_SIZE = 40;
	public static final int GRID_PADDING = 5;
	
	public static final CornerRadii ROUND_CORNER = new CornerRadii(SQUARE_SIZE / 2d);
	
	public static final Color PLAYER1_COLOR = Color.RED;
	public static final Color PLAYER2_COLOR = Color.YELLOW;
	public static final Color EMPTY_COLOR = Color.GRAY;
	
	private Pane[][] paneArea;
	private Pane root;
	
	public Board(OnCollumClicked handler) {
		setAlignment(Pos.CENTER);
		
		initializeGrid(handler);
		
		getChildren().add(root);
	}
	
	private void initializeGrid(OnCollumClicked handler) {
		paneArea = new Pane[GameData.HEIGHT][GameData.WIDTH];
		
		VBox[] columns = new VBox[GameData.WIDTH];
		for (int column = 0; column < GameData.WIDTH; column++) {
			
			VBox curColumn = new VBox(Board.GRID_PADDING);
			if (handler != null) {
				int finalColumn = column + 1;
				curColumn.setOnMouseClicked(mouseEvent -> handler.onCollumClicked(finalColumn));
			}
			columns[column] = curColumn;
			fillGridLine(column, curColumn, paneArea);
		}
		
		root = new HBox(Board.GRID_PADDING);
		root.getChildren().addAll(columns);
	}
	
	private void fillGridLine(int column, VBox curColumn, Pane[][] paneArea) {
		for (int line = 0; line < GameData.HEIGHT; line++) {
			Pane spot = new Pane();
			spot.setPrefSize(SQUARE_SIZE, SQUARE_SIZE);
			Connect4UI.changeBackground(spot, EMPTY_COLOR, ROUND_CORNER);
			
			curColumn.getChildren().add(spot);
			paneArea[line][column] = spot;
		}
	}
	
	public void updateBoard(Piece[][] area) {
		Platform.runLater(() -> {
			for (int line = 0; line < area.length; line++) {
				for (int column = 0; column < area[0].length; column++) {
					if (area[line][column] == null)
						Connect4UI.changeBackground(paneArea[line][column], EMPTY_COLOR, ROUND_CORNER);
					else if (area[line][column] == Piece.PLAYER1)
						Connect4UI.changeBackground(paneArea[line][column], PLAYER1_COLOR, ROUND_CORNER);
					else
						Connect4UI.changeBackground(paneArea[line][column], PLAYER2_COLOR, ROUND_CORNER);
				}
			}
		});
	}
	
	public interface OnCollumClicked {
		void onCollumClicked(int collum);
	}
}
