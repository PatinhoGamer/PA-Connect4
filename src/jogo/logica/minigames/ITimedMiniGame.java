package jogo.logica.minigames;

public interface ITimedMiniGame {
	void start();
	
	void stop();
	
	String getQuestion();
	
	String getGameObjective();
	
	boolean checkAnswer(String answer);
	
	int getAvailableTime();
	
	boolean finishedAnswering();
	
	boolean isFinished();
	
	boolean playerWon();
	
	boolean ranOutOfTime();
	
	boolean isRunning();
}
