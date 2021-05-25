package jogo.iu.gui;

import javafx.fxml.FXMLLoader;

import java.net.URL;

public class ResourceLoader {
	
	public static FXMLLoader loadFXML(String fileName){
		String filePath = "fxml/" + fileName + ".fxml";
		URL resource = ResourceLoader.class.getResource(filePath);
		return new FXMLLoader(resource);
	}
}
