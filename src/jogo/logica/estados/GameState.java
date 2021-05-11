package jogo.logica.estados;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
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
	
	Connect4Logic getGame();
	
	Piece getWinner();
	
	Player getPlayer(Piece playerPiece);
	
	Piece getCurrentPlayer();
	
	TimedMiniGame getMiniGame();
	
	Connect4States getState();
}
