package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static java.lang.Integer.*;


public class EvilHangmanGame implements IEvilHangmanGame {
    static Set<String> wordDictionary = new HashSet<>();
    SortedSet<Character> guessedLetters = new TreeSet<>();


    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {

        wordDictionary.clear();

        try {
            Scanner scanner = new Scanner(dictionary);

            if (!dictionary.exists() || dictionary.length() == 0) {
                throw new EmptyDictionaryException();
            }
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (word.length() == wordLength) {
                    wordDictionary.add(word);
                }
                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {

        HashMap<String, Set<String>> Partitions = new HashMap<>();
        Character.toLowerCase(guess);


//        Partitions.clear();


        //checking to see if user has already guessed the letter
        String guessAsString = Character.toString(guess);
        if (guessedLetters.contains(guessAsString)) {
            throw new GuessAlreadyMadeException();
        }
        guessedLetters.add(guess);


        //get words in dictionary for each loop
        //nested loop, take each word, then each chcaracter
        //stringbuilder to build the key

        for (String word : wordDictionary) {

            StringBuilder SubsetKey = new StringBuilder();

            for (int i = 0; i < word.length(); i++) {  //go through each word, character at a time
                if (word.charAt(i) != guess) {       //check if the guess matches the character
                    SubsetKey.setCharAt(i, '?');

                }
                SubsetKey.setCharAt(i, guess);
            }

            //if the key already exists, we don't need a new one, just add to previous,
            if (Partitions.containsKey(SubsetKey.toString())) {
                Partitions.get(SubsetKey.toString()).add(word);
            } else {
                HashSet<String> correspondingWords = new HashSet<>();
                correspondingWords.add(SubsetKey.toString());

                Partitions.put(SubsetKey.toString(), correspondingWords);
            }
        }
        //tiebreakers
        //whichever hashset of corresponding words wins tiebreaker, make that wordDictionary


        //we want the largest of the groups
        String maxKey = null;
        int maxCount = 0;
        String tieBreaker = null;
        String finalMaxKey = new String();

        for (Map.Entry<String, Set<String>> entry : Partitions.entrySet()) {
            int count = entry.getValue().size();

            if (count > maxCount) {
                maxCount = count;
                maxKey = entry.getKey();
//                finalMaxKey = maxKey;
            }
        }



        final String rand2Key = maxKey;
        Partitions.keySet().removeIf(key -> !(Partitions.get(key).contains(rand2Key)));



        //tiebreakers if more than one largest set
        if (Partitions.keySet().size() > 1) {
            int letterCount = 0;

            if (!Partitions.containsValue(guess)) {
                Partitions.keySet().removeIf(key -> (Partitions.get(key).contains(guess)));
            }

            //get key with least amount of letters in value
            if (Partitions.containsValue(guess)) {

                char target = guess;
                int maxCounter = 0;
                String keyWithMaxCount = "";

                for (Map.Entry<String, Set<String>> entry : Partitions.entrySet()) {
                    int count = 0;
                    for (String value : entry.getValue()) {
                        for (char c : value.toCharArray()) {
                            if (c == target) {
                                count++;
                            }
                        }
                    }
                    if (count > maxCount) {
                        maxCounter = count;
                        keyWithMaxCount = entry.getKey();

                        final String randKey = keyWithMaxCount;
                        Partitions.keySet().removeIf(key -> (Partitions.get(key).contains(randKey)));
                    }
                }

                //get key with the rightmost guessed letter

                if (Partitions.containsValue(guess)) {
                    String storeKey = "";
                    for (Map.Entry<String, Set<String>> entry : Partitions.entrySet()) {
                        for (int i = entry.getKey().length() - 1; i >= 0; i--) {
                            if (entry.getKey().charAt(i) != storeKey.charAt(i)) {
                                if (storeKey.charAt(i) != target) {
                                    storeKey = entry.getKey();
                                }
                            }
                        }
                    }
                    final String bestKey = storeKey;
                    Partitions.keySet().removeIf(key -> (!Partitions.get(key).contains(bestKey)));

                }

            }


        }
        Map.Entry<String, Set<String>> entry = Partitions.entrySet().iterator().next();

        return entry.getValue();
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }
}
