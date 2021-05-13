package jogo.logica;

import jogo.logica.dados.Replay;
import jogo.logica.estados.StateMachine;

import java.io.*;
import java.util.Date;
import java.util.List;

public class GameSaver {
	
	private static final String fileExtension = ".C4save";
	
	private static FixedSizeStack<Replay> replays = new FixedSizeStack<>(5);
	
	
	private GameSaver() {
	}
	
	public static void saveReplay(List<String> gameActions,String winner,String loser) {
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
	
	public static void sendReplaysToDisk(String filePath) throws IOException {
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath + fileExtension))) {
			objectOutputStream.writeUnshared(replays);
		}
	}
	
	public static void loadReplaysFromDisk(String filePath) throws IOException, ClassNotFoundException {
		try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath + fileExtension))) {
			replays = (FixedSizeStack<Replay>) objectInputStream.readUnshared();
		}
	}
}

