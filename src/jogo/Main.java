package jogo;

import jogo.iu.gui.Connect4UI;
import jogo.iu.texto.Connect4TextUI;
import jogo.logica.GameSaver;

import java.io.IOException;

public class Main {
	
	public static final String replaySaveFile = "replays";
	
	public static void main(String[] args) {
		boolean textVersion = false;
		if (args.length > 0) {
			if (args[0].equals("-text")) {
				textVersion = true;
			}
		}
		
		try {
			GameSaver.loadReplaysFromDisk(replaySaveFile);
		} catch (IOException e) {
			System.out.println("Error loading replays from file, maybe it doesnt exist. It will be created on exit");
		} catch (ClassNotFoundException e) {
			System.out.println("Class cast exception. Should never happen");
		}
		
		if (!textVersion) {
			Connect4UI.main(args);
		} else {
			new Connect4TextUI().start();
		}
		
		try {
			GameSaver.sendReplaysToDisk(replaySaveFile);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error saving replays to file. Should never happen");
		}
	}
}
