package jogo.logica.estados.minigames;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WordsMiniGame extends TimedGame {
	
	private static final String WORDS_PATH = "words.txt";
	
	private String[] getWordsFromFile(String file) throws IOException {
		List<String> lines = Files.readAllLines(Path.of(file));
		String[] words = new String[lines.size()];
		for (int i = 0; i < lines.size(); i++)
			words[i] = lines.get(i).trim();
		return words;
	}
	
	private String getLineToWrite(String[] words) {
		List<Integer> alreadyChosen = new ArrayList<>(5);
		StringBuilder builder = new StringBuilder();
		
		while (alreadyChosen.size() < 5) {
			int chosenOne = (int) Math.ceil(Math.random() * words.length);
			
			if (alreadyChosen.contains(chosenOne))
				continue;
			
			alreadyChosen.add(chosenOne);
			builder.append(words[chosenOne]);
		}
		return builder.toString();
	}
	
	@Override
	public void initialize() {
		try {
			String[] wordsFromFile = getWordsFromFile(WORDS_PATH);
			answer = getLineToWrite(wordsFromFile);
			question = answer;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public long availableTime() {
		return answer.length() / 2 * 1000L;
	}
	
	@Override
	public boolean checkAnswer(String answer) {
		if(super.checkAnswer(answer)){
			
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isFinished() {
		return false;
	}
}
