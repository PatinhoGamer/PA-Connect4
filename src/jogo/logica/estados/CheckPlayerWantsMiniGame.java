package jogo.logica.estados;

import jogo.logica.dados.observables.GameDataObservable;
import jogo.logica.minigames.MathMiniGame;
import jogo.logica.minigames.ITimedMiniGame;
import jogo.logica.minigames.WordsMiniGame;

public class CheckPlayerWantsMiniGame extends GameAbstractState {
	
	public CheckPlayerWantsMiniGame(GameDataObservable game) {
		super(game);
	}
	
	@Override
	public GameState acceptMiniGame() {
		game.generateMiniGameForCurrentPlayer();
		return new PlayingMiniGame(game);
	}
	
	@Override
	public GameState ignoreMiniGame() {
		game.playerIgnoredMiniGame();
		return new WaitingPlayerMove(game);
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.CheckPlayerWantsMiniGame;
	}
}
