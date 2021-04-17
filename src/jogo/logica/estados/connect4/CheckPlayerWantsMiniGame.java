package jogo.logica.estados.connect4;

import jogo.logica.Connect4Logic;

public class CheckPlayerWantsMiniGame extends GameAbstractState {
	
	
	
	public CheckPlayerWantsMiniGame(Connect4Logic game) {
		super(game);
	}
	
	
	@Override
	public Connect4States getState() {
		return Connect4States.CheckPlayerWantsMiniGame;
	}
}
