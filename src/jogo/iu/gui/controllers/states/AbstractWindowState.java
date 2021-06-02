package jogo.iu.gui.controllers.states;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import jogo.iu.gui.GameWindowStateManager;
import jogo.iu.gui.ResourceLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractWindowState implements StateWindow, Initializable {
	
	private final GameWindowStateManager windowStateManager;
	
	private Scene scene;
	
	protected AbstractWindowState(GameWindowStateManager windowStateManager) {
		this.windowStateManager = windowStateManager;
		firstSetupWindow();
	}
	
	protected AbstractWindowState(GameWindowStateManager windowStateManager, String fxmlPath) {
		this.windowStateManager = windowStateManager;
		
		FXMLLoader fxmlLoader = ResourceLoader.loadFXML(fxmlPath);
		fxmlLoader.setController(this);
		try {
			Parent root = fxmlLoader.load();
			scene = new Scene(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void firstSetupWindow() {
	}
	
	@Override
	public void hide() {
	}
	
	@Override
	public void show() {
	}
	
	@Override
	public final void initialize(URL url, ResourceBundle resourceBundle) {
		firstSetupWindow();
	}
	
	protected final GameWindowStateManager getWindowStateManager() {
		return windowStateManager;
	}
	
	@Override
	public final Scene getScene() {
		return scene;
	}
	
}
