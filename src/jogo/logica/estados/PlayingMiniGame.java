package jogo.logica.estados;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;
import jogo.logica.minigames.TimedMiniGame;

public class PlayingMiniGame extends GameAbstractState {
	
	private final TimedMiniGame miniGame;
	private final Piece playerPiece;
	
	public PlayingMiniGame(Connect4Logic game, Piece playerPiece, TimedMiniGame miniGame) {
		super(game);
		this.playerPiece = playerPiece;
		this.miniGame = miniGame;
	}
	
	@Override
	public GameState endMiniGame() {
		if (miniGame.playerManagedToDoIt()) {
			game.playerWonMiniGame(playerPiece);
			return new WaitingPlayerMove(game, playerPiece);
		}
		
		game.playerLostMiniGame(playerPiece);
		
		return stateAfterPlay(playerPiece);
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
