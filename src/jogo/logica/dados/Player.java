package jogo.logica.dados;

public class Player {
	
	private String name;
	private int specialPieces = 0;
	private int specialPiecesCounter = 0;
	private int rollbacks = 5;
	
	public Player(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getSpecialPieces() {
		return specialPieces;
	}
	
	public void setSpecialPieces(int specialPieces) {
		this.specialPieces = specialPieces;
	}
	
	public int getSpecialPiecesCounter() {
		return specialPiecesCounter;
	}
	
	public void incrementSpecialCounter() {
		this.specialPiecesCounter++;
	}
	
	public void resetSpecialCounter() {
		this.specialPiecesCounter = 0;
	}
	
	public int getRollbacks() {
		return rollbacks;
	}
	
	public void setRollbacks(int rollbacks) {
		this.rollbacks = rollbacks;
	}
	
	@Override
	public String toString() {
		return "Player{" +
				"name='" + name + '\'' +
				", specialPieces=" + specialPieces +
				", specialPiecesCounter=" + specialPiecesCounter +
				", rollbacks=" + rollbacks +
				'}';
	}
}
