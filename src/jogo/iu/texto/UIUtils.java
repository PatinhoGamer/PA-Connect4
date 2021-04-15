package jogo.iu.texto;

import java.util.Scanner;

public class UIUtils {
	
	private static final Scanner scanner = new Scanner(System.in);
	
	public static int chooseOption(String zero, String... options) {
		int lower = 1, higher = options.length;
		
		if (zero != null) {
			System.out.println("0 - " + zero);
			lower = 0;
		}
		
		for (int i = 0; i < options.length; i++)
			System.out.println((i + 1) + " - " + options[i]);
		
		return getChoice(lower, higher);
	}
	
	public static int getChoice(int lower, int higher) {
		while (true) {
			int choice = -1;
			try {
				choice = scanner.nextInt();
			} catch (Exception ex) {
				scanner.nextLine();
				System.out.println("Invalid Input");
				continue;
			}
			if (choice < lower || choice > higher) {
				System.out.println("Invalid Choice");
				continue;
			}
			return choice;
		}
	}
}
