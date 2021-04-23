package jogo.logica.estados;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.GameSaver;
import jogo.logica.dados.Piece;

public class GameFinished extends GameAbstractState {
	
	private Piece winner;
	
	public GameFinished(Connect4Logic game, Piece winner) {
		super(game);
		this.winner = winner;
		GameSaver.saveReplay(game);
	}
	
	@Override
	public Piece getWinner() {
		return winner;
	}
	
	@Override
	public GameAbstractState restartGame() {
		return new GameToStart(new Connect4Logic());
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.GameFinished;
	}
}
