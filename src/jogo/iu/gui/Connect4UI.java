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
import javafx.stage.Stage;
import jogo.logica.GameSaver;
import jogo.logica.dados.Replay;
import jogo.logica.StateMachine;
import jogo.logica.estados.GameState;

import java.io.*;
import java.util.Optional;

public class Connect4UI extends Application {
	
	private Stage stage;
	private Scene scene;
	private Pane root;
	
	private static Connect4UI instance;
	private Replay replayToWatch;
	private GameWindowStateManager gameWindowStateManager;
	
	public static Connect4UI getInstance() {
		return instance;
	}
	
	public Connect4UI() {
		instance = this;
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		changeScene(ResourceLoader.FXML_MENU);
		
		stage.setOnCloseRequest(event -> {
			try {
				stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		stage.setTitle("Connect 4");
		stage.setResizable(false);
		stage.show();
	}
	
	public void goBackToMenu() {
		changeScene(ResourceLoader.FXML_MENU);
	}
	
	public void startGame() {
		try {
			gameWindowStateManager = new GameWindowStateManager(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void continueGameFromFile(String path) {
		try {
			var state = loadGameFromFile(path);
			gameWindowStateManager = new GameWindowStateManager(stage, state);
		} catch (Exception e) {
			e.printStackTrace();
			openMessageDialog(Alert.AlertType.ERROR,"Load Error", "Error opening saved game");
		}
	}
	
	public GameState loadGameFromFile(String absolutePath) throws IOException, ClassNotFoundException {
		return GameSaver.getInstance().loadGameFromFile(absolutePath);
	}
	
	public void saveCurrentStateMachine(String absolutePath, GameState gameState) throws IOException {
		GameSaver.getInstance().saveCurrentStateToFile(gameState,absolutePath);
	}
	
	public void openMessageDialog(Alert.AlertType type, String title, String message) {
		var alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(message);
		alert.showAndWait();
	}
	
	public boolean openMessageWithCancel(String title, String message) {
		var alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isEmpty()) return false;
		return !result.get().getButtonData().isCancelButton();
	}
	
	private FileChooser getFileChooser() {
		var fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("."));
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("Connect4 SaveFile", '*' + GameSaver.fileExtensionSave));
		return fileChooser;
	}
	
	public File openFileChooser() {
		var fileChooser = getFileChooser();
		fileChooser.setTitle("Select the file");
		return fileChooser.showOpenDialog(getStage());
	}
	
	public File saveFileChooser() {
		var fileChooser = getFileChooser();
		fileChooser.setTitle("Choose where to save the file");
		return fileChooser.showSaveDialog(getStage());
	}
	
	public Stage getStage() {
		return stage;
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
	
	public void watchReplay(Replay replay) {
		replayToWatch = replay;
		changeScene(ResourceLoader.FXML_WATCHING_REPLAY);
	}
	
	public Replay getReplayToWatch() {
		var temp = replayToWatch;
		replayToWatch = null;
		return temp;
	}
	
	public FXMLLoader loadParent(String fileName) throws IOException {
		return ResourceLoader.loadFXML(fileName);
	}
	
	public void exit() {
		Platform.exit();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
