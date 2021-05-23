package jogo.iu.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jogo.iu.gui.controllers.ChoosingReplay;
import jogo.logica.Connect4Logic;
import jogo.logica.GameSaver;
import jogo.logica.Replayer;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.dados.Replay;
import jogo.logica.estados.GameToStart;
import jogo.logica.estados.StateMachine;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Optional;
import java.util.ResourceBundle;

public class Connect4UI extends Application {
	
	public static final int SQUARE_SIZE = 40;
	public static final int GRID_PADDING = 5;
	
	public static final CornerRadii ROUND_CORNER = new CornerRadii(SQUARE_SIZE / 2d);
	
	public static final Color PLAYER1_COLOR = Color.RED;
	public static final Color PLAYER2_COLOR = Color.YELLOW;
	public static final Color NOPLAYER_COLOR = Color.GRAY;
	
	public static final String FXML_GAMETOSTART = "GameToStart";
	public static final String FXML_MENU = "Menu";
	public static final String FXML_PLAYING_MINIGAME = "PlayingMiniGame";
	public static final String FXML_BOARD = "InGame";
	public static final String FXML_CHOOSING_REPLAY = "ChoosingReplay";
	public static final String FXML_WATCHING_REPLAY = "WatchingReplay";
	
	private Scene scene;
	private Pane root;
	
	private StateMachine stateMachine;
	
	private static Connect4UI instance;
	private Stage stage;
	private Replay replayToWatch;
	
	public static Connect4UI getInstance() {
		return instance;
	}
	
	public Connect4UI() {
		instance = this;
	}
	
	public static void fillGridLine(int column, VBox curColumn, Pane[][] paneArea) {
		for (int line = 0; line < Connect4Logic.HEIGHT; line++) {
			Pane spot = new Pane();
			spot.setPrefSize(SQUARE_SIZE, SQUARE_SIZE);
			Connect4UI.changeBackground(spot, NOPLAYER_COLOR, ROUND_CORNER);
			
			curColumn.getChildren().add(spot);
			paneArea[line][column] = spot;
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		changeScene(FXML_MENU);
		
		stage.setOnCloseRequest(event -> {
			try {
				stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		stage.setTitle("Connect 4");
		stage.show();
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
	}
	
	public void goBackToMenu() {
		stateMachine = null;
		changeScene(FXML_MENU);
	}
	
	public void startGame() {
		stateMachine = new StateMachine(new GameToStart(new Connect4Logic()));
		changeToRightState();
	}
	
	public void loadGameFromFile(String s) throws IOException, ClassNotFoundException {
		try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(s))) {
			stateMachine = (StateMachine) objectInputStream.readUnshared();
		}
		changeToRightState();
	}
	
	public void saveCurrentStateMachine(String filePath) throws IOException {
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
			objectOutputStream.writeUnshared(stateMachine);
		}
	}
	
	public void startGameWithPlayers(Player playe1, Player player2) {
		stateMachine.startGameWithPlayers(playe1, player2);
		changeToRightState();
	}
	
	private void changeToRightState() {
		switch (stateMachine.getState()) {
			case GameToStart -> changeScene(FXML_GAMETOSTART);
			case WaitingPlayerMove, ComputerPlays -> changeScene(FXML_BOARD);
			case CheckPlayerWantsMiniGame,GameFinished -> {
				// no need to do anything. this can never happen
				System.out.println("Why did this happen? . switch to right state");
			}
			case PlayingMiniGame -> changeScene(FXML_PLAYING_MINIGAME);
			default -> throw new IllegalStateException("Unexpected value: " + stateMachine.getState());
		}
	}
	
	public static void updateBoard(Piece[][] area, Pane[][] paneArea) {
		for (int line = 0; line < area.length; line++) {
			for (int column = 0; column < area[0].length; column++) {
				if (area[line][column] == null)
					Connect4UI.changeBackground(paneArea[line][column], NOPLAYER_COLOR, ROUND_CORNER);
				else if (area[line][column] == Piece.PLAYER1)
					Connect4UI.changeBackground(paneArea[line][column], PLAYER1_COLOR, ROUND_CORNER);
				else
					Connect4UI.changeBackground(paneArea[line][column], PLAYER2_COLOR, ROUND_CORNER);
			}
		}
	}
	
	public void runMiniGame() throws IOException {
		Stage miniGameStage = new Stage();
		miniGameStage.setTitle("Minigame");
		
		Pane root = loadParent(FXML_PLAYING_MINIGAME).load();
		
		miniGameStage.setScene(new Scene(root));
		miniGameStage.setOnCloseRequest(we -> {
			try {
				Connect4UI.getInstance().stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		miniGameStage.initModality(Modality.APPLICATION_MODAL);
		
		miniGameStage.showAndWait();
	}
	
	public void openMessageDialog(Alert.AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(message);
		alert.showAndWait();
	}
	
	public boolean openMessageWithCancel(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isEmpty()) return false;
		return !result.get().getButtonData().isCancelButton();
	}
	
	public File openFileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select the file");
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("Connect 4 Save File", '*' + GameSaver.fileExtension));
		return fileChooser.showOpenDialog(getStage());
	}
	
	public File saveFileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose where to save the file");
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("Connect 4 Save File", '*' + GameSaver.fileExtension));
		return fileChooser.showSaveDialog(getStage());
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public FXMLLoader loadParent(String fileName) throws IOException {
		URL resource = getClass().getResource("/" + fileName + ".fxml");
		return new FXMLLoader(resource);
	}
	
	public void changeScene(String fileName) {
		try {
			root = loadParent(fileName).load();
			scene = new Scene(root);
			stage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void changeBackground(Region region, Color color, CornerRadii corner) {
		region.setBackground(new Background(new BackgroundFill(color, corner, null)));
	}
	
	public StateMachine getStateMachine() {
		return stateMachine;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void watchReplay(Replay replay) {
		replayToWatch = replay;
		changeScene(Connect4UI.FXML_WATCHING_REPLAY);
	}
	
	public Replay getReplayToWatch() {
		var temp = replayToWatch;
		replayToWatch = null;
		return temp;
	}
}
