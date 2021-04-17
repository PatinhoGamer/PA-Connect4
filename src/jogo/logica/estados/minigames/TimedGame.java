package jogo.logica.estados.minigames;

public abstract class TimedGame {
	
	private long startTime;
	private long endTime;
	
	protected String question;
	protected String answer;
	
	protected boolean finished = false;
	
	public TimedGame() {
		initialize();
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public void stop() {
		endTime = System.currentTimeMillis() - startTime;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public boolean checkAnswer(String answer) {
		return this.answer.equals(answer);
	}
	
	public abstract void initialize();
	
	public abstract long availableTime();
	
	public boolean isFinished() {
		return finished;
	}
	
	public boolean playerManagedToDoIt() {
		return endTime < availableTime();
	}
}
