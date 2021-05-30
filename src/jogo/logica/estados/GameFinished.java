package jogo.logica.estados;

import jogo.logica.dados.observables.GameDataObservable;
import jogo.logica.GameSaver;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;

public class GameFinished extends GameAbstractState {
	
	private Piece winner;
	
	public GameFinished(GameDataObservable game, Piece winner) {
		super(game);
		this.winner = winner;
		
		Player winnerPlayer = game.getPlayerFromEnum(winner);
		Player loserPlayer = game.getPlayerFromEnum(winner.getOther());
		
		GameSaver.saveReplay(game.getGameActions(), winnerPlayer.getName(), loserPlayer.getName());
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.GameFinished;
	}
}
