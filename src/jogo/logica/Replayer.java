package jogo.logica;

import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.dados.PlayerType;
import jogo.logica.dados.Replay;

import java.util.List;

public class Replayer {
	
	private final Replay replay;
	private final List<String> gameActions;
	private int currentActionIndex = 0;
	
	private final Connect4Logic game;
	
	private String lastMessage = null;
	
	public Replayer(Replay replay) {
		this.replay = replay;
		gameActions = replay.getGameActions();
		game = new Connect4Logic();
	}
	
	public boolean moveToNextStep() {
		
		if (currentActionIndex >= replay.getGameActions().size())
			return false;
		
		String gameLine = gameActions.get(currentActionIndex);
		
		String[] parts = gameLine.split(String.valueOf(Connect4Logic.ACTION_DELIMITER));
		
		String secondPart = null;
		if (parts.length > 1)
			secondPart = parts[1];
		
		lastMessage = null;
		
		switch (parts[0]) {
			case Connect4Logic.ACTION_SET_PLAYERS -> {
				String[] playerStrings = secondPart.split(",");
				Player[] players = new Player[2];
				
				for (int i = 0; i < playerStrings.length; i++) {
					String[] s = playerStrings[i].split(" ");
					
					PlayerType type = s[0].equals(PlayerType.HUMAN.toString()) ? PlayerType.HUMAN : PlayerType.COMPUTER;
					String name = s[1];
					players[i] = new Player(name, type);
				}
				game.setPlayers(players[0], players[1]);
			}
			case Connect4Logic.ACTION_PLAY_AT -> {
				String[] s = secondPart.split(" ");
				Piece piece = stringToPiece(s[0]);
				int column = Integer.parseInt(s[1]);
				game.playAt(piece, column);
			}
			case Connect4Logic.ACTION_CLEAR_COLUMN -> {
				String[] s = secondPart.split(" ");
				Piece piece = stringToPiece(s[0]);
				int column = Integer.parseInt(s[1]);
				game.clearColumn(piece, column);
			}
			case Connect4Logic.ACTION_ROLLBACK -> {
				String[] s = secondPart.split(" ");
				Piece piece = stringToPiece(s[0]);
				int amount = Integer.parseInt(s[1]);
				game.rollback(piece, amount);
				lastMessage = "Rollback from " + piece;
			}
			case Connect4Logic.ACTION_MINIGAME_IGNORED -> {
				Piece piece = stringToPiece(secondPart);
				game.playerIgnoredMiniGame(piece);
				lastMessage = piece + " ignored minigame";
			}
			case Connect4Logic.ACTION_MINIGAME_WON -> {
				Piece piece = stringToPiece(secondPart);
				game.playerWonMiniGame(piece);
				lastMessage = piece + " won minigame";
			}
			case Connect4Logic.ACTION_MINIGAME_LOST -> {
				Piece piece = stringToPiece(secondPart);
				game.playerLostMiniGame(piece);
				lastMessage = piece + " lost minigame";
			}
			case Connect4Logic.ACTION_FINISHED -> lastMessage =  stringToPiece(secondPart) + " won the game";
		}
		
		currentActionIndex++;
		return true;
	}
	
	public String getLastMessage() {
		return lastMessage;
	}
	
	private Piece stringToPiece(String s) {
		return s.equals(Piece.PLAYER1.toString()) ? Piece.PLAYER1 : Piece.PLAYER2;
	}
	
	public Piece[][] getGameArea() {
		return game.getGameArea();
	}
}
