/**
 * Simple hangman game made from zero previous knowledge.
 * 'hangman' class runs the entire game and the functions 
 * defined in this file. 
 * 
 * Game premise: 
 *  1. Random word is picked from the 'wordlist.txt' file.
 *  2. Player guesses letters one by one.
 *  3. Incorrect guesses draw the hangman.
 *  4. If player guesses all letters before hangman is complete: they win.
 */

// Libraries 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class hangMan {

    // Tracks players game state 
    private int playerIncorrectGuesses;
    private String currentHangman;
    private String chosenWord;
    private StringBuilder chosenWordHidden;
    private Scanner userInput; 

    private static final String[] GAME_COMMANDS = {
        "exit",
        "help",
        "restart"
    };

    private static final String GAME_TITLE = 
        "$$\\   $$\\                                                                 $$\\ \n" + 
        "$$ |  $$ |                                                                $$ |\n" +
        "$$ |  $$ | $$$$$$\\  $$$$$$$\\   $$$$$$\\  $$$$$$\\$$$$\\   $$$$$$\\  $$$$$$$\\  $$ |\n" +
        "$$$$$$$$ | \\____$$\\ $$  __$$\\ $$  __$$\\ $$  _$$  _$$\\  \\____$$\\ $$  __$$\\ $$ |\n" +
        "$$  __$$ | $$$$$$$ |$$ |  $$ |$$ /  $$ |$$ / $$ / $$ | $$$$$$$ |$$ |  $$ |\\__|\n" +
        "$$ |  $$ |$$  __$$ |$$ |  $$ |$$ |  $$ |$$ | $$ | $$ |$$  __$$ |$$ |  $$ |    \n" +
        "$$ |  $$ |\\$$$$$$$ |$$ |  $$ |\\$$$$$$$ |$$ | $$ | $$ |\\$$$$$$$ |$$ |  $$ |$$\\ \n" +
        "\\__|  \\__| \\_______|\\__|  \\__| \\____$$ |\\__| \\__| \\__| \\_______|\\__|  \\__|\\__|\n" +
        "                              $$\\   $$ |                                      \n" +
        "                              \\$$$$$$  |                                      \n" +
        "                               \\______/      ";

    public static void main(String[] args) {

        hangMan newHangmanGame = new hangMan();

        newHangmanGame.welcomeInstructions();
        System.out.println("Press Enter to start game!");
        newHangmanGame.userInput.nextLine();
    
        newHangmanGame.gameLoop();
    }

    /**
     * Constructor
     */
    public hangMan() {
        this.playerIncorrectGuesses = 0;
        this.currentHangman = drawHangman(playerIncorrectGuesses); 
        this.chosenWord = pickWordRand();
        this.chosenWordHidden = initChosenWordHidden(chosenWord);
        this.userInput = new Scanner(System.in);
    }

    /**
     * Return random word from wordlist.txt
     */
    public String pickWordRand() {
        int randomIndex = randomIndex();
        String wordlistFilename = "wordlist.txt";
        int currentIndex = 0;
        String wordChosen = "";

        try {
            File wordListObject = new File(wordlistFilename);
            Scanner wordListReader = new Scanner(wordListObject);
            while (wordListReader.hasNextLine()) {
                if (currentIndex != randomIndex) {
                    wordListReader.nextLine();
                    currentIndex++;
                } else {
                    wordChosen = wordListReader.nextLine().trim().toLowerCase();
                    break;
                }
            }
            wordListReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error reading file:");
            e.printStackTrace();
        }

        return wordChosen;
    }

    /**
     * Random number between 0 and 50
     */
    public int randomIndex() {
        return new Random().nextInt(50);
    }

    /**
     * Hangman display stages
     */
    public String drawHangman(int playerIncorrectGuesses) {
        String[] stages = {
            "  +---+\n  |   |\n      |\n      |\n      |\n      ===\n",
            "  +---+\n  |   |\n  O   |\n      |\n      |\n      ===\n",
            "  +---+\n  |   |\n  O   |\n  |   |\n      |\n      ===\n",
            "  +---+\n  |   |\n  O   |\n /|   |\n      |\n      ===\n",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n      |\n      ===\n",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n /    |\n      ===\n",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n / \\  |\n      ===\n"
        };

        return stages[playerIncorrectGuesses];
    }

    /**
     * Main game loop
     */
    public void gameLoop() {

        while (playerIncorrectGuesses < 6) {

            System.out.println("\n" + currentHangman);
            System.out.println("Word: " + spaced(chosenWordHidden.toString()));
            System.out.println("Enter guess or command: ");

            String input = userInput.nextLine().trim().toLowerCase();

            // COMMAND HANDLING FIXED HERE
            if (isCommand(input)) {
                handleCommand(input);
                continue;
            }

            if (input.length() != 1) {
                System.out.println("Enter one letter at a time.");
                continue;
            }

            char letter = input.charAt(0);

            if (!Character.isLetter(letter)) {
                System.out.println("Only letters allowed.");
                continue;
            }

            handleLetterGuess(letter);

            if (chosenWordHidden.toString().equals(chosenWord)) {
                System.out.println("You guessed the word: " + chosenWord);
                return;
            }
        }

        System.out.println("\n" + drawHangman(6));
        System.out.println("Game over - The word was: " + chosenWord);
    }

    /**
     * Handles a single letter guess
     */
    public void handleLetterGuess(char guessedLetter) {
        if (chosenWord.indexOf(guessedLetter) < 0) {
            System.out.println("Incorrect");
            updatePlayerIncorrectGuesses();
            updateHangman(playerIncorrectGuesses);
            return;
        }

        System.out.println("Correct");
        updateChosenWordHiddenChar(guessedLetter);
    }

    public String spaced(String s) {
        return s.replace("", " ").trim();
    }

    /**
     * Intro message
     */
    public void welcomeInstructions() {

        String gallows = drawHangman(0);
        String welcomeMessage = String.format("""
                Welcome to Hangman!

                The premise of the game is to guess the correct 
                word randomly chosen by the game. You have 6 guesses to 
                uncover the word.

                In the beginning, the chosen word will be 
                presented to you as underscores (e.g. _ _ _ _).

                There will also be a gallows displayed as such:
                %s

                Commands: exit, help, restart

                Enjoy the game!
                """, gallows);

        System.out.println(GAME_TITLE);
        System.out.println(welcomeMessage);
        
    }

    /**
     * Create hidden word (____)
     */
    public StringBuilder initChosenWordHidden(String chosenWord) {
        StringBuilder hidden = new StringBuilder();
        for (int i = 0; i < chosenWord.length(); i++) {
            hidden.append('_');
        }
        return hidden;
    }

    public void updateChosenWordHiddenChar(char guessed) {
        for (int i = 0; i < chosenWord.length(); i++) {
            if (chosenWord.charAt(i) == guessed) {
                chosenWordHidden.setCharAt(i, guessed);
            }
        }
    }

    /**
     * Command check
     */
    public boolean isCommand(String inputCmd) {
        for (String cmd : GAME_COMMANDS) {
            if (cmd.equals(inputCmd)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Command handler
     */
    public void handleCommand(String cmd) {
        switch (cmd) {
            case "exit":
                System.out.println("Exiting game...");
                System.exit(0);
                break;

            case "help":
                welcomeInstructions();
                break;

            case "restart":
                restartGame();
                break;
        }
    }

    /**
     * Restart game
     */
    public void restartGame() {
        System.out.println("Restarting game...");
        this.playerIncorrectGuesses = 0;
        this.chosenWord = pickWordRand();
        this.chosenWordHidden = initChosenWordHidden(chosenWord);
        this.currentHangman = drawHangman(0);
    }

    

    /**
     * Lose reveal
     */
    public void revealFullWord() {
        chosenWordHidden = new StringBuilder(chosenWord);
        System.out.println("The word was: " + chosenWord);
    }

    // Getters/setters
    public void updateHangman(int g) {
        this.currentHangman = drawHangman(g);
    }

    public void updatePlayerIncorrectGuesses() {
        this.playerIncorrectGuesses++;
    }
}

