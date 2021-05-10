package jogo.logica.estados;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;
import jogo.logica.minigames.MathMiniGame;
import jogo.logica.minigames.TimedMiniGame;
import jogo.logica.minigames.WordsMiniGame;

public class CheckPlayerWantsMiniGame extends GameAbstractState {
	
	private final Piece player;
	
	public CheckPlayerWantsMiniGame(Connect4Logic game, Piece nextPlayer) {
		super(game);
		this.player = nextPlayer;
	}
	
	@Override
	public GameState startMiniGame() {
		int nextActivity = game.getPlayerFromEnum(player).getNextActivity();
		TimedMiniGame miniGame;
		if (nextActivity == 0) miniGame = new MathMiniGame();
		else miniGame = new WordsMiniGame();
		return new PlayingMiniGame(game, player, miniGame);
	}
	
	@Override
	public GameState ignoreMiniGame() {
		game.playerIgnoredMiniGame(player);
		return new WaitingPlayerMove(game, player);
	}
	
	@Override
	public Piece getCurrentPlayer() {
		return player;
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.CheckPlayerWantsMiniGame;
	}
}
