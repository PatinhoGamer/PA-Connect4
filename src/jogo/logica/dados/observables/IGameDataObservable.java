package jogo.logica.dados.observables;

import java.beans.PropertyChangeListener;

public interface IGameDataObservable {

	void addChangeListener(GameDataObservable.Changes change, PropertyChangeListener listener);
	
	void removeChangeListener(PropertyChangeListener listener);
	
}
