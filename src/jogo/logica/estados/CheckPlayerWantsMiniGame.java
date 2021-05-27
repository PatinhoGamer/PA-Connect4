package jogo.logica.estados;

import jogo.logica.GameDataObservable;
import jogo.logica.dados.Piece;
import jogo.logica.minigames.MathMiniGame;
import jogo.logica.minigames.TimedMiniGame;
import jogo.logica.minigames.WordsMiniGame;

public class CheckPlayerWantsMiniGame extends GameAbstractState {
	
	public CheckPlayerWantsMiniGame(GameDataObservable game) {
		super(game);
	}
	
	@Override
	public GameState startMiniGame() {
		int nextActivity = game.getPlayerFromEnum(getCurrentPlayerPiece()).getNextActivity();
		TimedMiniGame miniGame;
		if (nextActivity == 0) miniGame = new MathMiniGame();
		else miniGame = new WordsMiniGame();
		return new PlayingMiniGame(game, miniGame);
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
