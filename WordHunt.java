/*
 * Jeffrey Li
 * WordHunt
 * June 1, 2023
 */
//WordHunt Game 
//Based on GamePigeon WordHunt 
import java.util.*;
import java.io.*;

public class WordHunt {
	private static ArrayList<String> words;

	String[][] WordHunt;
	WordHuntGUI letters;
	int x = 4, y = 4, highScore, highScoreCount;
	int points;
	int interval;
	int delay, period;
	FileReader in1, in2;
	FileWriter out;
	BufferedReader readDictionary, readHighScore;
	BufferedWriter writeHighScore;
	String score;

	public WordHunt() {
		Random rand = new Random();
		String dictionary, score, letter, word;
		words = new ArrayList<String>();

		int element, index = 0;
		char randLetter;

		try {
	
			WordHunt = new String[x][y];
			// accesses words_alpha.txt a local file English dictionary
			in1 = new FileReader("/Users/jeffreyli/Downloads/words_alpha.txt");
			readDictionary = new BufferedReader(in1);
			while ((dictionary = readDictionary.readLine()) != null) {
				// words less than 3 characters are not added to the words ArrayList
				// prevents player from clicking one button "a" for example and scoring a point
				if (dictionary.length() >= 3) {
					words.add(dictionary);
					// increments for elements/words in dictionary
					index++;
				}

			}
			
			in2 = new FileReader("/Users/jeffreyli/Downloads/highscore.txt");
			readHighScore = new BufferedReader(in2);
			while ((score = readHighScore.readLine()) != null) {
				if (score != null) {
					highScore = Integer.parseInt(score);
				}
				
				
			} 

			// adds letter to each button from temp array
			for (int i = 0; i < x; i++) {
				for (int j = 0; j < y; j++) {
					// gets random word from dictionary
					element = rand.nextInt(index);
					word = words.get(element);
					// gets a random character from the length of the array
					randLetter = word.charAt(rand.nextInt(word.length()));

					// String.valueOf returns char as a string
					letter = String.valueOf(randLetter);
					WordHunt[i][j] = letter;
				}
			}

		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}

	//writes highscore to file
	public void highScore(int score) {
		try {
		out = new FileWriter("/Users/jeffreyli/Downloads/highscore.txt");
		writeHighScore = new BufferedWriter(out);
		writeHighScore.write(String.valueOf(score));
		
		writeHighScore.close();
		out.close();
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}
	

	public ArrayList<String> getWords() {
		return words;
	}

}
