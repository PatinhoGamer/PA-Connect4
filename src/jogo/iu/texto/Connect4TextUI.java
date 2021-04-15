package jogo.iu.texto;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.PlayerPiece;
import jogo.logica.estados.PlayingMiniGame;
import jogo.logica.estados.StateMachine;
import jogo.logica.dados.Player;
import jogo.logica.estados.GameToStart;

import java.util.Scanner;

public class Connect4TextUI {
	
	private final StateMachine stateMachine;
	private final Scanner scanner = new Scanner(System.in);
	private boolean exit = false;
	
	public Connect4TextUI(Connect4Logic gameLogic) {
		this.stateMachine = new StateMachine(
				new GameToStart(gameLogic));
	}
	
	public void start() {
		while (!exit) {
			drawGameArea();
			System.out.println("Game State : " + stateMachine.getState());
			
			switch (stateMachine.getState()) {
				case GameToStart -> {
					gameToStart();
				}
				case PlayingMiniGame -> {
					playingMiniGame();
				}
				case GameFinished -> {
					gameFinished();
				}
				case WaitingPlayerMove -> {
					waitingPlayerMove();
				}
			}
		}
	}
	
	private void playingMiniGame() {
	
	}
	
	private void gameToStart() {
		System.out.println("\tPlayer1:");
		System.out.print("name -> ");
		Player player1 = new Player(scanner.nextLine());
		int option = UIUtils.chooseOption(null, "Human", "Computer");
		
		System.out.println("\tPlayer2:");
		System.out.print("name -> ");
		String player2Name = null;
		while (true) {
			player2Name = scanner.nextLine();
			if (player1.getName().equals(player2Name))
				System.out.println("Name already in use by player 1");
			else
				break;
		}
		
		Player player2 = new Player(player2Name);
		int option2 = UIUtils.chooseOption(null, "Human", "Computer");
		
		stateMachine.setPlayers(player1, player2);
	}
	
	private void gameFinished() {
		PlayerPiece curPlayer = stateMachine.getWinner();
		System.out.println("Winner : " + stateMachine.getPlayer(curPlayer));
		char playerType = curPlayer == PlayerPiece.PLAYER1 ? 'X' : 'O';
		
		switch (UIUtils.chooseOption("Exit", "Restart")) {
			case 0 -> exit = true;
			case 1 -> {
				//TODO Make the restart thing
			}
		}
	}
	
	private void waitingPlayerMove() {
		PlayerPiece curPlayer = stateMachine.getCurrentPlayer();
		char playerType = curPlayer == PlayerPiece.PLAYER1 ? 'X' : 'O';
		System.out.println("Current Player : " + playerType + "\n\t" + stateMachine.getPlayer(curPlayer));
		
		switch (UIUtils.chooseOption("Exit", "Insert Piece", "Clear Column")) {
			case 0 -> exit = true;
			case 1 -> {
				System.out.print("Choose Column [1-7] : ");
				stateMachine.playAt(chooseColumn());
			}
			case 2 -> {
				System.out.print("Choose Column [1-7] : ");
				stateMachine.clearColumn(chooseColumn());
			}
		}
	}
	
	private int chooseColumn() {
		return UIUtils.getChoice(1, Connect4Logic.WIDTH);
	}
	
	private void drawGameArea() {
		PlayerPiece[][] gameArea = stateMachine.getGameArea();
		
		for (int line = 0; line < gameArea.length; line++) {
			System.out.print("|");
			for (int column = 0; column < gameArea[0].length; column++) {
				if (gameArea[line][column] == null) System.out.print(" ");
				else if (gameArea[line][column] == PlayerPiece.PLAYER1) System.out.print("X");
				else System.out.print("O");
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
