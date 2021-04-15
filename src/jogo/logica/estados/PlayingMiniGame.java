package jogo.logica.estados;

import jogo.logica.Connect4Logic;

public class PlayingMiniGame extends GameAbstractState {
	
	public PlayingMiniGame(Connect4Logic game) {
		super(game);
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.PlayingMiniGame;
	}
}
