package jogo.logica.estados.connect4;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.PlayerPiece;
import jogo.logica.dados.Player;

public class GameToStart extends GameAbstractState {
	
	public GameToStart(Connect4Logic gameLogic) {
		super(gameLogic);
	}
	
	@Override
	public GameAbstractState setPlayers(Player player1, Player player2) {
		game.setPlayers(player1, player2);
		PlayerPiece startingPlayer = PlayerPiece.PLAYER1;
		if(Math.random() > 0.5f)
			startingPlayer = PlayerPiece.PLAYER2;
			
		return new WaitingPlayerMove(game,startingPlayer);
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.GameToStart;
	}
}
