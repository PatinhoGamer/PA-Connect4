package jogo.logica.estados;

import jogo.logica.GameSaver;
import jogo.logica.dados.Player;
import jogo.logica.dados.observables.GameDataObservable;

public class GameFinished extends GameAbstractState {
	
	public GameFinished(GameDataObservable game) {
		super(game);
		
		var winner = getWinner();
		Player winnerPlayer = game.getPlayerFromEnum(winner);
		Player loserPlayer = game.getPlayerFromEnum(winner.getOther());
		
		GameSaver.saveReplay(game.getGameActions(), winnerPlayer.getName(), loserPlayer.getName());
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.GameFinished;
	}
}
