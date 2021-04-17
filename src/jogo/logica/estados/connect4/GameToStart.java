package jogo.logica.estados.connect4;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.dados.PlayerType;

public class GameToStart extends GameAbstractState {
	
	public GameToStart(Connect4Logic gameLogic) {
		super(gameLogic);
	}
	
	@Override
	public GameAbstractState setPlayers(Player player1, Player player2) {
		game.setPlayers(player1, player2);
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
