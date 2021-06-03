package jogo.logica;

import jogo.logica.dados.Player;
import jogo.logica.dados.Piece;
import jogo.logica.dados.PlayerType;
import jogo.logica.minigames.ITimedMiniGame;
import jogo.logica.minigames.MathMiniGame;
import jogo.logica.minigames.WordsMiniGame;

import java.awt.Point;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class GameData implements Serializable {
	@Serial
	private static final long serialVersionUID = 0L;
	
	public static final int HEIGHT = 6; // 6
	public static final int WIDTH = 7; // 7
	public static final int AMOUNT_TO_WIN = 4; // 4
	public static final int MAX_ROLLBACK = 5;
	public static final int ROUNDS_TO_PLAY_MINIGAME = 4;
	
	public static final char ACTION_DELIMITER = ':';
	public static final String ACTION_ROLLBACK = "Rollback";
	public static final String ACTION_CLEAR_COLUMN = "ClearColumn";
	public static final String ACTION_PLAY_AT = "PlayAt";
	public static final String ACTION_SET_PLAYERS = "SetPlayers";
	public static final String ACTION_MINIGAME_IGNORED = "MiniGameIgnored";
	public static final String ACTION_MINIGAME_WON = "MiniGameWon";
	public static final String ACTION_MINIGAME_LOST = "MiniGameLost";
	public static final String ACTION_FINISHED = "Finished";
	
	private Piece[][] gameArea = new Piece[HEIGHT][WIDTH];
	private final FixedSizeStack<Piece[][]> lastPlays = new FixedSizeStack<>(MAX_ROLLBACK * 2);
	private int round = 1; // Not used for anything
	
	private Player player1;
	private Player player2;
	private Piece currentPlayer;
	private Piece winner;
	
	private ITimedMiniGame miniGame;
	private boolean wonMiniGame = false;
	private boolean gotMiniGameAnswerRight = false;
	
	private final List<String> gameActions = new ArrayList<>();
	
	// State Methods ---------------------------------------------------------------------------------------------------
	public boolean setPlayers(Player player1, Player player2) {
		if (player1.getName().equals(player2.getName())) return false;
		this.player1 = player1;
		this.player2 = player2;
		gameActions.add(ACTION_SET_PLAYERS + ACTION_DELIMITER +
				player1.getType() + ' ' + player1.getName() + ',' + player2.getType() + ' ' + player2.getName());
		
		currentPlayer = Piece.PLAYER1;
		if (Math.random() >= 0.5)
			currentPlayer = currentPlayer.getOther();
		
		return true;
	}
	
	public boolean playAt(int column) {
		boolean success = playAt(getCurrentPlayerPiece(), column);
		if (success)
			setNextPlayer();
		return success;
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
				
				gameActions.add(ACTION_PLAY_AT + ACTION_DELIMITER + playerPiece + ' ' + (column + 1));
				getPlayerFromEnum(playerPiece).incrementMiniGameCounter();
				return true;
			}
		}
		return false;
	}
	
	public boolean clearColumn(int column) {
		boolean success = clearColumn(getCurrentPlayerPiece(), column);
		if (success) {
			System.out.println("next shitass");
			setNextPlayer();
		}
		return success;
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
			
			gameActions.add(ACTION_CLEAR_COLUMN + ACTION_DELIMITER + playerPiece + ' ' + (column + 1));
			getPlayerFromEnum(playerPiece).incrementMiniGameCounter();
			return true;
		}
		return false;
	}
	
	public boolean rollback(int amount) {
		boolean success = rollback(getCurrentPlayerPiece(), amount);
		if (success)
			setNextPlayer();
		return success;
	}
	
	public boolean rollback(Piece playerPiece, int amount) {
		Player player = getPlayerFromEnum(playerPiece);
		if (amount <= 0 || player.getRollbacks() < amount || lastPlays.size() < amount)
			return false;
		
		for (int i = 0; i < amount; i++) {
			round--;
			gameArea = lastPlays.pop();
		}
		
		player.setRollbacks(player.getRollbacks() - amount);
		player.resetSpecialCounter();
		
		gameActions.add(ACTION_ROLLBACK + ACTION_DELIMITER + playerPiece + ' ' + amount);
		return true;
	}
	// -----------------------------------------------------------------------------------------------------------------
	
	// Minigame Related ------------------------------------------------------------------------------------------------
	public boolean isMiniGameAvailable() {
		return getCurrentPlayer().getMiniGameCounter() >= GameData.ROUNDS_TO_PLAY_MINIGAME;
	}
	
	public void playerWonMiniGame() {
		playerWonMiniGame(getCurrentPlayerPiece());
	}
	
	public void playerWonMiniGame(Piece piece) {
		wonMiniGame = true;
		Player player = getPlayerFromEnum(piece);
		player.resetSpecialCounter();
		player.addSpecialPiece();
		gameActions.add(GameData.ACTION_MINIGAME_WON + ':' + piece);
	}
	
	public void playerLostMiniGame() {
		playerLostMiniGame(getCurrentPlayerPiece());
	}
	
	public void playerLostMiniGame(Piece piece) {
		wonMiniGame = false;
		Player player = getPlayerFromEnum(piece);
		player.resetSpecialCounter();
		gameActions.add(GameData.ACTION_MINIGAME_LOST + ':' + piece);
	}
	
	public void playerIgnoredMiniGame() {
		playerIgnoredMiniGame(getCurrentPlayerPiece());
	}
	
	public void playerIgnoredMiniGame(Piece piece) {
		Player player = getPlayerFromEnum(piece);
		player.resetSpecialCounter();
		gameActions.add(GameData.ACTION_MINIGAME_IGNORED + ':' + piece);
	}
	
	public void generateMiniGameForCurrentPlayer() {
		int nextActivity = getCurrentPlayer().getNextActivity();
		if (nextActivity == 0) miniGame = new MathMiniGame();
		else miniGame = new WordsMiniGame();
	}
	
	public boolean checkMiniGameAnswer(String answer) {
		if (miniGame.checkAnswer(answer)) {
			gotMiniGameAnswerRight = true;
			return true;
		}
		gotMiniGameAnswerRight = false;
		return false;
	}
	
	public boolean didPlayerWinMiniGame() {
		return wonMiniGame;
	}
	
	public boolean isMiniGameFinished() {
		if (miniGame == null) return true;
		
		if (miniGame.isFinished()) {
			miniGame.stop();
			if (miniGame.playerWon()) {
				playerWonMiniGame();
			} else {
				playerLostMiniGame();
				setNextPlayer();
			}
			miniGame = null;
			return true;
		}
		return false;
	}
	
	public boolean hasMiniGameStarted() {
		return miniGame != null && miniGame.isRunning();
	}
	
	public ITimedMiniGame getMiniGame() {
		return miniGame;
	}
	
	public boolean playerGotMiniGameQuestionAnswerRight() {
		return gotMiniGameAnswerRight;
	}
	// -----------------------------------------------------------------------------------------------------------------
	
	// Getters
	public List<String> getGameActions() {
		return gameActions;
	}
	
	public Piece[][] getGameArea() {
		return gameArea;
	}
	
	public int getRound() {
		return round;
	}
	
	public int getAvailableRollbacks() {
		return lastPlays.size();
	}
	
	public Player getPlayerFromEnum(Piece playerPiece) {
		return playerPiece == Piece.PLAYER1 ? player1 : player2;
	}
	
	public Piece getCurrentPlayerPiece() {
		return currentPlayer;
	}
	
	public Player getCurrentPlayer() {
		return getPlayerFromEnum(getCurrentPlayerPiece());
	}
	
	public boolean isCurrentPlayerBot() {
		return getCurrentPlayer().getType() == PlayerType.COMPUTER;
	}
	
	public Piece getWinner() {
		return winner;
	}
	
	// Getters ---------------------------------------------------------------------------------------------------------
	
	// Board Verification ----------------------------------------------------------------------------------------------
	public Piece checkWinner() {
		for (int column = 0; column < WIDTH; column++) {
			for (int line = HEIGHT - 1; line >= 0; line--) {
				
				Piece currentPlacePiece = gameArea[line][column];
				if (currentPlacePiece == null)
					break;
				
				if (checkIfPlayerWonAt(currentPlacePiece, line, column)) {
					gameActions.add(ACTION_FINISHED + ':' + currentPlacePiece);
					currentPlayer = currentPlacePiece;
					winner = currentPlacePiece;
					return currentPlacePiece;
				}
			}
		}
		winner = null;
		return null;
	}
	
	private boolean checkIfPlayerWonAt(Piece player, int line, int column) {
		Point[] directionsAround = new Point[]{
				new Point(-1, -1), new Point(0, -1), new Point(1, -1),
				new Point(-1, 0),/*       Center      */  new Point(1, 0),
				new Point(-1, 1), new Point(0, 1), new Point(1, 1)};
		for (Point offset : directionsAround) {
			int amount = 1;
			int offX = column;
			int offY = line;
			
			while (amount <= AMOUNT_TO_WIN) {
				offX += offset.x;
				offY += offset.y;
				
				if (offX < 0 || offX >= WIDTH || offY < 0 || offY >= HEIGHT)
					break;
				if (gameArea[offY][offX] == player) {
					if (++amount == AMOUNT_TO_WIN)
						return true;
				} else
					break;
			}
		}
		return false;
	}
	
	public boolean isFull() {
		for (int column = 0; column < WIDTH; column++)
			if (gameArea[0][column] == null)
				return false;
		return true;
	}
	// Board Verification ----------------------------------------------------------------------------------------------
	
	// Helpers ---------------------------------------------------------------------------------------------------------
	private Piece[][] createGameAreaCopy() {
		Piece[][] copy = new Piece[gameArea.length][gameArea[0].length];
		for (int i = 0; i < copy.length; i++)
			copy[i] = Arrays.copyOf(gameArea[i], gameArea[i].length);
		return copy;
	}
	
	private void setNextPlayer() {
		currentPlayer = currentPlayer.getOther();
	}
	// Helpers ---------------------------------------------------------------------------------------------------------
}
