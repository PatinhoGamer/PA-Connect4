package jogo.iu.gui;

import javafx.fxml.FXMLLoader;

import java.net.URL;

public class ResourceLoader {
	
	public static final String FXML_GAMETOSTART = "GameToStart";
	public static final String FXML_MENU = "Menu";
	public static final String FXML_PLAYING_MINIGAME = "PlayingMiniGame";
	public static final String FXML_BOARD = "InGame";
	public static final String FXML_CHOOSING_REPLAY = "ChoosingReplay";
	public static final String FXML_WATCHING_REPLAY = "WatchingReplay";
	public static final String FXML_CHECK_PLAYER_MINIGAME = "CheckPlayerWantsMiniGame";
	public static final String FXML_GAME_FINISHED = "GameFinished";
	
	
	
	public static FXMLLoader loadFXML(String fileName){
		String filePath = "fxml/" + fileName + ".fxml";
		URL resource = ResourceLoader.class.getResource(filePath);
		return new FXMLLoader(resource);
	}
}
