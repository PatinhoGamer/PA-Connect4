package jogo.logica.estados;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.PlayerPiece;
import jogo.logica.dados.Player;

public class WaitingPlayerMove extends GameAbstractState {
	
	private final PlayerPiece playerPiece;
	
	public WaitingPlayerMove(Connect4Logic game, PlayerPiece player) {
		super(game);
		this.playerPiece = player;
	}
	
	@Override
	public GameAbstractState playAt(int column) {
		boolean columnIsFull = game.playAt(playerPiece, column);
		if (!columnIsFull) // If board is full at that column
			return this;
		
		Player player = game.getPlayerFromEnum(playerPiece);
		player.incrementSpecialCounter();
		
		PlayerPiece winner = game.checkWinner();
		if (winner != null)
			return new GameFinished(game, winner);
		
		PlayerPiece nextPlayer = playerPiece.getOther();
		return new WaitingPlayerMove(game, nextPlayer);
	}
	
	@Override
	public GameAbstractState clearColumn(int column) {
		boolean success = game.clearColumn(playerPiece, column);
		if (success)
			return new WaitingPlayerMove(game, playerPiece.getOther());
		
		return this;
	}
	
	@Override
	public PlayerPiece getCurrentPlayer(){
		return playerPiece;
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.WaitingPlayerMove;
	}
}
