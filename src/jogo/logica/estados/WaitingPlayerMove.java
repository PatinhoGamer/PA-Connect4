package jogo.logica.estados;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.dados.PlayerType;

public class WaitingPlayerMove extends GameAbstractState {
	
	private final Piece playerPiece;
	
	public WaitingPlayerMove(Connect4Logic game, Piece player) {
		super(game);
		this.playerPiece = player;
	}
	
	@Override
	public GameAbstractState playAt(int column) {
		boolean columnWasFull = !game.playAt(playerPiece, column);
		if (columnWasFull)
			return this;
		
		Player player = game.getPlayerFromEnum(playerPiece);
		player.incrementSpecialCounter();
		
		return stateAfterPlay(playerPiece);
	}
	
	@Override
	public GameAbstractState clearColumn(int column) {
		boolean success = game.clearColumn(playerPiece, column);
		if (success) {
			Piece nextPlayer = playerPiece.getOther();
			if (game.getPlayerFromEnum(nextPlayer).getType() == PlayerType.COMPUTER)
				return new ComputerPlays(game, nextPlayer);
			return new WaitingPlayerMove(game, nextPlayer);
		}
		return this;
	}
	
	@Override
	public GameAbstractState rollback() {
		game.rollback(playerPiece);
		return new WaitingPlayerMove(game, playerPiece);
	}
	
	@Override
	public Piece getCurrentPlayer() {
		return playerPiece;
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.WaitingPlayerMove;
	}
}
