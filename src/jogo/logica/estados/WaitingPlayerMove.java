package jogo.logica.estados;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;
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
		return stateAfterPlay(playerPiece);
	}
	
	@Override
	public GameAbstractState clearColumn(int column) {
		boolean success = game.clearColumn(playerPiece, column);
		if(!success)
			return this;
		return stateAfterPlay(playerPiece);
	}
	
	@Override
	public GameAbstractState rollback(int amount) {
		if (!game.rollback(playerPiece, amount))
			return this;
		
		Piece nextPlayer = playerPiece;
		if (amount % 2 == 1)
			nextPlayer = nextPlayer.getOther();
		
		if (game.getPlayerFromEnum(nextPlayer).getType() == PlayerType.COMPUTER)
			return new ComputerPlays(game, nextPlayer);
		return new WaitingPlayerMove(game, nextPlayer);
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
