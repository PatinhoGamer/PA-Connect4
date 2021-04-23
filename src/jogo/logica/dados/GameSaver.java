package jogo.logica.dados;

import jogo.logica.FixedSizeStack;
import jogo.logica.Connect4Logic;
import jogo.logica.Replayer;
import jogo.logica.estados.StateMachine;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GameSaver {
	
	private static final String fileExtension = ".C4save";
	
	private static final FixedSizeStack<Replay> replays = new FixedSizeStack<>(5);
	
	
	private GameSaver() {
	}
	
	public static void saveReplay(Connect4Logic game) {
		List<String> gameActions = game.getGameActions();
		String player1Name = game.getPlayerFromEnum(Piece.PLAYER1).getName();
		String player2Name = game.getPlayerFromEnum(Piece.PLAYER2).getName();
		
		Replay replay = new Replay(gameActions, player1Name, player2Name, new Date());
		
		replays.push(replay);
	}
	
	public static Replayer getReplayerForGame(int index) {
		if (index < 0 || index >= replays.size())
			return null;
		return new Replayer(replays.get(index));
	}
	
	public static List<Replay> getReplays() {
		return replays;
	}
	
	public static void saveGameToFile(StateMachine wholeThing, String filePath) throws IOException {
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath + fileExtension))) {
			objectOutputStream.writeUnshared(wholeThing);
		}
	}
	
	public static StateMachine loadGameFromFile(String filePath) throws IOException, ClassNotFoundException {
		try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath + fileExtension))) {
			return (StateMachine) objectInputStream.readUnshared();
		}
	}
	
}

