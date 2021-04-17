package jogo.iu.texto;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.PlayerPiece;
import jogo.logica.dados.PlayerType;
import jogo.logica.estados.StateMachine;
import jogo.logica.dados.Player;
import jogo.logica.estados.connect4.GameToStart;
import jogo.logica.estados.minigames.TimedGame;

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
		
		PlayerPiece curPlayer = stateMachine.getCurrentPlayer();
		
		System.out.println("Current Player : " + getPlayerChar(curPlayer) + "\n\t" + stateMachine.getPlayer(curPlayer));
		
		TimedGame miniGame = stateMachine.getMiniGame();
		
		while (!miniGame.isFinished()) {
			
			System.out.println("Answer this question : " + miniGame.getQuestion());
			
			String answer = scanner.nextLine();
			
			while (!miniGame.checkAnswer(answer))
				System.out.println("Wrong answer, try again");
			
			System.out.println("Well done, that is the right answer");
		}
	}
	
	private void gameToStart() {
		System.out.println("\tPlayer1:");
		System.out.print("name -> ");
		String player1Name = scanner.nextLine();
		int option1 = UIUtils.chooseOption(null, "Human", "Computer");
		Player player1 = new Player(player1Name, option1 == 1 ? PlayerType.HUMAN : PlayerType.COMPUTER);
		
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
		
		int option2 = UIUtils.chooseOption(null, "Human", "Computer");
		Player player2 = new Player(player1Name, option2 == 1 ? PlayerType.HUMAN : PlayerType.COMPUTER);
		
		stateMachine.setPlayers(player1, player2);
	}
	
	private void gameFinished() {
		PlayerPiece curPlayer = stateMachine.getWinner();
		System.out.println("Winner : " + stateMachine.getPlayer(curPlayer));
		
		
		switch (UIUtils.chooseOption("Exit", "Restart")) {
			case 0 -> exit = true;
			case 1 -> {
				//TODO Make the restart thing
			}
		}
	}
	
	private void waitingPlayerMove() {
		PlayerPiece curPlayer = stateMachine.getCurrentPlayer();
		System.out.println("Current Player : " + getPlayerChar(curPlayer) + "\n\t" + stateMachine.getPlayer(curPlayer));
		
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
	
	private char getPlayerChar(PlayerPiece playerType) {
		return playerType == PlayerPiece.PLAYER1 ? 'X' : 'O';
	}
}
