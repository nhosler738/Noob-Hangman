/**
 * Simple hangman game made from zero previous knowledge.
 * 'hangman' class runs the entire game and the functions 
 * defined in this file. 
 * 
 * Game premise: 
 *  1. Random word is picked from the 'wordlist.txt' file 
 *     and draws a line for each letter. 
 *  2. The player now guess letters one by 
 *     one.
 *  3. If a letter is correct, the class fills in all the blanks 
 *     where that letter exists. Player can guess other letters afterwards
 *     if the word hasn't been fully guessed yet. 
 *  4. If the guess is incorrect, the class draws one portion of the 
 *     hangman. (Head => arms => torso => legs => game over)
 *  5. Finally, if the player finds all the letters to complete the full 
 *     word before the hangman drawing is complete: the player wins. 
 * 
 * Side Notes:
 *  - If the player guesses enough letters to know what the word is, the players
 *    can attempt to guess the word in full. If a full word guess is incorrect
 *    another part of the hangman is drawn.  
 */

// Libraries for handling word picking 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;




public class hangman {

    // tracks players game state 
    private int playerIncorrectGuesses;
    private String currentHangman;
    private String chosenWord;
    private Scanner userInput; 

    private static final String[] GAME_COMMANDS = {
        "exit",
        "help",
        "restart",
        "full-guess"
    };

    private static final String GAME_TITLE = "$$\\   $$\\                                                                 $$\\ \n" + //
        "$$ |  $$ |                                                                $$ |\n" + //
        "$$ |  $$ | $$$$$$\\  $$$$$$$\\   $$$$$$\\  $$$$$$\\$$$$\\   $$$$$$\\  $$$$$$$\\  $$ |\n" + //
        "$$$$$$$$ | \\____$$\\ $$  __$$\\ $$  __$$\\ $$  _$$  _$$\\  \\____$$\\ $$  __$$\\ $$ |\n" + //
        "$$  __$$ | $$$$$$$ |$$ |  $$ |$$ /  $$ |$$ / $$ / $$ | $$$$$$$ |$$ |  $$ |\\__|\n" + //
        "$$ |  $$ |$$  __$$ |$$ |  $$ |$$ |  $$ |$$ | $$ | $$ |$$  __$$ |$$ |  $$ |    \n" + //
        "$$ |  $$ |\\$$$$$$$ |$$ |  $$ |\\$$$$$$$ |$$ | $$ | $$ |\\$$$$$$$ |$$ |  $$ |$$\\ \n" + //
        "\\__|  \\__| \\_______|\\__|  \\__| \\____$$ |\\__| \\__| \\__| \\_______|\\__|  \\__|\\__|\n" + //
        "                              $$\\   $$ |                                      \n" + //
        "                              \\$$$$$$  |                                      \n" + //
        "                               \\______/      ";



    public static void main(String[] args) {

        hangman newHangmanGame = new hangman();
        // welcome player and introduce game premise 
        newHangmanGame.welcomeInstructions();

        
        System.out.println("Press Enter to start game!");
        newHangmanGame.userInput.nextLine();
        
        

        // main game loop
        while(newHangmanGame.playerIncorrectGuesses != 6) {
            System.out.println(newHangmanGame.currentHangman);

        }
        
        
    }

    /**
     * Constructor to create initial game object.
     * Player starts with:
     *  - variable for keeping track of incorrect guesses (player lives)
     *  - initial hangman drawing on game start (just gallows)
     *  - random chosen word to guess 
     */
    public hangman() {
        this.playerIncorrectGuesses = 0;
        currentHangman = drawHangman(playerIncorrectGuesses); 
        this.chosenWord = pickWordRand();
        this.userInput = new Scanner(System.in);
    }


    /**
     * Reads through wordlist.txt, finds, and 
     * returns the word associates with the generated
     * random index number. 
     * @return value of chosenWord 
     */
    public String pickWordRand() {
        int randomIndex = randomIndex();
        String wordlistFilename = "wordlist.txt";
        int currentIndex = 0; // start at first word
        String wordChosen = "";

        try {
            File wordListObject = new File(wordlistFilename);
            Scanner wordListReader = new Scanner(wordListObject);
            while (wordListReader.hasNextLine()) {
                if (currentIndex != randomIndex) {
                    wordListReader.nextLine();
                    currentIndex++;
                } else {
                    wordChosen = wordListReader.nextLine();
                    break;
                }
            }
            wordListReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error occured at file read:");
            e.printStackTrace();
        }


        return wordChosen;
        
        
    }

    /**
     * Generates and returns a random integer between 0 and 50.
     * This function assumes the index range of 
     * the wordlist.txt file is 0 to 50. 
     * @return The value of randomIndex. 
     */
    public int randomIndex() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(50); // range: 0-50 
        return randomIndex;
    }



    public String drawHangman(int playerIncorrectGuesses) {
        String[] hangmanStages = {
            // Stage 0: Empty gallows
            "  +---+\n  |   |\n      |\n      |\n      |\n      ===\n",
            // Stage 1: Head
            "  +---+\n  |   |\n  O   |\n      |\n      |\n      ===\n",
            // Stage 2: Body
            "  +---+\n  |   |\n  O   |\n  |   |\n      |\n      ===\n",
            // Stage 3: Left Arm
            "  +---+\n  |   |\n  O   |\n /|   |\n      |\n      ===\n",
            // Stage 4: Right Arm
            "  +---+\n  |   |\n  O   |\n /|\\  |\n      |\n      ===\n",
            // Stage 5: Left Leg
            "  +---+\n  |   |\n  O   |\n /|\\  |\n /    |\n      ===\n",
            // Stage 6: Right Leg (Game Over)
            "  +---+\n  |   |\n  O   |\n /|\\  |\n / \\  |\n      ===\n"
        };

        return hangmanStages[playerIncorrectGuesses];

    }

    /**
     * Displays introduction messages 
     * for the player to familiarize 
     * themselves with the game premise 
     * and rules. Also gives the player 
     * an understanding on how to operate 
     * the program. 
     */
    public void welcomeInstructions() {

        String gallows = drawHangman(0);
        String welcomeMessage = String.format("""
                Welcome to Hangman!

                The premise of the game is to guess the correct 
                word randomly chosen by the game. You have 6 guesses to 
                uncover the word. Once you've made 6 incorrect guesses its 
                game over. 

                In the beginning, the chosen word will be 
                presented to you in a line of underscores (e.g. _ _ _ _).
                Your task is to fill out these underscores with characters
                of the word. There can me more than one of the same characters
                in a word, which can help fast-track your progress. 

                There will also be a gallows displayed as such:
                %s

                This is where the hangman will slowly appear 
                as you make incorrect guesses. Each incorrect guess
                results in one part of the hangman to display. Once
                the hangman is fully drawn, its game over. 

                Lastly, there are a few in-game commands you can run to 
                operate the game: 
                    1. Exit: exit the game 
                    2. Help: Displays this message as a quick reminder for the player 
                    3. Restart: Restart the game with a new random word 
                    4. Full-Guess: Allows you to guess the word in full (e.g. Guess = Banana instead of 'a')

                Enjoy the game!    
                """, gallows);

        System.out.println(GAME_TITLE);
        System.out.println(welcomeMessage);
        
    }



    
}
