package jogo.logica.minigames;

public abstract class TimedGameAbstract implements ITimedMiniGame {
	
	private long startTime;
	private long endTime;
	
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
	public boolean playerWon() {
		return endTime / 1000L < getAvailableTime();
	}
	
	@Override
	public boolean isFinished() {
		return ranOutOfTime() || finishedAnswering();
	}
	
	@Override
	public boolean ranOutOfTime() {
		int currentTime = (int) (System.currentTimeMillis() - startTime) / 1000;
		return currentTime > getAvailableTime();
	}
}
