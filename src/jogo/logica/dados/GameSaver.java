package jogo.logica.dados;

import jogo.logica.FixedSizeStack;
import jogo.logica.Connect4Logic;
import jogo.logica.estados.StateMachine;

import java.io.*;
import java.util.List;

public class GameSaver {
	
	private static final String fileExtension = ".C4save";
	
	private static final FixedSizeStack<List<String>> replays = new FixedSizeStack<>(5);
	
	private GameSaver() {
	}
	
	public static void saveReplay(Connect4Logic game) {
		replays.push(game.getGameActions());
	}
	
	//TODO find a way to play the replays
	
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
