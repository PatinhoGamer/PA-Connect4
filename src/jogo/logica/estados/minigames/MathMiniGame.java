package jogo.logica.estados.minigames;

public class MathMiniGame extends TimedGame {
	
	private int answeredQuestions = 0;
	private final int TO_ANSWER = 5;
	
	@Override
	public void initialize() {
		
		int number1 = (int) ((Math.random() * 98) + 1);
		int number2 = (int) ((Math.random() * 98) + 1);
		
		int operation = (int) Math.floor(Math.random() * 4);
		char operationChar;
		
		int result;
		
		switch (operation) {
			case 0 -> {
				operationChar = '+';
				result = number1 + number2;
			}
			case 1 -> {
				operationChar = '-';
				result = number1 - number2;
			}
			case 2 -> {
				operationChar = '*';
				result = number1 * number2;
			}
			case 3 -> {
				operationChar = '/';
				result = number1 / number2;
			}
			default -> throw new IllegalStateException("Unexpected value: " + operation);
		}
		answer = String.valueOf(result);
	}
	
	@Override
	public long availableTime() {
		return 30 * 1000;
	}
	
	@Override
	public boolean checkAnswer(String answer) {
		if (super.checkAnswer(answer)) {
			answeredQuestions++;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isFinished() {
		return answeredQuestions >= TO_ANSWER;
	}
}
