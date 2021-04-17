package jogo.logica.dados;

public class Player {
	
	private final String name;
	private final PlayerType type;
	private int specialPieces = 0;
	private int specialPiecesCounter = 0;
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
	
	public int getNextActivity() {
		if (activityChooser == -1)
			activityChooser = (int) Math.round(Math.random());
		
		int choice = activityChooser;
		activityChooser = (activityChooser + 1) % 2;
		return choice;
	}
	
	@Override
	public String toString() {
		if (type == PlayerType.HUMAN) {
			return "Player{" +
					"name='" + name + '\'' +
					", specialPieces=" + specialPieces +
					", specialPiecesCounter=" + specialPiecesCounter +
					", rollbacks=" + rollbacks +
					'}';
		} else {
			return "AI{" +
					"name='" + name + "'}";
		}
	}
}
