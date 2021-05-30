package jogo.logica.minigames;

public class MathMiniGame extends TimedGameAbstract {
	
	private static final String GAME_OBJECTIVE = "In this minigame you have to guess the mathematical answer to the presented question (ignore decimals)";
	private static final int TO_ANSWER = 5;
	private static final int MAX_NUMBER = 99;
	
	private int answeredQuestions = 0;
	
	@Override
	public void generateQuestion() {
		int number1 = (int) (Math.random() * MAX_NUMBER + 1);
		int number2 = (int) (Math.random() * MAX_NUMBER + 1);
		
		int operation = (int) Math.floor(Math.random() * 4);
		char operationChar = new char[]{'+', '-', '*', '/'}[operation];
		int result;
		
		switch (operation) {
			case 0 -> result = number1 + number2;
			case 1 -> result = number1 - number2;
			case 2 -> result = number1 * number2;
			case 3 -> result = number1 / number2;
			default -> throw new IllegalStateException("Unexpected value: " + operation);
		}
		
		question = String.valueOf(number1) + ' ' + operationChar + ' ' + number2;
		answer = String.valueOf(result);
	}
	
	@Override
	public boolean checkAnswer(String answer) {
		if (super.checkAnswer(answer)) {
			answeredQuestions++;
			if (!finishedAnswering())
				generateQuestion();
			return true;
		}
		generateQuestion();
		return false;
	}
	
	@Override
	public String getGameObjective() {
		return GAME_OBJECTIVE;
	}
	
	@Override
	public int getAvailableTime() {
		return 30;
	}
	
	@Override
	public boolean finishedAnswering() {
		return answeredQuestions >= TO_ANSWER;
	}
}
