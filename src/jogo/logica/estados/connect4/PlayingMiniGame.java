package jogo.logica.estados.connect4;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.PlayerPiece;
import jogo.logica.estados.minigames.TimedGame;

public class PlayingMiniGame extends GameAbstractState {
	
	private final TimedGame miniGame;
	private final PlayerPiece playerPiece;
	
	public PlayingMiniGame(Connect4Logic game, PlayerPiece playerPiece, TimedGame miniGame) {
		super(game);
		this.playerPiece = playerPiece;
		this.miniGame = miniGame;
	}
	
	@Override
	public TimedGame getMiniGame() {
		return miniGame;
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.PlayingMiniGame;
	}
}
