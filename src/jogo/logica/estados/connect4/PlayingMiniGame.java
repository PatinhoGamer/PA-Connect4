package jogo.logica.estados.connect4;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.estados.minigames.TimedGame;

public class PlayingMiniGame extends GameAbstractState {
	
	private final TimedGame miniGame;
	private final Piece playerPiece;
	
	public PlayingMiniGame(Connect4Logic game, Piece playerPiece, TimedGame miniGame) {
		super(game);
		this.playerPiece = playerPiece;
		this.miniGame = miniGame;
	}
	
	@Override
	public GameAbstractState ignoreAndEndMiniGame() {
		if (miniGame.playerManagedToDoIt()) {
			Player player = game.getPlayerFromEnum(playerPiece);
			player.setSpecialPieces(player.getSpecialPieces() + 1);
			return new WaitingPlayerMove(game, playerPiece);
		}
		
		return new WaitingPlayerMove(game, playerPiece.getOther());
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
