package jogo.logica;

import jogo.logica.dados.FixedSizeStack;
import jogo.logica.dados.Replay;
import jogo.logica.estados.GameState;

import java.io.*;
import java.util.Date;
import java.util.List;

public class GameSaver {
	
	public static final String fileExtensionReplays = ".C4replays";
	public static final String fileExtensionSave = ".C4save";
	
	private FixedSizeStack<Replay> replays = new FixedSizeStack<>(5);
	
	private static GameSaver instance = null;
	
	public static GameSaver getInstance() {
		if (instance == null)
			instance = new GameSaver();
		return instance;
	}
	
	private GameSaver() {
	}
	
	public void saveReplay(List<String> gameActions, String winner, String loser) {
		Replay replay = new Replay(gameActions, winner, loser, new Date());
		replays.push(replay);
	}
	
	public Replayer getReplayerForGame(int index) {
		if (index < 0 || index >= replays.size())
			return null;
		return new Replayer(replays.get(index));
	}
	
	public List<Replay> getReplays() {
		return replays;
	}
	
	public void saveCurrentStateToFile(GameState wholeState, String filePath) throws IOException {
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
			objectOutputStream.writeUnshared(wholeState);
		}
	}
	
	public GameState loadGameFromFile(String filePath) throws IOException, ClassNotFoundException {
		try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
			return (GameState) objectInputStream.readUnshared();
		}
	}
	
	public void sendReplaysToDisk(String filePath) throws IOException {
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath + fileExtensionReplays))) {
			objectOutputStream.writeUnshared(replays);
		}
	}
	
	public void loadReplaysFromDisk(String filePath) throws IOException, ClassNotFoundException {
		try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath + fileExtensionReplays))) {
			replays = (FixedSizeStack<Replay>) objectInputStream.readUnshared();
		}
	}
}

