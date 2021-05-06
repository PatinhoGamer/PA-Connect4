package jogo.logica.estados;

import jogo.logica.Connect4Logic;
import jogo.logica.GameSaver;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;

public class GameFinished extends GameAbstractState {
	
	private Piece winner;
	
	public GameFinished(Connect4Logic game, Piece winner) {
		super(game);
		this.winner = winner;
		
		Player winnerPlayer = game.getPlayerFromEnum(winner);
		Player loserPlayer = game.getPlayerFromEnum(winner.getOther());
		
		GameSaver.saveReplay(game.getGameActions(), winnerPlayer.getName(), loserPlayer.getName());
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
