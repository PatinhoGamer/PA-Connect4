package jogo.logica.minigames;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class WordsMiniGame extends TimedGame {
	
	private static final String GAME_OBJECTIVE = "In this minigame you have to type out the showed words";
	private static final String WORDS_PATH = "words.txt";
	
	private static String[] cachedWords = null;
	
	private String[] getWords() throws IOException {
		if (cachedWords != null)
			return cachedWords;
		
		List<String> lines = Files.readAllLines(Path.of(WordsMiniGame.WORDS_PATH));
		String[] words = new String[lines.size()];
		for (int i = 0; i < lines.size(); i++)
			words[i] = lines.get(i).trim();
		
		cachedWords = words;
		return words;
	}
	
	private String getLineToWrite(String[] words) {
		List<Integer> alreadyChosen = new ArrayList<>(5);
		StringJoiner builder = new StringJoiner(" ");
		
		while (alreadyChosen.size() < 5) {
			int chosenOne = (int) Math.floor(Math.random() * words.length);
			
			if (alreadyChosen.contains(chosenOne))
				continue;
			
			alreadyChosen.add(chosenOne);
			builder.add(words[chosenOne]);
		}
		return builder.toString();
	}
	
	@Override
	public String getGameObjective() {
		return GAME_OBJECTIVE;
	}
	
	@Override
	public void generateQuestion() {
		if (question == null) {
			try {
				String[] wordsFromFile = getWords();
				answer = getLineToWrite(wordsFromFile);
				question = answer;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public long availableTime() {
		return answer.length() / 2 * 1000L;
	}
	
	@Override
	public boolean checkAnswer(String answer) {
		if (super.checkAnswer(answer)) {
			finished = true;
			return true;
		}
		return false;
	}
}
