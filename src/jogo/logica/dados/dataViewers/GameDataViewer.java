package jogo.logica.dados.dataViewers;

import jogo.logica.dados.Piece;
import jogo.logica.dados.observables.GameDataObservable;
import jogo.logica.dados.observables.IGameDataObservable;

import java.beans.PropertyChangeListener;
import java.util.List;

public class GameDataViewer implements IGameDataObservable {
	
	private final GameDataObservable gameDataObservable;
	
	public GameDataViewer(GameDataObservable gameDataObservable) {
		this.gameDataObservable = gameDataObservable;
	}
	
	@Override
	public void addChangeListener(GameDataObservable.Changes change, PropertyChangeListener listener) {
		gameDataObservable.addChangeListener(change, listener);
	}
	
	@Override
	public void removeChangeListener(PropertyChangeListener listener) {
		gameDataObservable.removeChangeListener(listener);
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
	
	public Piece getCurrentPlayerPiece(){
		return gameDataObservable.getCurrentPlayerPiece();
	}
	
	public PlayerViewer getCurrentPlayer(){
		return new PlayerViewer(gameDataObservable.getCurrentPlayer());
	}
	
}
