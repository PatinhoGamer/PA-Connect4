package jogo.logica.estados;

import jogo.logica.dados.observables.GameDataObservable;
import jogo.logica.dados.Piece;
import jogo.logica.dados.PlayerType;

public class WaitingPlayerMove extends GameAbstractState {
	
	public WaitingPlayerMove(GameDataObservable game) {
		super(game);
	}
	
	@Override
	public GameState playAt(int column) {
		boolean columnWasFull = !game.playAt(column);
		if (columnWasFull)
			return this;
		return stateAfterPlay();
	}
	
	@Override
	public GameState clearColumn(int column) {
		boolean success = game.clearColumn(column);
		if(!success)
			return this;
		return stateAfterPlay();
	}
	
	@Override
	public GameState rollback(int amount) {
		if (!game.rollback( amount))
			return this;
		
		Piece nextPlayer = getCurrentPlayerPiece();
		
		if (game.getPlayerFromEnum(nextPlayer).getType() == PlayerType.COMPUTER)
			return new ComputerPlays(game);
		return new WaitingPlayerMove(game);
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.WaitingPlayerMove;
	}
}
