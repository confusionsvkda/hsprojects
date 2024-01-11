/*
 * Jeffrey Li
 * WordHuntGUI
 * June 1, 2023
 */
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.awt.*;
import javax.sound.sampled.*;
import java.io.*;
import javax.swing.*;

public class WordHuntGUI implements ActionListener {
	//currentChain is an ArrayList for letters to be added to when the player clicks on a button
	private static ArrayList<String> currentChain = new ArrayList<>();
	private static ArrayList<String> discoveredWord = new ArrayList<>();
	private static ArrayList<String> foundWords = new ArrayList<>();

	// timer length
	private static final int DURATION = 80;
	private WordHunt wordhunt = new WordHunt();
	private Timer timer;
	private int timeRemainder = DURATION;
	private int maxWordLength = 0, clickedCounter = 0;
	private int x = 4;
	private int y = 4;
	private ImageIcon resizedBB, resizedPT, resizedST, resizedGT;
	private JLabel timerDisplay, pointDisplay, highScore, numWords, directions, PT, ST, GT;
	private JPanel gamePanel, clearPanel;
	private JButton[][] letters = new JButton[x][y];
	private JButton prevClicked = null;
	private Font customFont1;
	private Font customFont2;
	private Font customFont3;

	public WordHuntGUI() {

		JFrame frame;
		JPanel container, difficultyPanel, layoutPanel, wordBank;
		JButton clear;

		//custom fonts 
		customFont1 = new Font("Helevitca", Font.BOLD, 40);
		customFont2 = new Font("Helevitca", Font.BOLD, 20);
		customFont3 = new Font("Sans Serif", Font.BOLD, 20);
		

		timer = new Timer(1000, this);
		timerDisplay = new JLabel();
		updateTimer();

		pointDisplay = new JLabel();
		updatePoints();
		
		highScore = new JLabel();
		updateHighScore();

		directions = new JLabel("Click on any button to start.");

		ImageIcon buttonBackground = new ImageIcon("/Users/jeffreyli/Downloads/buttonbackground.jpg");
		//images are resized with scaledIcon method
		resizedBB = scaledIcon(buttonBackground, 170, 110);

		ImageIcon participationTrophy = new ImageIcon("/Users/jeffreyli/Downloads/participation.jpg") ;
		resizedPT = scaledIcon(participationTrophy , 65,65);
		PT = new JLabel(resizedPT);
		
		ImageIcon silverTrophy = new ImageIcon("/Users/jeffreyli/Downloads/silver.jpg") ;
		resizedST = scaledIcon(silverTrophy , 65,65);
		ST = new JLabel(resizedST);
		
		ImageIcon goldTrophy = new ImageIcon("/Users/jeffreyli/Downloads/gold.jpg") ;
		resizedGT = scaledIcon(goldTrophy , 65,65);
		GT = new JLabel(resizedGT);
		
		// setting GUI layout
		frame = new JFrame("Word Hunt");

		container = new JPanel();
		container.setPreferredSize(new Dimension(800, 850));
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		gamePanel = new JPanel();
		gamePanel.setPreferredSize(new Dimension(500, 500));
		gamePanel.setLayout(new GridLayout(x, y));
		gamePanel.setBorder(BorderFactory.createLineBorder(new Color(70, 93, 64), 40));

		difficultyPanel = new JPanel();
		difficultyPanel.setLayout(new GridLayout(x, y));

		layoutPanel = new JPanel();
		layoutPanel.setPreferredSize(new Dimension(500, 500));

		clearPanel = new JPanel();
		clearPanel.setLayout(new BoxLayout(clearPanel, BoxLayout.X_AXIS));
		clearPanel.setPreferredSize(new Dimension(200, 200));

		wordhunt = new WordHunt();
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				//sets image and sets text toUpperCase
				letters[i][j] = new JButton(wordhunt.WordHunt[i][j].toUpperCase(), resizedBB);
				letters[i][j].setBorder(BorderFactory.createLineBorder(new Color(78, 93, 95), 6));
				letters[i][j].setFont(customFont1);
				letters[i][j].setHorizontalTextPosition(JButton.CENTER);
				letters[i][j].setVerticalTextPosition(JButton.CENTER);
				letters[i][j].addActionListener(this);
				// Colors background and sets transparent
				letters[i][j].setOpaque(true);
				letters[i][j].setBackground(new Color(246, 222, 155));
				gamePanel.add(letters[i][j]);
			
			}

		}

		// calls findWords to search for words on grid
		findWords();
		updateTrophies();
		
		numWords = new JLabel();
		numPossibleWords();

		directions = new JLabel("Click on any button to start.");
		directions.setFont(customFont3);
		difficultyPanel.add(directions);
		difficultyPanel.add(numWords);
		
		

		clear = new JButton("CLEAR");
		clear.setFont(customFont2);
		clear.setOpaque(true);
		clear.setBackground(new Color(210, 43, 43));
		clear.setMaximumSize(new Dimension(500, 100));
		clear.setActionCommand("CLEAR");
		clear.addActionListener(this);
		clearPanel.add(clear);

		
		clearPanel.add(timerDisplay);
		clearPanel.add(pointDisplay);
		clearPanel.add(highScore);


		clearPanel.setAlignmentX(Box.CENTER_ALIGNMENT);

		// container.add(layoutPanel);
		container.add(difficultyPanel);
		container.add(gamePanel);
		container.add(clearPanel);

		frame.setContentPane(container);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
	
	

	

	// scales image to fit whatever
	private static ImageIcon scaledIcon(ImageIcon icon, int width, int height) {
		Image image = icon.getImage();
		Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(scaledImage);
	}

	//updates timer
	public void updateTimer() {
		timerDisplay.setText("Time: " + timeRemainder + " ");
		timerDisplay.setFont(customFont2);
	}

	//updates points counter
	public void updatePoints() {
		pointDisplay.setText("Points: " + wordhunt.points + " ");
		pointDisplay.setFont(customFont2);
	}
	
	//updates the highScore counter
	public void updateHighScore() {
		highScore.setFont(customFont2);
		if (wordhunt.points > wordhunt.highScore) {
			wordhunt.highScore(wordhunt.points);
		}
		highScore.setText("High Score: " + wordhunt.highScore + " ");
	}
	
	public void updateTrophies() {
		if (wordhunt.highScore >= 7000) {
			clearPanel.add(GT);
		} else if (wordhunt.highScore >= 4000 ) {
			clearPanel.add(ST);
		} else if (wordhunt.highScore >= 1000) {
			clearPanel.add(PT);
		}
	}
	
	//displays number of possible word combinations 
	public void numPossibleWords() {
		int num = foundWords.size();
		numWords.setFont(customFont2);
		numWords.setText("Number of words on grid: " + num);
	}

	private ArrayList<String> findWords() {
		// since i'm using almost an entire dictionary, I learned that a HashSet is
		// much more efficient compared to an ArrayList
		// in storing and searching for elements greater than about 6.
		//I tested it with an ArrayList and with a HashSet and the HashSet started the program much faster
		HashSet<String> validWords = new HashSet<>(wordhunt.getWords());

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				findWordsSearch(i, j, new ArrayList<>(), validWords);
			}
		}
		System.out.println(foundWords);

		// takes the longest word from the arraylist of valid words so that if the
		// button clicks
		// exceed that, the board is reset so that the player can start over.
		// it is an alternative solution to the actual game that uses a tap and drag
		// while this uses buttons.
		// loops the entire arraylist
		for (int i = 0; i < foundWords.size(); i++) {
			// checks word at current index i
			String word = foundWords.get(i);
			int wordLength = word.length();
			// if the current word is greater than the longest word stored, it becomes the
			// longest
			// it updates until the largest word is found
			if (wordLength > maxWordLength) {
				maxWordLength = wordLength;
			}
			System.out.println(maxWordLength);
		}
		return foundWords;

	}

	private void findWordsSearch(int x, int y, ArrayList<String> chain, HashSet<String> validWords) {
		
		//this checks if the position isn't out of bounds and the current letter is in chain 
		if (x < 0 || x >= 4 || y < 0 || y >= 4 || (chain.contains(wordhunt.WordHunt[x][y]))) {
			return;
		}
		
		chain.add(wordhunt.WordHunt[x][y]);
		
		// checks if the current chain forms a word
		// joins letters into string with String.join
		//if the joined chain of letters matches a word in validWords then it is added to foundWords
		if (chain.size() >= 3 && validWords.contains(String.join("", chain))) {
			foundWords.add(String.join("", chain).toUpperCase());
			// prevents duplicate words from being added to foundWords by removing it from validWords after the word has been found
			validWords.remove(String.join("", chain));
		}

		// Recursively checks all adjacent letters 
		findWordsSearch(x - 1, y, chain, validWords); // up
		findWordsSearch(x + 1, y, chain, validWords); // down
		findWordsSearch(x, y - 1, chain, validWords); // left
		findWordsSearch(x, y + 1, chain, validWords); // right
		findWordsSearch(x - 1, y - 1, chain, validWords); // up-left
		findWordsSearch(x - 1, y + 1, chain, validWords); // up-right
		findWordsSearch(x + 1, y - 1, chain, validWords); // down-left
		findWordsSearch(x + 1, y + 1, chain, validWords); // down-right

		//**understanding backtracking had help from my brother**
		//this removes the last letter from the chain
		//current position would be exhausted of all possible words found
		//removing the end allows it to go back to the previous position and letter to search other possible words from there
		chain.remove(chain.size() - 1);

	}

	// GameOver method to display game over pop up, and disabling the buttons to
	// prevent additional button clicks
	private void gameOver() {
		playSound("/Users/jeffreyli/Downloads/complete.wav");
		
		if (wordhunt.points > wordhunt.highScore) {
			JOptionPane.showMessageDialog(null, "Game Over!" + " New Highscore! : " + wordhunt.points);
		} else {
			JOptionPane.showMessageDialog(null, "Game Over!" + " Points: " + wordhunt.points);
		}
		
		timer.stop();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				letters[i][j].setEnabled(false);
			}
			System.exit(0);
		}
	}

	public void actionPerformed(ActionEvent e) {
		String selected = e.getActionCommand();

		// Countdown timer function
		if (e.getSource() == timer) {
			timeRemainder--;
			playSound("/Users/jeffreyli/Downloads/ticktock.wav");
			// stops timer and initiates a game over when time is 0
			if (timeRemainder == 3) {
				timerDisplay.setForeground(Color.RED);
				directions.setText("Times almost up...");
				directions.setForeground(Color.RED);
				playSound("/Users/jeffreyli/Downloads/warning.wav");
			} else if (timeRemainder == -1) {
				updateHighScore();
				gameOver();
			} else {
				updateTimer();
			}
		} else {

			// checks button click on 2D array of buttons
			JButton clicked = (JButton) e.getSource();
			String clickedWord;

			int clickedX = 0;
			int clickedY = 0;

			for (int i = 0; i < letters.length; i++) {
				for (int j = 0; j < letters[0].length; j++) {
					if (letters[i][j] != clicked) {
						directions.setText("Click CLEAR to reset chain if you mess up.");
						// for buttons that have not been clicked, disable them
						// reenabling the adjacent buttons is handled below
						letters[i][j].setEnabled(false);
						letters[i][j].setBackground(new Color(254, 254, 254));

					}
				}
			}

			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (letters[i][j] == clicked) {
						// disables the button clicked
						letters[i][j].setEnabled(false);
						letters[i][j].setBackground(new Color(178, 253, 160));
						playSound("/Users/jeffreyli/Downloads/click.wav");
						// clickedX and clickedY are the players current position

						clickedX = i;
						clickedY = j;
						clickedCounter++;
					}
				}
			}

			if (selected == "CLEAR") {
				resetBoard();
				playSound("/Users/jeffreyli/Downloads/select.wav");
			} else {
				if (prevClicked != null) {
					prevClicked.setEnabled(false);
				}

				// enables buttons next to clicked button if it is not out of bounds
				// it also checks if the previous button
				//if the previous button doesn't equal the current positon , enable the adjacent button

				if (clickedX - 1 >= 0) {
					if (prevClicked != letters[clickedX - 1][clickedY]) {
						letters[clickedX - 1][clickedY].setEnabled(true); // up
					}
				}
				if (clickedX + 1 < letters.length) {
					if (prevClicked != letters[clickedX + 1][clickedY]) {
						letters[clickedX + 1][clickedY].setEnabled(true); // down
					}
				}
				if (clickedY - 1 >= 0) {
					if (prevClicked != letters[clickedX][clickedY - 1]) {
						letters[clickedX][clickedY - 1].setEnabled(true); // left
					}
				}
				if (clickedY + 1 < letters[0].length) {
					if (prevClicked != letters[clickedX][clickedY + 1]) {
						letters[clickedX][clickedY + 1].setEnabled(true); // right
					}
				}
				if (clickedX - 1 >= 0 && clickedY - 1 >= 0) {
					if (prevClicked != letters[clickedX - 1][clickedY - 1]) {
						letters[clickedX - 1][clickedY - 1].setEnabled(true); // up-left
					}
				}
				if (clickedX - 1 >= 0 && clickedY + 1 < letters[0].length) {
					if (prevClicked != letters[clickedX - 1][clickedY + 1]) {
						letters[clickedX - 1][clickedY + 1].setEnabled(true); // up-right
					}
				}
				if (clickedX + 1 < letters.length && clickedY - 1 >= 0) {
					if (prevClicked != letters[clickedX + 1][clickedY - 1]) {
						letters[clickedX + 1][clickedY - 1].setEnabled(true); // down-left
					}
				}
				if (clickedX + 1 < letters.length && clickedY + 1 < letters[0].length) {
					if (prevClicked != letters[clickedX + 1][clickedY + 1]) {
						letters[clickedX + 1][clickedY + 1].setEnabled(true); // down-right
					}
				}

				prevClicked = clicked;
			}

			
			// starts countdown timer once button is clicked.
			timer.start();

			// prevents "CLEAR" from being added to currentChain
			// the "CLEAR" button follows the same logic as the other buttons which is why
			// it gets added
			if (!selected.equals("CLEAR")) {
				// adds letter from clicked button to currentChain arraylist
				currentChain.add(clicked.getText());
				// String.join joins letters from currentChain into one string
				clickedWord = String.join("", currentChain);
				System.out.println(clickedWord);

				// resets board after button clicks exceed the length of the longest word on the
				// board
				if (clickedCounter > maxWordLength) {
					resetBoard();
					clickedWord = "";
				}

				System.out.println(clickedCounter);

				if (foundWords.contains(clickedWord)) {
					if (discoveredWord.contains(clickedWord)) {

						//depending on the size of the word, the points vary
						//board is reset after word is found
					} else {
						if (clickedWord.length() >= 6) {
							playSound("/Users/jeffreyli/Downloads/found4.wav");
							wordhunt.points += 1400;
							resetBoard();
						}
						if (clickedWord.length() >= 5) {
							playSound("/Users/jeffreyli/Downloads/found3.wav");
							wordhunt.points += 800;
							resetBoard();
						} else if (clickedWord.length() >= 4) {
							playSound("/Users/jeffreyli/Downloads/found2.wav");
							wordhunt.points += 400;
							resetBoard();
						} else if (clickedWord.length() >= 3) {
							playSound("/Users/jeffreyli/Downloads/found1.wav");
							wordhunt.points += 100;
							resetBoard();

						}
						

						// adds word to discoveredWord for already found words
						discoveredWord.add(clickedWord);
						// removes discovered word from foundWords ArrayList
						foundWords.remove(clickedWord);

						updatePoints();
					}

				}
			}
		}

	}

//resets board to original state reenabling buttons, setting color, and clearing the chain of letters from user click
	private void resetBoard() {

		currentChain.clear();
		clickedCounter = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				letters[i][j].setOpaque(true);
				letters[i][j].setBackground(new Color(246, 222, 155));
				letters[i][j].setEnabled(true);
			}
		}
		currentChain.clear();

	}

	//plays audio files when called 
	private void playSound(String soundFile) {
		try {
			File sound = new File(soundFile);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(sound);

			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.start();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private static void runGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		WordHuntGUI game = new WordHuntGUI();
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				runGUI();
			}
		});

	}

}
