package jogo.logica.estados;

import jogo.logica.GameDataObservable;
import jogo.logica.dados.GameDataViewer;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.dados.PlayerViewer;
import jogo.logica.minigames.TimedMiniGame;

import java.io.Serializable;

public interface GameState extends Serializable {
	
	GameState playAt(int column);
	
	GameState clearColumn(int column);
	
	GameState startMiniGame();
	
	GameState ignoreMiniGame();
	
	GameState endMiniGame();
	
	GameState executePlay();
	
	GameState rollback(int amount);
	
	GameState startGameWithPlayers(Player player1, Player player2);
	
	GameState restartGame();
	
	GameDataViewer getGameViewer();
	
	Piece[][] getGameArea();
	
	Piece getWinner();
	
	PlayerViewer getPlayer(Piece playerPiece);
	
    PlayerViewer getCurrentPlayer();
	
	Piece getCurrentPlayerPiece();
	
	TimedMiniGame getMiniGame();
	
	Connect4States getState();
}
