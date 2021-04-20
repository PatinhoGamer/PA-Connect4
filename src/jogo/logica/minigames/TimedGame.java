package jogo.logica.minigames;

public abstract class TimedGame {
	
	private long startTime;
	private long endTime;
	
	protected String question;
	protected String answer;
	
	protected boolean finished = false;
	
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public void stop() {
		endTime = System.currentTimeMillis() - startTime;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public abstract String getGameObjective();
	
	public boolean checkAnswer(String answer) {
		return this.answer.equals(answer);
	}
	
	public abstract void generateQuestion();
	
	public abstract long availableTime();
	
	public boolean finishedAnswering() {
		return finished;
	}
	
	public boolean playerManagedToDoIt() {
		return endTime < availableTime();
	}
	
	public boolean ranOutOfTime() {
		long currentTime = System.currentTimeMillis() - startTime;
		return currentTime > availableTime();
	}
}
