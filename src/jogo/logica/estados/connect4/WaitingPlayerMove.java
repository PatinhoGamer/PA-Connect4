package jogo.logica.estados.connect4;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.PlayerPiece;
import jogo.logica.dados.Player;
import jogo.logica.estados.minigames.MathMiniGame;
import jogo.logica.estados.minigames.TimedGame;
import jogo.logica.estados.minigames.WordsMiniGame;

public class WaitingPlayerMove extends GameAbstractState {
	
	private final PlayerPiece playerPiece;
	
	public WaitingPlayerMove(Connect4Logic game, PlayerPiece player) {
		super(game);
		this.playerPiece = player;
	}
	
	@Override
	public GameAbstractState playAt(int column) {
		boolean columnHasntFull = game.playAt(playerPiece, column);
		if (!columnHasntFull)
			return this;
		
		Player player = game.getPlayerFromEnum(playerPiece);
		player.incrementSpecialCounter();
		
		PlayerPiece winner = game.checkWinner();
		if (winner != null)
			return new GameFinished(game, winner);
		
		PlayerPiece nextPlayer = playerPiece.getOther();
		Player trulyPlayer = getGame().getPlayerFromEnum(nextPlayer);
		if (trulyPlayer.getSpecialPiecesCounter() == 4) {
			trulyPlayer.resetSpecialCounter();
			int nextActivity = trulyPlayer.getNextActivity();
			TimedGame miniGame;
			if (nextActivity == 0) miniGame = new MathMiniGame();
			else miniGame = new WordsMiniGame();
			return new PlayingMiniGame(game, nextPlayer, miniGame);
		}
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
	public PlayerPiece getCurrentPlayer() {
		return playerPiece;
	}
	
	@Override
	public Connect4States getState() {
		return Connect4States.WaitingPlayerMove;
	}
}
