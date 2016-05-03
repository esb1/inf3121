package hangman;

import java.util.Random;
import java.util.Scanner;

// ve4e pi6a mnogo dobre na java, napisah vsi4ko za 2 4asa i trugna ot raz, yahuuaoaoaoaaauuuuu
public class Game {
	private static final String[] wordForGuesing = { "computer", "programmer",
			"software", "debugger", "compiler", "developer", "algorithm",
			"array", "method", "variable" };

	private String guesWord;
	private StringBuffer dashedWord;
	private FileReadWriter filerw;

    // Constructor, starting the game
	public Game(boolean autoStart) {
		guesWord = getRandWord();
		dashedWord = getW(guesWord);
		filerw = new FileReadWriter();
		if(autoStart) {
			displayMenu();
		}
	}


	private String getRandWord() {
		Random rand = new Random();
		String randWord = wordForGuesing[rand.nextInt(9)];
		return randWord;
	}
	public void displayMenu() {
		System.out.println("Welcome to �Hangman� game. Please, try to guess my secret word.\n"
				+ "Use 'TOP' to view the top scoreboard, 'RESTART' to start a new game,"
				+ "'HELP' to cheat and 'EXIT' to quit the game.");

		findLetterAndPrintIt();
	}

    // Method running the game.
	private void findLetterAndPrintIt() {
		boolean isHelpUsed = false;
		String letter = "";
		StringBuffer dashBuff = new StringBuffer(dashedWord);
		int mistakes = 0;

        // keep asking user for input until correct word is guessed
		do {
			System.out.println("The secret word is: " + printDashes(dashBuff));
			System.out.println("DEBUG " + guesWord);

            // take input from user until valid input is given
            do {
				System.out.println("Enter your gues(1 letter alowed): ");
				Scanner input = new Scanner(System.in);
				letter = input.next();

				if (letter.equals(Command.help.toString())) {
					isHelpUsed = true;

                    // moved the block that helps the user to a new method
					getHelp(dashBuff);

				}// end if

				menu(letter);

			} while (!letter.matches("[a-z]"));


            // check if input mathces any letters in secret word
			int counter = 0;
			for (int i = 0; i < guesWord.length(); i++) {
				String currentLetter = Character.toString(guesWord.charAt(i));
				if (letter.equals(currentLetter)) {
					++counter;
					dashBuff.setCharAt(i, letter.charAt(0));
				}
			}

			if (counter == 0) {
				++mistakes;

				System.out.printf(
						"Sorry! There are no unrevealed letters \'%s\'. \n",
						letter);
			} else {
				System.out.printf("Good job! You revealed %d letter(s).\n",
						counter);
			}

		} while (!dashBuff.toString().equals(guesWord));

        // print message to user when correct word is guessed
        gameDone(isHelpUsed, dashBuff, mistakes);


		// restart the game
		new Game(true);

	}// end method


    // When correct word is guessed, print message to user.
    // If no assistance is given, add user to scoreboard
    private void gameDone(boolean isHelpUsed, StringBuffer dashBuff, int mistakes){
        if (isHelpUsed == false) {
            System.out.println("You won with " + mistakes + " mistake(s).");
            System.out.println("The secret word is: " + printDashes(dashBuff));

            System.out.println("Please enter your name for the top scoreboard:");
            Scanner input = new Scanner(System.in);
            String playerName = input.next();

            filerw.openFileToWite();
            filerw.addRecords(mistakes, playerName);
            filerw.closeFileFromWriting();
            filerw.openFiletoRead();
            filerw.readRecords();
            filerw.closeFileFromReading();
            filerw.printAndSortScoreBoard();

        } else {
            System.out
                    .println("You won with "
                            + mistakes
                            + " mistake(s). but you have cheated. You are not allowed to enter into the scoreboard.");
            System.out.println("The secret word is: " + printDashes(dashBuff));
        }
    }

    // if user input is 'help' reveal one letter in the secret word
	private void getHelp(StringBuffer dashBuff){
		int i = 0, j = 0;
		while (j < 1) {
			if (dashBuff.charAt(i) == '_') {
				dashBuff.setCharAt(i, guesWord.charAt(i));
				++j;
			}
			++i;
		}
		System.out.println("The secret word is: "
				+ printDashes(dashBuff));
	}

    // perform menu-action
	private void menu(String letter) {
		if (letter.equals(Command.restart.toString())) {
			new Game(true);
		} else if (letter.equals(Command.top.toString())) {
			filerw.openFiletoRead();
			filerw.readRecords();
			filerw.closeFileFromReading();
			filerw.printAndSortScoreBoard();
			new Game(true);
		} else if (letter.equals(Command.exit.toString())) {
			System.exit(1);
		}
	}


    // create a Stringbuffer with same number of characters as the secret word
    // "fill" all positions with dashes
	private StringBuffer getW(String word) {
		StringBuffer dashes = new StringBuffer("");
		for (int i = 0; i < word.length(); i++) {
			dashes.append("_");
		}
		return dashes;
	}

	// print the guessed version of the secret word.
	// @param word a stringbuffer containing dashes and guessed letters
	private String printDashes(StringBuffer word) {
		String toDashes = "";

		for (int i = 0; i < word.length(); i++) {
			toDashes += (" " + word.charAt(i));
		}
		return toDashes;
	}
}
