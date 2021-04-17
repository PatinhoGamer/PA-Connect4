package jogo.logica.estados.connect4;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.PlayerPiece;

public class GameFinished extends GameAbstractState {
	
	private PlayerPiece winner;
	
	public GameFinished(Connect4Logic game, PlayerPiece winner) {
		super(game);
		this.winner = winner;
	}
	
	@Override
	public PlayerPiece getWinner(){
		return winner;
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.GameFinished;
	}
}
