package jogo.logica.estados;

import jogo.logica.GameDataObservable;
import jogo.logica.minigames.TimedMiniGame;

public class PlayingMiniGame extends GameAbstractState {
	
	private final TimedMiniGame miniGame;
	
	public PlayingMiniGame(GameDataObservable game, TimedMiniGame miniGame) {
		super(game);
		this.miniGame = miniGame;
	}
	
	@Override
	public GameState endMiniGame() {
		if (miniGame.playerManagedToDoIt()) {
			game.playerWonMiniGame();
			return new WaitingPlayerMove(game);
		}
		
		game.playerLostMiniGame();
		
		return stateAfterPlay();
	}
	
	@Override
	public TimedMiniGame getMiniGame() {
		return miniGame;
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.PlayingMiniGame;
	}
}
