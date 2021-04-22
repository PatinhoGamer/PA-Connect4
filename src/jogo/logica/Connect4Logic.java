package jogo.logica;

import jogo.logica.dados.Player;
import jogo.logica.dados.Piece;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Connect4Logic implements Serializable {
	
	public static final int HEIGHT = 6;
	public static final int WIDTH = 7;
	public static final int AMOUNT_TO_WIN = 4;
	public static final int MAX_ROLLBACK = 5;
	public static final int ROUNDS_TO_PLAY_MINIGAME = 100;
	
	private Piece[][] gameArea = new Piece[HEIGHT][WIDTH];
	private final FixedSizeStack<Piece[][]> lastPlays = new FixedSizeStack<>(MAX_ROLLBACK * 2);
	private int round = 1;
	
	private Player player1;
	private Player player2;
	
	private final List<String> gameActions = new ArrayList<>();
	
	public boolean setPlayers(Player player1, Player player2) {
		if (player1.getName().equals(player2.getName())) return false;
		this.player1 = player1;
		this.player2 = player2;
		gameActions.add("Player1:" + player1.getType() + ' ' + player1.getName());
		gameActions.add("Player2:" + player2.getType() + ' ' + player2.getName());
		return true;
	}
	
	public Piece checkWinner() {
		for (int line = 0; line < HEIGHT; line++) {
			for (int column = 0; column < WIDTH; column++) {
				
				Piece currentPlacePiece = gameArea[line][column];
				if (currentPlacePiece == null)
					continue;
				
				if (checkIfPlayerWonAt(currentPlacePiece, line, column))
					return currentPlacePiece;
			}
		}
		return null;
	}
	
	public boolean rollback(Piece playerPiece) {
		Player player = getPlayerFromEnum(playerPiece);
		if (player.getRollbacks() <= 0 || lastPlays.size() == 0)
			return false;
		
		round--;
		gameArea = lastPlays.pop();
		
		player.setRollbacks(player.getRollbacks() - 1);
		player.resetSpecialCounter();
		
		gameActions.add("Rollback:" + playerPiece);
		return true;
	}
	
	private boolean checkIfPlayerWonAt(Piece player, int line, int column) {
		Point[] directionsAround = new Point[]{
				new Point(-1, -1), new Point(0, -1), new Point(1, -1),
				new Point(-1, 0),/*       Center      */  new Point(1, 0),
				new Point(-1, 1), new Point(0, 1), new Point(1, 1)};
		
		for (Point offset : directionsAround) {
			int amount = 1;
			
			for (int i = 1; i <= AMOUNT_TO_WIN; i++) {
				
				int offX = column + offset.x * i;
				int offY = line + offset.y * i;
				
				if (offX < 0 || offX >= WIDTH || offY < 0 || offY >= HEIGHT)
					break;
				
				if (gameArea[offY][offX] == player) {
					amount++;
					if (amount == AMOUNT_TO_WIN)
						return true;
				} else
					break;
			}
		}
		return false;
	}
	
	public boolean clearColumn(Piece playerPiece, int column) {
		column = column - 1;
		Player player = getPlayerFromEnum(playerPiece);
		if (player.getSpecialPieces() > 0) {
			player.useSpecialPiece();
			
			lastPlays.push(createGameAreaCopy());
			
			for (int i = 0; i < HEIGHT; i++)
				gameArea[i][column] = null;
			round++;
			
			gameActions.add("clearColumn:" + playerPiece + ' ' + (column + 1));
			return true;
		}
		return false;
	}
	
	public Player getPlayerFromEnum(Piece playerPiece) {
		return playerPiece == Piece.PLAYER1 ? player1 : player2;
	}
	
	public boolean playAt(Piece playerPiece, int column) {
		column = column - 1;
		if (gameArea[0][column] != null)
			return false;
		
		lastPlays.push(createGameAreaCopy());
		
		for (int line = HEIGHT - 1; line >= 0; line--) {
			if (gameArea[line][column] == null) {
				gameArea[line][column] = playerPiece;
				round++;
				
				gameActions.add("playAt:" + playerPiece + ' ' + (column + 1));
				return true;
			}
		}
		return false;
	}
	
	private Piece[][] createGameAreaCopy() {
		Piece[][] copy = new Piece[gameArea.length][gameArea[0].length];
		for (int i = 0; i < copy.length; i++)
			copy[i] = Arrays.copyOf(gameArea[i], gameArea[i].length);
		return copy;
	}
	
	public List<String> getGameActions() {
		return gameActions;
	}
	
	public Piece[][] getGameArea() {
		return gameArea;
	}
	
	private void showWhere(int y, int x) {
		for (int line = 0; line < gameArea.length; line++) {
			System.out.print("|");
			for (int column = 0; column < gameArea[0].length; column++) {
				if (line == y && column == x) System.out.print("X");
				else System.out.print(" ");
				System.out.print("|");
			}
			System.out.println();
		}
		for (int line = 0; line < gameArea.length * 2 + 3; line++) {
			System.out.print("-");
		}
		System.out.println();
	}
	
	public boolean isFull() {
		for (int column = 0; column < WIDTH; column++)
			if (gameArea[0][column] == null)
				return false;
		return true;
	}
	
	public int getRound() {
		return round;
	}
	
	public int getAvailableRollbacks() {
		return lastPlays.size();
	}
}
