package jogo.iu.texto;

import jogo.logica.GameData;
import jogo.logica.dados.observables.GameDataObservable;
import jogo.logica.GameSaver;
import jogo.logica.Replayer;
import jogo.logica.dados.Piece;
import jogo.logica.dados.Player;
import jogo.logica.dados.PlayerType;
import jogo.logica.dados.Replay;
import jogo.logica.estados.GameToStart;
import jogo.logica.StateMachine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Connect4TextUI {
	
	private StateMachine stateMachine;
	private final Scanner scanner = new Scanner(System.in);
	private boolean exit = false;
	
	public void start() {
		while (!exit) {
			if (!inGame())
				menuState();
			
			else {
				System.out.println();
				System.out.println("Game State : " + stateMachine.getState());
				
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
	
	private void menuState() {
		System.out.println("Choose What you want to do");
		
		switch (UIUtils.chooseOption("Exit", "Start Game", "Watch Replay", "Load Saved Game")) {
			case 0 -> exit = true;
			case 1 -> initializeGame();
			case 2 -> {
				System.out.println("Choose Replay to Watch:");
				
				List<Replay> replays = GameSaver.getReplays();
				List<String> replayString = new ArrayList<>(replays.size());
				for (Replay replay : replays)
					replayString.add(replay.getDate() + " -> W: " + replay.getWinner() + " vs L: " + replay.getLoser());
				
				int index = UIUtils.chooseOption("Go Back", replayString.toArray()) - 1;
				
				if (index == -1) break;
				
				Replayer replayerForGame = GameSaver.getReplayerForGame(index);
				if (replayerForGame == null) throw new IllegalStateException("replayerForGame == null");
				playReplay(replayerForGame);
			}
			case 3 -> loadStateMachineFromFile();
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
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Game States -----------------------------------------------------------------------------------------------------
	private void checkPlayerWantsMiniGame() {
		printCurrentPlayer();
		System.out.println("Do you want to play the minigame? ");
		switch (UIUtils.chooseOption("Go Back To Main Menu", "Yes", "No")) {
			case 0 -> stateMachine = null;
			case 1 -> stateMachine.acceptMiniGame();
			case 2 -> stateMachine.ignoreMiniGame();
		}
	}
	
	private void computerPlays() {
		printCurrentPlayer();
		stateMachine.executePlay();
	}
	
	private void playingMiniGame() {
		
		if (!stateMachine.hasMiniGameStarted()) {
			System.out.println("Objective : " + stateMachine.getMiniGameObjective());
			System.out.println("Available time : " + stateMachine.getMiniGameAvailableTime());
			stateMachine.startMiniGameTimer();
		}
		
		System.out.println("Question : " + stateMachine.getMiniGameQuestion());
		String answer = scanner.nextLine();
		stateMachine.answerMiniGame(answer);
		
		if (stateMachine.playerGotMiniGameQuestionAnswerRight())
			System.out.println("That is the right answer");
		else
			System.out.println("Wrong answer");
		
		if (stateMachine.isMiniGameFinished())
			if (stateMachine.didPlayerWinMiniGame())
				System.out.println("You finished it on time");
			else
				System.out.println("Took too long, better luck next time");
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
		drawGameArea(stateMachine.getGameArea());
		
		Piece winner = stateMachine.getWinner();
		if (winner == null)
			System.out.println("Draw! No one wins this time");
		else
			System.out.println("Winner : " + getPlayerChar(winner) + '\n' + stateMachine.getPlayer(winner));
		
		switch (UIUtils.chooseOption("Exit", "Go Back to Menu")) {
			case 0 -> exit = true;
			case 1 -> stateMachine = null;
		}
	}
	
	private void waitingPlayerMove() {
		drawGameArea(stateMachine.getGameArea());
		printCurrentPlayer();
		
		switch (UIUtils.chooseOption("Go Back To Main Menu", "Insert Piece", "Clear Column", "Rollback", "Save Current Game", "Load Saved Game")) {
			case 0 -> stateMachine = null;
			case 1 -> {
				System.out.print("Choose Column [1-7] : ");
				stateMachine.playAt(chooseColumn());
			}
			case 2 -> {
				if (stateMachine.getCurrentPlayerObj().getSpecialPieces() <= 0) {
					System.out.println("You don't have any special pieces");
					break;
				}
				System.out.print("Choose Column [1-7] : ");
				stateMachine.clearColumn(chooseColumn());
			}
			case 3 -> {
				if (stateMachine.getCurrentPlayerObj().getRollbacks() <= 0) {
					System.out.println("You don't have any rollbacks left");
					break;
				}
				if (stateMachine.getGame().getAvailableRollbacks() <= 0) {
					System.out.println("There are no available rollbacks");
					break;
				}
				
				System.out.println("How many times to rollback");
				int amount = UIUtils.getChoice(0, 5);
				if (amount == 0) {
					System.out.println("Rollback aborted");
					break;
				}
				if (stateMachine.getCurrentPlayerObj().getRollbacks() < amount || stateMachine.getGame().getAvailableRollbacks() < amount) {
					System.out.println("Not enough rollback credits or previous states");
					break;
				}
				stateMachine.rollback(amount);
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
			GameSaver.saveCurrentStateToFile(stateMachine.getUnderlyingGameState(), filePath + GameSaver.fileExtensionSave);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void loadStateMachineFromFile() {
		System.out.println("Filename : ");
		String filePath = scanner.nextLine();
		try {
			stateMachine = new StateMachine(GameSaver.loadGameFromFile(filePath + GameSaver.fileExtensionSave));
		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private int chooseColumn() {
		return UIUtils.getChoice(1, GameData.WIDTH);
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
	
	private boolean inGame() {
		return stateMachine != null;
	}
	
	private void initializeGame() {
		this.stateMachine = new StateMachine(new GameToStart());
	}
	
	private void printCurrentPlayer() {
		var player = stateMachine.getCurrentPlayerObj();
		System.out.println("Current Player : " + player.getName());
		if (player.getType() == PlayerType.HUMAN) {
			System.out.println("\t Rollbacks : " + player.getRollbacks());
			System.out.println("\t Special Pieces : " + player.getSpecialPieces());
			System.out.println("\t Minigame Counter : " + (GameData.ROUNDS_TO_PLAY_MINIGAME - player.getMiniGameCounter()));
		}
	}
	
}
