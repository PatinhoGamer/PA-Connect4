package jogo.logica.estados.connect4;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;

public class ComputerPlays extends GameAbstractState {
	
	private Piece computerPlayer;
	
	public ComputerPlays(Connect4Logic game, Piece nextPlayer) {
		super(game);
		this.computerPlayer = nextPlayer;
	}
	
	@Override
	public GameAbstractState executePlay() {
		while (true) {
			int columnToPlayAt = (int) Math.floor(Math.random() * Connect4Logic.WIDTH) + 1;
			boolean columnWasntFull = game.playAt(computerPlayer, columnToPlayAt);
			
			if (columnWasntFull)
				return stateAfterPlay(computerPlayer);
		}
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.ComputerPlays;
	}
}
