package jogo.iu.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import jogo.logica.GameData;
import jogo.logica.dados.Piece;
import jogo.logica.dados.dataViewers.GameDataViewer;
import jogo.logica.dados.observables.GameDataObservable;


public class GameBoardNode extends FlowPane {
	
	public static final int SQUARE_SIZE = 40;
	public static final int GRID_PADDING = 5;
	
	public static final CornerRadii ROUND_CORNER = new CornerRadii(SQUARE_SIZE / 2d);
	
	public static final Color PLAYER1_COLOR = Color.RED;
	public static final Color PLAYER2_COLOR = Color.YELLOW;
	public static final Color EMPTY_COLOR = Color.GRAY;
	
	private Pane[][] paneArea;
	private Pane[] topDiscs;
	
	private Pane root;
	private final GameDataViewer gameDataObservable;
	
	public GameBoardNode(OnCollumClicked handler, GameDataViewer gameDataObservable) {
		setAlignment(Pos.CENTER);
		
		initializeGrid(handler);
		
		this.setPadding(new Insets(10));
		
		getChildren().add(root);
		
		this.gameDataObservable = gameDataObservable;
		
		if (gameDataObservable != null) {
			gameDataObservable.addChangeListener(GameDataObservable.Changes.PlayerClearedColumn, evt -> {
				int column = (int) evt.getNewValue();
				//clearedColumn(column - 1); // gave up on making the animation. too much work for such a little thing
			});
			gameDataObservable.addChangeListener(GameDataObservable.Changes.PlayerPlayedAt, evt -> {
				int column = (int) evt.getNewValue();
				//playedAt(column - 1); // the listeners work but don't do anything
			});
		}
	}
	
	public GameBoardNode(GameDataViewer gameDataObservable) {
		this(null, gameDataObservable);
	}
	
	public GameBoardNode() {
		this(null, null);
	}
	
	private void initializeGrid(OnCollumClicked handler) {
		paneArea = new Pane[GameData.HEIGHT][GameData.WIDTH];
		this.topDiscs = new Pane[GameData.WIDTH];
		
		VBox[] columns = new VBox[GameData.WIDTH];
		for (int columnIndex = 0; columnIndex < GameData.WIDTH; columnIndex++) {
			
			VBox curColumn = new VBox(GameBoardNode.GRID_PADDING);
			
			if (handler != null) {
				int finalColumn = columnIndex;
				
				topDiscs[finalColumn] = new Pane();
				topDiscs[finalColumn].setPrefSize(SQUARE_SIZE, SQUARE_SIZE);
				
				curColumn.hoverProperty().addListener((observableValue, oldValue, newValue) -> {
					if (newValue) {
						changeDiscColor(gameDataObservable.getCurrentPlayerPiece(), topDiscs[finalColumn]);
						topDiscs[finalColumn].setVisible(true);
					} else {
						topDiscs[finalColumn].setVisible(false);
					}
				});
				curColumn.setOnMouseClicked(mouseEvent -> handler.onCollumClicked(finalColumn + 1));
			}
			columns[columnIndex] = curColumn;
			
			if (topDiscs[columnIndex] != null)
				curColumn.getChildren().add(topDiscs[columnIndex]);
			
			fillGridLine(columnIndex, curColumn, paneArea);
		}
		
		root = new HBox(GameBoardNode.GRID_PADDING);
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
			for (int line = 0; line < area.length; line++)
				for (int column = 0; column < area[0].length; column++)
					changeDiscColor(area[line][column], paneArea[line][column]);
		});
	}
	
	private void changeDiscColor(Piece piece, Region region) {
		if (piece == null)
			Connect4UI.changeBackground(region, EMPTY_COLOR, ROUND_CORNER);
		else if (piece == Piece.PLAYER1)
			Connect4UI.changeBackground(region, PLAYER1_COLOR, ROUND_CORNER);
		else
			Connect4UI.changeBackground(region, PLAYER2_COLOR, ROUND_CORNER);
	}
	
	public Pane getHighestFreeSpot(int column) {
		var area = gameDataObservable.getGameArea();
		for (int line = 0; line < area.length; line++) {
			if (area[column][line] == null)
				return paneArea[column][line];
		}
		return null;
	}
	
	public interface OnCollumClicked {
		void onCollumClicked(int collum);
	}
}
