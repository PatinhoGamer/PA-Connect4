package jogo.logica.minigames;

public abstract class TimedGameAbstract implements ITimedMiniGame {
	
	private long startTime = 0;
	private long endTime = -1;
	
	protected String question;
	protected String answer;
	
	protected boolean finished = false;
	
	public abstract void generateQuestion();
	
	public TimedGameAbstract() {
		generateQuestion();
	}
	
	@Override
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void stop() {
		endTime = System.currentTimeMillis() - startTime;
	}
	
	@Override
	public String getQuestion() {
		return question;
	}
	
	@Override
	public abstract String getGameObjective();
	
	@Override
	public boolean checkAnswer(String answer) {
		return this.answer.equals(answer);
	}
	
	@Override
	public abstract int getAvailableTime();
	
	@Override
	public boolean finishedAnswering() {
		return finished;
	}
	
	@Override
	public final boolean playerWon() {
		return finishedAnswering() && (endTime / 1000L < getAvailableTime());
	}
	
	@Override
	public final boolean isFinished() {
		return ranOutOfTime() || finishedAnswering();
	}
	
	@Override
	public final boolean ranOutOfTime() {
		return elapsedTime() > getAvailableTime();
	}
	
	@Override
	public String toString() {
		return "TimedGameAbstract{" +
				"question='" + question + '\'' +
				", answer='" + answer + '\'' +
				", isFinished=" + isFinished() +
				", available time = " + getAvailableTime() +
				", elapsed time = " + elapsedTime() +
				", start time = " + startTime + '\n';
	}
	
	@Override
	public boolean isRunning() {
		return startTime != 0;
	}
	
	private long elapsedTime() {
		return (System.currentTimeMillis() - startTime) / 1000;
	}
}
