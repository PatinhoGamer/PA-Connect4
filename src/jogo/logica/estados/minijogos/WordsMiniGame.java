package jogo.logica.estados.minijogos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class WordsMiniGame extends TimedGame {
	
	private String lineToWrite;
	
	private String[] getWordsFromFile(File file) throws IOException {
		List<String> lines = Files.readAllLines(file.toPath());
		String[] words = new String[lines.size()];
		for (int i = 0; i < lines.size(); i++)
			words[i] = lines.get(i).trim();
		return words;
	}
	
	private String getLineToWrite(String[] words) {
		List<Integer> alreadyChosen = new ArrayList<>(5);
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; alreadyChosen.size() < 5; i++) {
			int chosenOne = (int) Math.ceil(Math.random() * words.length);
			
			if (alreadyChosen.contains(chosenOne))
				continue;
			
			alreadyChosen.add(chosenOne);
			builder.append(words[chosenOne]);
		}
		return builder.toString();
	}
	
	@Override
	public long availableTime() {
		return lineToWrite.length() / 2 * 1000L;
	}
}
