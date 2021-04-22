package jogo.logica.estados;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.dados.PlayerType;

public class GameToStart extends GameAbstractState {
	
	public GameToStart(Connect4Logic gameLogic) {
		super(gameLogic);
	}
	
	@Override
	public GameAbstractState startGameWithPlayers(Player player1, Player player2) {
		if (!game.setPlayers(player1, player2))
			return this;
		
		Piece startingPlayer = Piece.PLAYER1;
		if (Math.random() > 0.5f)
			startingPlayer = Piece.PLAYER2;
		
		if (game.getPlayerFromEnum(startingPlayer).getType() == PlayerType.COMPUTER)
			return new ComputerPlays(game, startingPlayer);
		return new WaitingPlayerMove(game, startingPlayer);
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.GameToStart;
	}
}
