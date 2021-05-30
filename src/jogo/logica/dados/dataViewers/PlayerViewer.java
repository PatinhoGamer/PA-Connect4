package jogo.logica.dados.dataViewers;

import jogo.logica.dados.Player;
import jogo.logica.dados.PlayerType;

public class PlayerViewer {
	
	private final Player player;
	
	public PlayerViewer(Player player) {
		this.player = player;
	}
	
	public int getMiniGameCounter(){
		return player.getMiniGameCounter();
	}
	public int getRollbacks(){
		return player.getRollbacks();
	}
	public String getName(){
		return player.getName();
	}
	public int getSpecialPieces(){
		return player.getSpecialPieces();
	}
	public PlayerType getType(){
		return player.getType();
	}
}
