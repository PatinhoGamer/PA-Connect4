package jogo.logica;

import jogo.logica.dados.Player;
import jogo.logica.dados.PlayerPiece;

import java.awt.*;

public class Connect4Logic {
	
	public static final int HEIGHT = 6;
	public static final int WIDTH = 7;
	public static final int AMOUNT_TO_WIN = 4;
	
	private final PlayerPiece[][] gameArea;
	
	private Player player1;
	private Player player2;
	
	public Connect4Logic() {
		gameArea = new PlayerPiece[HEIGHT][WIDTH];
	}
	
	public void setPlayers(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public PlayerPiece checkWinner() {
		for (int line = 0; line < HEIGHT; line++) {
			for (int column = 0; column < WIDTH; column++) {
				
				PlayerPiece currentPlacePiece = gameArea[line][column];
				if (currentPlacePiece == null)
					continue;
				
				if (checkIfPlayerWonAt(currentPlacePiece, line, column))
					return currentPlacePiece;
			}
		}
		return null;
	}
	
	private boolean checkIfPlayerWonAt(PlayerPiece player, int line, int column) {
		Point[] directionsAround = new Point[]{
				new Point(-1, -1), new Point(0, -1), new Point(1, -1),
				new Point(-1, 0), new Point(1, 0),
				new Point(-1, 1), new Point(0, 1), new Point(1, 1)};
		
		for (int direction = 0; direction < directionsAround.length; direction++) {
			Point offset = directionsAround[direction];
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
				}
			}
		}
		return false;
	}
	
	public boolean clearColumn(PlayerPiece playerPiece, int column) {
		Player player = getPlayerFromEnum(playerPiece);
		if (player.getSpecialPieces() > 0) {
			player.setSpecialPieces(player.getSpecialPieces() - 1);
			
			for (int i = 0; i < HEIGHT; i++)
				gameArea[i][column] = null;
			return true;
		}
		return false;
	}
	
	public Player getPlayerFromEnum(PlayerPiece playerPiece) {
		return playerPiece == PlayerPiece.PLAYER1 ? player1 : player2;
	}
	
	public boolean playAt(PlayerPiece player, int column) {
		column = column - 1;
		for (int height = HEIGHT - 1; height >= 0; height--) {
			if (gameArea[height][column] == null) {
				gameArea[height][column] = player;
				return true;
			}
		}
		return false;
	}
	
	public PlayerPiece[][] getGameArea() {
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
}
