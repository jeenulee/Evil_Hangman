package hangman;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;


public class EvilHangman {

    public static void main(String[] args) throws EmptyDictionaryException, IOException {

        EvilHangmanGame o = new EvilHangmanGame();
        File dictionaryWords = new File(args[0]);
        int wordSize = parseInt(args[1]);
        int amountOfGuesses = parseInt(args[2]);





        Scanner object = new Scanner(System.in);
        while(amountOfGuesses != 0){



//            System.out.println("You have " + amountOfGuesses + " left");
//            System.out.println("Used Letters: " + o.getGuessedLetters());
//            System.out.println("Word: " + o);
//            System.out.println("Enter guess: ");
//            char guess = object.next().charAt(0);
//
//            o.makeGuess(guess);
//
//
//            amountOfGuesses--;



        }


    }



}
