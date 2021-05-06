package jogo.logica.dados;

import java.io.Serializable;

public class Player implements Serializable {
	
	private final String name;
	private final PlayerType type;
	private int specialPieces = 0;
	private int miniGameCounter = 0;
	private int rollbacks = 5;
	
	private int activityChooser = -1;
	
	public Player(String name, PlayerType type) {
		this.name = name;
		this.type = type;
	}
	
	public PlayerType getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public int getSpecialPieces() {
		return specialPieces;
	}
	
	public void useSpecialPiece() {
		if (specialPieces == 0) throw new IllegalStateException("Cant use special pieces without having them");
		specialPieces--;
	}
	
	public void addSpecialPiece() {
		specialPieces++;
	}
	
	public int getMiniGameCounter() {
		return miniGameCounter;
	}
	
	public void incrementMiniGameCounter() {
		this.miniGameCounter++;
	}
	
	public void resetSpecialCounter() {
		this.miniGameCounter = 0;
	}
	
	public int getRollbacks() {
		return rollbacks;
	}
	
	public void setRollbacks(int rollbacks) {
		this.rollbacks = rollbacks;
	}
	
	public int getNextActivity() {
		if (activityChooser == -1)
			activityChooser = (int) Math.round(Math.random());
		
		int choice = activityChooser;
		activityChooser = (activityChooser + 1) % 2;
		return choice;
	}
	
	@Override
	public String toString() {
		if (type == PlayerType.HUMAN)
			return "\tPlayer -> name : " + name +
					"\n\tspecialPieces : " + specialPieces +
					"\n\trollbacks : " + rollbacks;
		else return "\tAI -> name : " + name;
	}
}
