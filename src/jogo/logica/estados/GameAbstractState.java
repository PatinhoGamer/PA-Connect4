package jogo.logica.estados;

import jogo.logica.GameDataObservable;
import jogo.logica.dados.*;
import jogo.logica.minigames.TimedMiniGame;

public abstract class GameAbstractState implements GameState {
	
	protected GameDataObservable game;
	
	public GameAbstractState(GameDataObservable game) {
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
	public GameDataViewer getGameViewer() {
		return new GameDataViewer(game);
	}
	
	private GameDataObservable getGame() {
		return game;
	}
	
	@Override
	public Piece[][] getGameArea() {
		return game.getGameArea();
	}
	
	@Override
	public Piece getWinner() {
		return null;
	}
	
	@Override
	public PlayerViewer getPlayer(Piece playerPiece) {
		return new PlayerViewer(game.getPlayerFromEnum(playerPiece));
	}
	
	@Override
	public PlayerViewer getCurrentPlayer() {
		return new PlayerViewer(game.getPlayerFromEnum(getCurrentPlayerPiece()));
	}
	
	@Override
	public Piece getCurrentPlayerPiece() {
		return game.getCurrentPlayerPiece();
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
	
	protected GameState stateAfterPlay() {
		GameFinished finishedState = checkFinishedState();
		if (finishedState != null) return finishedState;
		
		if (game.isPlayerBot())
			return new ComputerPlays(game);
		
		if (game.isMinigameAvailable()) {
			return new CheckPlayerWantsMiniGame(game);
		}
		return new WaitingPlayerMove(game);
	}
}
