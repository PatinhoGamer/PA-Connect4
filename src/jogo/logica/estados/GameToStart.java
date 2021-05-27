package jogo.logica.estados;

import jogo.logica.GameDataObservable;
import jogo.logica.dados.Player;

public class GameToStart extends GameAbstractState {
	
	public GameToStart(GameDataObservable gameLogic) {
		super(gameLogic);
	}
	
	public GameToStart() {
		this(new GameDataObservable());
	}
	
	@Override
	public GameState startGameWithPlayers(Player player1, Player player2) {
		if (!game.setPlayers(player1, player2))
			return this;
		
		if (game.isPlayerBot())
			return new ComputerPlays(game);
		return new WaitingPlayerMove(game);
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.GameToStart;
	}
}
