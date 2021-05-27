package jogo.logica.estados;

import jogo.logica.GameData;
import jogo.logica.GameDataObservable;

public class ComputerPlays extends GameAbstractState {
	
	public ComputerPlays(GameDataObservable game) {
		super(game);
	}
	
	@Override
	public GameState executePlay() {
		while (true) {
			int columnToPlayAt = (int) Math.floor(Math.random() * GameData.WIDTH) + 1;
			boolean columnWasntFull = game.playAt(columnToPlayAt);
			
			if (columnWasntFull)
				return stateAfterPlay();
		}
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.ComputerPlays;
	}
}
