package jogo.logica.minigames;

public abstract class TimedGameAbstract implements TimedMiniGame {
	
	private long startTime;
	private long endTime;
	
	protected String question;
	protected String answer;
	
	protected boolean finished = false;
	
	public abstract void generateQuestion();
	
	@Override
	public void start() {
		startTime = System.currentTimeMillis();
		generateQuestion();
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
	public abstract int availableTime();
	
	@Override
	public boolean finishedAnswering() {
		return finished;
	}
	
	@Override
	public boolean playerManagedToDoIt() {
		return endTime / 1000L < availableTime();
	}
	
	@Override
	public boolean isFinished() {
		return ranOutOfTime() || finishedAnswering();
	}
	
	@Override
	public boolean ranOutOfTime() {
		int currentTime = (int) (System.currentTimeMillis() - startTime) / 1000;
		return currentTime > availableTime();
	}
}
