package jogo.logica.estados.connect4;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Player;
import jogo.logica.dados.Piece;
import jogo.logica.dados.PlayerType;

public class ComputerPlays extends GameAbstractState {
	
	private Piece computerPlayer;
	
	public ComputerPlays(Connect4Logic game, Piece nextPlayer) {
		super(game);
		this.computerPlayer = nextPlayer;
	}
	
	@Override
	public GameAbstractState executePlay() {
		while (true) {
			int columnToPlayAt = (int) Math.floor(Math.random() * Connect4Logic.WIDTH) + 1;
			boolean columnWasntFull = game.playAt(computerPlayer, columnToPlayAt);
			if (columnWasntFull) {
				//TODO remove this mega duplication
				GameFinished finishedState = checkFinishedState();
				if (finishedState != null) return finishedState;
				
				Piece nextPlayer = computerPlayer.getOther();
				Player trulyNextPlayer = getGame().getPlayerFromEnum(nextPlayer);
				
				if (trulyNextPlayer.getType() == PlayerType.COMPUTER)
					return new ComputerPlays(game, nextPlayer);
				
				if (trulyNextPlayer.getSpecialPiecesCounter() == 4) {
					trulyNextPlayer.resetSpecialCounter();
					return new CheckPlayerWantsMiniGame(game, nextPlayer);
				}
				
				return new WaitingPlayerMove(game, nextPlayer);
			}
		}
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.ComputerPlays;
	}
}
