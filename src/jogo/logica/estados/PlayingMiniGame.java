package jogo.logica.estados;

import jogo.logica.dados.observables.GameDataObservable;
import jogo.logica.minigames.ITimedMiniGame;

public class PlayingMiniGame extends GameAbstractState {
	
	public PlayingMiniGame(GameDataObservable game) {
		super(game);
		//if (!game.hasMiniGameStarted())
			game.generateMiniGameForCurrentPlayer();
	}
	
	public GameState answerMiniGame(String answer) {
		game.checkMiniGameAnswer(answer);
		
		if (game.isMiniGameFinished()) {
			if (game.didPlayerWinMiniGame())
				return new WaitingPlayerMove(game);
			else
				return stateAfterPlay();
		}
		
		return this;
	}
	
	@Override
	public void startMiniGameTimer() {
		getMiniGame().start();
	}
	
	@Override
	public String getMiniGameQuestion() {
		return getMiniGame().getQuestion();
	}
	
	@Override
	public String getMiniGameObjective() {
		return getMiniGame().getGameObjective();
	}
	
	@Override
	public int getMiniGameAvailableTime() {
		return getMiniGame().getAvailableTime();
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.PlayingMiniGame;
	}
	
	private ITimedMiniGame getMiniGame() {
		return game.getMiniGame();
	}
}
