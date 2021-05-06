package jogo.logica.minigames;

public interface TimedMiniGame {
	void start();
	
	void stop();
	
	String getQuestion();
	
	String getGameObjective();
	
	boolean checkAnswer(String answer);
	
	int availableTime();
	
	boolean finishedAnswering();
	
	boolean playerManagedToDoIt();
	
	boolean ranOutOfTime();
}
