package jogo.logica.estados.connect4;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.dados.PlayerType;
import jogo.logica.minigames.TimedGame;

import java.io.Serializable;

public abstract class GameAbstractState implements Serializable {
	
	protected Connect4Logic game;
	
	public GameAbstractState(Connect4Logic game) {
		this.game = game;
	}
	
	public GameAbstractState playAt(int column) {
		return this;
	}
	
	public GameAbstractState clearColumn(int column) {
		return this;
	}
	
	public GameAbstractState startMiniGame() {
		return this;
	}
	
	public GameAbstractState ignoreOrEndMiniGame() {
		return this;
	}
	
	public GameAbstractState executePlay() {
		return this;
	}
	
	public GameAbstractState rollback() {
		return this;
	}
	
	public GameAbstractState setPlayers(Player player1, Player player2) {
		return this;
	}
	
	public GameAbstractState restartGame() {
		return this;
	}
	
	public Connect4Logic getGame() {
		return game;
	}
	
	public Piece getWinner() {
		return null;
	}
	
	public Player getPlayer(Piece playerPiece) {
		return game.getPlayerFromEnum(playerPiece);
	}
	
	public Piece getCurrentPlayer() {
		return null;
	}
	
	public TimedGame getMiniGame() {
		return null;
	}
	
	public abstract Connect4States getState();
	
	protected GameFinished checkFinishedState() {
		Piece winner = game.checkWinner();
		if (winner != null)
			return new GameFinished(game, winner);
		if (game.isFull())
			return new GameFinished(game, null);
		return null;
	}
	
	protected GameAbstractState stateAfterPlay(Piece playerPiece) {
		GameFinished finishedState = checkFinishedState();
		if (finishedState != null) return finishedState;
		
		Piece nextPlayer = playerPiece.getOther();
		Player trulyNextPlayer = getGame().getPlayerFromEnum(nextPlayer);
		if (trulyNextPlayer.getType() == PlayerType.COMPUTER)
			return new ComputerPlays(game, nextPlayer);
		
		if (trulyNextPlayer.getSpecialPiecesCounter() == Connect4Logic.ROUNDS_TO_PLAY_MINIGAME) {
			trulyNextPlayer.resetSpecialCounter();
			return new CheckPlayerWantsMiniGame(game, nextPlayer);
		}
		return new WaitingPlayerMove(game, nextPlayer);
	}
}
