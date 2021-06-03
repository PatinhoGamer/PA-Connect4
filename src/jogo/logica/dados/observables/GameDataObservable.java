package jogo.logica.dados.observables;

import jogo.logica.GameData;
import jogo.logica.dados.Piece;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GameDataObservable extends GameData implements IGameDataObservable {
	
	//Observable Changes
	public enum Changes {
		PlayerWon, BoardChanged, PlayerPlayedAt, PlayerRollback, PlayerClearedColumn
	}
	
	private final PropertyChangeSupport changer = new PropertyChangeSupport(this);
	
	@Override
	public void addChangeListener(Changes change, PropertyChangeListener listener) {
		changer.addPropertyChangeListener(String.valueOf(change), listener);
	}
	
	private void firePropertyChange(Changes change, Object newValue) {
		changer.firePropertyChange(String.valueOf(change), -1, newValue);
	}
	
	@Override
	public void removeChangeListener(PropertyChangeListener listener) {
		changer.removePropertyChangeListener(listener);
	}
	
	@Override
	public boolean playAt(Piece playerPiece, int column) {
		boolean columnWasntFull = super.playAt(playerPiece, column);
		if (columnWasntFull) {
			firePropertyChange(Changes.PlayerPlayedAt, column);
			firePropertyChange(Changes.BoardChanged, getGameArea());
		}
		return columnWasntFull;
	}
	
	@Override
	public boolean clearColumn(Piece playerPiece, int column) {
		boolean success = super.clearColumn(playerPiece, column);
		if (success) {
			firePropertyChange(Changes.PlayerClearedColumn, column);
			firePropertyChange(Changes.BoardChanged, getGameArea());
		}
		return success;
	}
	
	@Override
	public boolean rollback(Piece playerPiece, int amount) {
		boolean success = super.rollback(playerPiece, amount);
		if (success) {
			firePropertyChange(Changes.PlayerRollback, amount);
			firePropertyChange(Changes.BoardChanged, getGameArea());
		}
		return success;
	}
	
	@Override
	public Piece checkWinner() {
		Piece whoWon = super.checkWinner();
		if (whoWon != null)
			firePropertyChange(Changes.PlayerWon, whoWon);
		return whoWon;
	}
	
}
