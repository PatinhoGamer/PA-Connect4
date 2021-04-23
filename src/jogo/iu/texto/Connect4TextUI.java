package jogo.iu.texto;

import jogo.logica.Connect4Logic;
import jogo.logica.Replayer;
import jogo.logica.dados.*;
import jogo.logica.estados.StateMachine;
import jogo.logica.estados.GameToStart;
import jogo.logica.minigames.TimedGame;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Connect4TextUI {
	
	private StateMachine stateMachine;
	private final Scanner scanner = new Scanner(System.in);
	private boolean exit = false;
	private boolean inGame = false;
	
	public Connect4TextUI() {
		this.stateMachine = new StateMachine(
				new GameToStart(new Connect4Logic()));
	}
	
	public void start() {
		while (!exit) {
			
			if (!inGame)
				menuState();
			
			else {
				System.out.println("\nGame State : " + stateMachine.getState());
				drawGameArea(stateMachine.getGameArea());
				switch (stateMachine.getState()) {
					case GameToStart -> gameToStart();
					case ComputerPlays -> computerPlays();
					case CheckPlayerWantsMiniGame -> checkPlayerWantsMiniGame();
					case PlayingMiniGame -> playingMiniGame();
					case GameFinished -> gameFinished();
					case WaitingPlayerMove -> waitingPlayerMove();
					default -> throw new IllegalStateException("Unexpected value: " + stateMachine.getState());
				}
			}
		}
	}
	
	
	// States ----------------------------------------------------------------------------------------------------------
	private void menuState() {
		System.out.println("Choose What you want to do");
		
		switch (UIUtils.chooseOption("Exit", "Start Game", "Watch Replay", "Load Saved Game")) {
			case 0 -> exit = true;
			case 1 -> {
				stateMachine = new StateMachine(new GameToStart(new Connect4Logic()));
				inGame = true;
			}
			case 2 -> {
				System.out.println("Choose Replay to Watch:");
				
				List<Replay> replays = GameSaver.getReplays();
				int index = UIUtils.chooseOption("Go Back", replays.toArray()) - 1;
				
				if (index == 0) break;
				
				Replayer replayerForGame = GameSaver.getReplayerForGame(index);
				if (replayerForGame == null) throw new IllegalStateException("replayerForGame == null");
				playReplay(replayerForGame);
			}
			case 3 -> {
				loadStateMachineFromFile();
				inGame = true;
			}
		}
	}
	
	private void playReplay(Replayer replayer) {
		while (true) {
			if (!replayer.moveToNextStep()) {
				System.out.println("Finished replay");
				break;
			}
			if (replayer.getLastMessage() != null)
				System.out.println(replayer.getLastMessage());
			else
				drawGameArea(replayer.getGameArea());
			scanner.nextLine();
			//TODO maybe player should change after rolling back and for that I need to choose how many rollbacks to happen at once
		}
	}
	
	private void checkPlayerWantsMiniGame() {
		System.out.println("Do you want to play the minigame? ");
		switch (UIUtils.chooseOption("Go Back To Main Menu", "Yes", "No")) {
			case 0 -> inGame = false;
			case 1 -> stateMachine.startMiniGame();
			case 2 -> stateMachine.ignoreOrEndMiniGame();
		}
	}
	
	private void computerPlays() {
		stateMachine.executePlay();
	}
	
	private void playingMiniGame() {
		Piece curPlayer = stateMachine.getCurrentPlayer();
		System.out.println("Current Player : " + getPlayerChar(curPlayer) + '\n' + stateMachine.getPlayerObj() + '\n');
		
		TimedGame miniGame = stateMachine.getMiniGame();
		System.out.println(miniGame.getGameObjective());
		
		miniGame.start();
		miniGame.generateQuestion();
		System.out.println("Available time : " + miniGame.availableTime() / 1000);
		while (true) {
			
			System.out.println("Question : " + miniGame.getQuestion());
			String answer = scanner.nextLine();
			
			if (miniGame.checkAnswer(answer))
				System.out.println("Well done, that is the right answer");
			else
				System.out.println("Wrong answer, try again");
			
			if (!miniGame.finishedAnswering())
				miniGame.generateQuestion();
			else
				break;
			if (miniGame.ranOutOfTime())
				break;
		}
		miniGame.stop();
		
		if (miniGame.playerManagedToDoIt())
			System.out.println("You finished it on time");
		else
			System.out.println("Took too long, better luck next time");
		
		stateMachine.ignoreOrEndMiniGame();
	}
	
	private void gameToStart() {
		System.out.println("\tPlayer1:");
		System.out.print("name -> ");
		
		String player1Name = UIUtils.getStringFromUser();
		int option1 = UIUtils.chooseOption(null, "Human", "Computer");
		Player player1 = new Player(player1Name, option1 == 1 ? PlayerType.HUMAN : PlayerType.COMPUTER);
		
		System.out.println("\tPlayer2:");
		System.out.print("name -> ");
		String player2Name;
		while (true) {
			player2Name = UIUtils.getStringFromUser();
			if (player1.getName().equals(player2Name))
				System.out.println("Name already in use by player 1");
			else
				break;
		}
		
		int option2 = UIUtils.chooseOption(null, "Human", "Computer");
		Player player2 = new Player(player2Name, option2 == 1 ? PlayerType.HUMAN : PlayerType.COMPUTER);
		
		stateMachine.startGameWithPlayers(player1, player2);
	}
	
	private void gameFinished() {
		Piece winner = stateMachine.getWinner();
		if (winner == null)
			System.out.println("Draw! No one wins this time");
		else
			System.out.println("Winner : " + getPlayerChar(winner) + '\n' + stateMachine.getPlayer(winner));
		
		switch (UIUtils.chooseOption("Exit", "Go Back to Menu")) {
			case 0 -> exit = true;
			case 1 -> {
				stateMachine = null;
				inGame = false;
			}
		}
	}
	
	private void waitingPlayerMove() {
		Piece curPlayer = stateMachine.getCurrentPlayer();
		System.out.println("Current Player : " + getPlayerChar(curPlayer) + "\n" + stateMachine.getPlayerObj());
		
		switch (UIUtils.chooseOption("Go Back To Main Menu", "Insert Piece", "Clear Column", "Rollback", "Save Current Game", "Load Saved Game")) {
			case 0 -> inGame = false;
			case 1 -> {
				System.out.print("Choose Column [1-7] : ");
				stateMachine.playAt(chooseColumn());
			}
			case 2 -> {
				if (stateMachine.getPlayerObj().getSpecialPieces() <= 0) {
					System.out.println("You don't have any special pieces");
					break;
				}
				System.out.print("Choose Column [1-7] : ");
				stateMachine.clearColumn(chooseColumn());
			}
			case 3 -> {
				if (stateMachine.getPlayerObj().getRollbacks() <= 0) {
					System.out.println("You don't have any rollbacks left");
					break;
				}
				if (stateMachine.getGame().getAvailableRollbacks() <= 0) {
					System.out.println("There are no available rollbacks");
					break;
				}
				stateMachine.rollback();
			}
			case 4 -> saveStateMachineToFile();
			case 5 -> loadStateMachineFromFile();
		}
	}
	// -----------------------------------------------------------------------------------------------------------------
	
	private void saveStateMachineToFile() {
		System.out.println("Filename : ");
		String filePath = scanner.nextLine();
		try {
			GameSaver.saveGameToFile(stateMachine, filePath);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void loadStateMachineFromFile() {
		System.out.println("Filename : ");
		String filePath = scanner.nextLine();
		try {
			stateMachine = GameSaver.loadGameFromFile(filePath);
		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private int chooseColumn() {
		return UIUtils.getChoice(1, Connect4Logic.WIDTH);
	}
	
	private void drawGameArea(Piece[][] gameArea) {
		
		for (int line = 0; line < gameArea.length; line++) {
			System.out.print("|");
			for (int column = 0; column < gameArea[0].length; column++) {
				if (gameArea[line][column] == null) System.out.print(" ");
				else if (gameArea[line][column] == Piece.PLAYER1) System.out.print("X");
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
	
	private char getPlayerChar(Piece playerType) {
		return playerType == Piece.PLAYER1 ? 'X' : 'O';
	}
}
