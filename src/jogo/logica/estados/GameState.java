package jogo.logica.estados;

import jogo.logica.dados.dataViewers.GameDataViewer;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.dados.dataViewers.PlayerViewer;
import jogo.logica.minigames.ITimedMiniGame;

import java.io.Serializable;

public interface GameState extends Serializable {
	
	GameState playAt(int column);
	
	GameState clearColumn(int column);
	
	GameState acceptMiniGame();
	
	GameState ignoreMiniGame();
	
	GameState executePlay();
	
	GameState rollback(int amount);
	
	GameState startGameWithPlayers(Player player1, Player player2);
	
	// MiniGame Related --------------------
	GameState answerMiniGame(String answer);
	
	void startMiniGameTimer();
	
	String getMiniGameQuestion();
	
	String getMiniGameObjective();
	
	int getMiniGameAvailableTime();
	// -------------------------------------
	
	boolean didPlayerWinMiniGame();
	
	GameDataViewer getGameViewer();
	
	Piece[][] getGameArea();
	
	Piece getWinner();
	
	PlayerViewer getPlayer(Piece playerPiece);
	
	PlayerViewer getCurrentPlayer();
	
	Piece getCurrentPlayerPiece();
	
	Connect4States getState();
	
	boolean isMiniGameFinished();
	
	boolean hasMiniGameStarted();
	
	boolean playerGotMiniGameQuestionAnswerRight();
}
