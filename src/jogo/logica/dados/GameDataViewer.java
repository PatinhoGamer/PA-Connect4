package jogo.logica.dados;

import jogo.logica.GameDataObservable;

import java.beans.PropertyChangeListener;
import java.util.List;

public class GameDataViewer {
	
	private final GameDataObservable gameDataObservable;
	
	public GameDataViewer(GameDataObservable gameDataObservable) {
		this.gameDataObservable = gameDataObservable;
	}
	
	public void addChangeListener(GameDataObservable.Changes change, PropertyChangeListener listener) {
		gameDataObservable.addChangeListener(change, listener);
	}
	
	public Piece[][] getGameArea() {
		return gameDataObservable.getGameArea();
	}
	
	public int getAvailableRollbacks() {
		return gameDataObservable.getAvailableRollbacks();
	}
	
	public List<String> getGameActions() {
		return gameDataObservable.getGameActions();
	}
	
	public PlayerViewer getPlayerFromEnum(Piece playerPiece) {
		return new PlayerViewer(gameDataObservable.getPlayerFromEnum(playerPiece));
	}
	
}
