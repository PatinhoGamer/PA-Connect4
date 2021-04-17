package jogo.logica.estados.connect4;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;

public class GameFinished extends GameAbstractState {
	
	private Piece winner;
	
	public GameFinished(Connect4Logic game, Piece winner) {
		super(game);
		this.winner = winner;
	}
	
	@Override
	public Piece getWinner() {
		return winner;
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.GameFinished;
	}
}
