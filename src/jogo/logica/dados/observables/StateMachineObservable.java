package jogo.logica.dados.observables;

import jogo.logica.StateMachine;
import jogo.logica.dados.dataViewers.GameDataViewer;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.dados.dataViewers.PlayerViewer;
import jogo.logica.estados.Connect4States;
import jogo.logica.estados.GameState;
import jogo.logica.estados.GameToStart;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class StateMachineObservable extends StateMachine {
	
	public static final String CHANGE_STATE = "CHANGE_STATE";
	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	
	public StateMachineObservable(GameState gameState) {
		currentState = gameState;
	}
	
	public StateMachineObservable() {
		currentState = new GameToStart();
	}
	
	public void addChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}
	
	public void removeChangeListener(PropertyChangeListener stateChangeListener) {
		changeSupport.removePropertyChangeListener(stateChangeListener);
	}
	
	@Override
	protected void setState(GameState newState) {
		boolean changed = currentState != newState;
		currentState = newState;
		if (changed) changeSupport.firePropertyChange(CHANGE_STATE, null, getState());
	}
	
	public void setGameState(GameState newState) {
		setState(newState);
	}
}
