package jogo;

import jogo.iu.texto.Connect4TextUI;
import jogo.logica.GameSaver;

import java.io.IOException;

public class Main {
	
	public static final String replaySaveFile = "replays";
	
	public static void main(String[] args) {
		try {
			GameSaver.loadReplaysFromDisk(replaySaveFile);
		} catch (IOException e) {
			System.out.println("Error loading replays from file, maybe it doesnt exist. It will be created on exit");
		} catch (ClassNotFoundException e) {
			System.out.println("Class cast exception. Should never happen");
		}
		
		Connect4TextUI ui = new Connect4TextUI();
		ui.start();
		
		try {
			GameSaver.sendReplaysToDisk(replaySaveFile);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error saving replays to file. Should never happen");
		}
	}
}
