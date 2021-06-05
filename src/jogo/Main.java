package jogo;

import jogo.iu.gui.Connect4UI;
import jogo.iu.texto.Connect4TextUI;
import jogo.logica.GameSaver;

import java.io.IOException;

public class Main {
	
	public static final String replaySaveFile = "replays";
	
	public static void main(String[] args) {
		boolean textVersion = false;
		
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
				case "-text" -> textVersion = true;
			}
		}
		
		try {
			System.out.println("Loading replays from Disk");
			GameSaver.getInstance().loadReplaysFromDisk(replaySaveFile);
		} catch (IOException e) {
			System.out.println("Error loading replays from file, maybe it doesn't exist. It will be created on exit");
		} catch (ClassNotFoundException e) {
			System.out.println("Class cast exception. Should never happen, maybe there was a change in one of the classes");
		}
		
		if (!textVersion) {
			Connect4UI.main(args);
		} else {
			new Connect4TextUI().start();
		}
		
		try {
			System.out.println("Saving replays to Disk");
			GameSaver.getInstance().sendReplaysToDisk(replaySaveFile);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error saving replays to file. Should never happen");
		}
	}
}
