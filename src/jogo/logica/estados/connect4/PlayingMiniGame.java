package jogo.logica.estados.connect4;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.dados.PlayerType;
import jogo.logica.minigames.TimedGame;

public class PlayingMiniGame extends GameAbstractState {
	
	private final TimedGame miniGame;
	private final Piece playerPiece;
	
	public PlayingMiniGame(Connect4Logic game, Piece playerPiece, TimedGame miniGame) {
		super(game);
		this.playerPiece = playerPiece;
		this.miniGame = miniGame;
	}
	
	@Override
	public GameAbstractState ignoreOrEndMiniGame() {
		if (miniGame.playerManagedToDoIt()) {
			Player player = game.getPlayerFromEnum(playerPiece);
			player.resetSpecialCounter();
			player.setSpecialPieces(player.getSpecialPieces() + 1);
			game.getGameActions().add("wonMinigame:" + playerPiece);
			return new WaitingPlayerMove(game, playerPiece);
		}
		
		game.getGameActions().add("lostdMiniGame:" + playerPiece);
		
		Piece other = playerPiece.getOther();
		Player nextPlayer = getGame().getPlayerFromEnum(other);
		if (nextPlayer.getType() == PlayerType.COMPUTER)
			return new ComputerPlays(game, other);
		
		return new WaitingPlayerMove(game, other);
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
