package jogo.logica.dados;

import jogo.logica.Connect4Logic;

public class GameSaver {
	
	private static final GameSaver instance;
	
	public static GameSaver getInstance() {
		return instance;
	}
	
	static {
		instance = new GameSaver();
	}
	
	private GameSaver() {
	}
	
	public void storeGame(Connect4Logic game) {
	
	}
}
