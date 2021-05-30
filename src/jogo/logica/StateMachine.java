package jogo.logica;

import jogo.logica.dados.dataViewers.GameDataViewer;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.dados.dataViewers.PlayerViewer;
import jogo.logica.estados.Connect4States;
import jogo.logica.estados.GameState;
import jogo.logica.estados.GameToStart;

import java.io.Serial;
import java.io.Serializable;

public class StateMachine implements Serializable {
	@Serial
	private static final long serialVersionUID = 0L;
	
	protected GameState currentState;
	
	public StateMachine(GameState currentState) {
		this.currentState = currentState;
	}
	
	public StateMachine() {
		this.currentState = new GameToStart();
	}
	
	public void playAt(int column) {
		setState(currentState.playAt(column));
	}
	
	public void clearColumn(int column) {
		setState(currentState.clearColumn(column));
	}
	
	public void executePlay() {
		setState(currentState.executePlay());
	}
	
	public void rollback(int amount) {
		setState(currentState.rollback(amount));
	}
	
	public void acceptMiniGame() {
		setState(currentState.acceptMiniGame());
	}
	
	public void ignoreMiniGame() {
		setState(currentState.ignoreMiniGame());
	}
	
	public void startGameWithPlayers(Player player1, Player player2) {
		setState(currentState.startGameWithPlayers(player1, player2));
	}
	
	public void answerMiniGame(String answer) {
		setState(currentState.answerMiniGame(answer));
	}
	
	public void startMiniGameTimer() {
		currentState.startMiniGameTimer();
	}
	
	public String getMiniGameQuestion() {
		return currentState.getMiniGameQuestion();
	}
	
	public String getMiniGameObjective() {
		return currentState.getMiniGameObjective();
	}
	
	public int getMiniGameAvailableTime() {
		return currentState.getMiniGameAvailableTime();
	}
	
	public boolean didPlayerWinMiniGame() {
		return currentState.didPlayerWinMiniGame();
	}
	
	public boolean isMiniGameFinished() {
		return currentState.isMiniGameFinished();
	}
	
	public boolean hasMiniGameStarted() {
		return currentState.hasMiniGameStarted();
	}
	
	public Piece getCurrentPlayer() {
		return currentState.getCurrentPlayerPiece();
	}
	
	public PlayerViewer getCurrentPlayerObj() {
		return currentState.getPlayer(currentState.getCurrentPlayerPiece());
	}
	
	public PlayerViewer getPlayer(Piece playerPiece) {
		return currentState.getPlayer(playerPiece);
	}
	
	public Piece getWinner() {
		return currentState.getWinner();
	}
	
	public Piece[][] getGameArea() {
		return currentState.getGameArea();
	}
	
	public GameDataViewer getGame() {
		return currentState.getGameViewer();
	}
	
	public Connect4States getState() {
		return currentState.getState();
	}
	
	protected void setState(GameState newState) {
		currentState = newState;
	}
	
	public GameState getUnderlyingGameState() {
		return currentState;
	}
	
	public boolean playerGotMiniGameQuestionAnswerRight() {
		return currentState.playerGotMiniGameQuestionAnswerRight();
	}
}
