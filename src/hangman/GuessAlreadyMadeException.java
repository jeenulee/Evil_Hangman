package hangman;

public class GuessAlreadyMadeException extends Exception {

    public void guessMade(){
        System.out.println("This guess has already been made");
    }
}
