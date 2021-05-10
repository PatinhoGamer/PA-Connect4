package jogo.logica.estados;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.dados.PlayerType;
import jogo.logica.minigames.TimedMiniGame;

public abstract class GameAbstractState implements GameState {
	
	protected Connect4Logic game;
	
	public GameAbstractState(Connect4Logic game) {
		this.game = game;
	}
	
	@Override
	public GameState playAt(int column) {
		return this;
	}
	
	@Override
	public GameState clearColumn(int column) {
		return this;
	}
	
	@Override
	public GameState startMiniGame() {
		return this;
	}
	
	@Override
	public GameState ignoreMiniGame() {
		return this;
	}
	
	@Override
	public GameState endMiniGame() {
		return this;
	}
	
	@Override
	public GameState executePlay() {
		return this;
	}
	
	@Override
	public GameState rollback(int amount) {
		return this;
	}
	
	@Override
	public GameState startGameWithPlayers(Player player1, Player player2) {
		return this;
	}
	
	@Override
	public GameState restartGame() {
		return this;
	}
	
	@Override
	public Connect4Logic getGame() {
		return game;
	}
	
	@Override
	public Piece getWinner() {
		return null;
	}
	
	@Override
	public Player getPlayer(Piece playerPiece) {
		return game.getPlayerFromEnum(playerPiece);
	}
	
	@Override
	public Piece getCurrentPlayer() {
		return null;
	}
	
	@Override
	public TimedMiniGame getMiniGame() {
		return null;
	}
	
	protected GameFinished checkFinishedState() {
		Piece winner = game.checkWinner();
		if (winner != null)
			return new GameFinished(game, winner);
		if (game.isFull())
			return new GameFinished(game, null);
		return null;
	}
	
	protected GameState stateAfterPlay(Piece playerPiece) {
		GameFinished finishedState = checkFinishedState();
		if (finishedState != null) return finishedState;
		
		Piece nextPlayer = playerPiece.getOther();
		
		Player trulyNextPlayer = getGame().getPlayerFromEnum(nextPlayer);
		if (trulyNextPlayer.getType() == PlayerType.COMPUTER)
			return new ComputerPlays(game, nextPlayer);
		
		if (trulyNextPlayer.getMiniGameCounter() >= Connect4Logic.ROUNDS_TO_PLAY_MINIGAME) {
			trulyNextPlayer.resetSpecialCounter();
			return new CheckPlayerWantsMiniGame(game, nextPlayer);
		}
		return new WaitingPlayerMove(game, nextPlayer);
	}
}
