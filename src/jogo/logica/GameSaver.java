package jogo.logica;

import jogo.logica.dados.Replay;
import jogo.logica.estados.GameState;

import java.io.*;
import java.util.Date;
import java.util.List;

public class GameSaver {
	
	public static final String fileExtensionReplays = ".C4replays";
	public static final String fileExtensionSave = ".C4save";
	
	private static FixedSizeStack<Replay> replays = new FixedSizeStack<>(5);
	
	
	private GameSaver() {
	}
	
	public static void saveReplay(List<String> gameActions, String winner, String loser) {
		Replay replay = new Replay(gameActions, winner, loser, new Date());
		
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
	
	public static void saveCurrentStateToFile(GameState wholeState, String filePath) throws IOException {
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
			objectOutputStream.writeUnshared(wholeState);
		}
	}
	
	public static GameState loadGameFromFile(String filePath) throws IOException, ClassNotFoundException {
		try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
			return (GameState) objectInputStream.readUnshared();
		}
	}
	
	public static void sendReplaysToDisk(String filePath) throws IOException {
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath + fileExtensionReplays))) {
			objectOutputStream.writeUnshared(replays);
		}
	}
	
	public static void loadReplaysFromDisk(String filePath) throws IOException, ClassNotFoundException {
		try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath + fileExtensionReplays))) {
			replays = (FixedSizeStack<Replay>) objectInputStream.readUnshared();
		}
	}
}

