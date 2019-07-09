// James Chen
// CSE 143 AE with Alex Johnson
// Homework 4
// Hunter Schafer
// The HangmanManager class cheats. It manages a game of hangman but will delay picking a word
//    until it is forced to instead of picking it at the beginning. It will be limited to words
//    given to it through a predetermined dictionary. Will take out all the words of the guessed
//    letter until it cannot anymore, in which case it will show the letters as it cannot lie.

import java.util.*;

public class HangmanManager {
   
   private Set<String> wordSet; // contains possible words from the dictionary w/ correct length
   private Set<Character> guessedLetters; // letters that have been guessed already
   private int guessesRemaining; // number of guesses remaining
   private String pattern; // pattern of word to be displayed for hangman game
   
   // this method takes a dictionary of words, a certain word length, and max number of
   //    guesses to start the hangman game. It will then gather all the words from the
   //    dictionary of the desired length initially, eliminating duplicates.
   // Precondition: Length cannot be less than 1, max cannot be less than 0
   //    (throws IllegalArgumentException if it does not satisfiy)
   // Parameters:
   // Collection<String> dictionary - dictionary of words to construct the game
   // int length - length of words to be used for the game
   // int max - max number of guesses given to the player
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      if(length < 1 || max < 0) {
         throw new IllegalArgumentException("Please enter valid length/max");
      }
      
      wordSet = new TreeSet<>();
      guessedLetters = new TreeSet<>();
      guessesRemaining = max;
      
      // puts all words of desired length into a set from dictionary
      for(String word : dictionary) {
         if(word.length() == length) {
            wordSet.add(word);
         }
      }
      
      // pattern intially consists of all dashes
      pattern = "-";
      for(int i = 1; i < length; i++) {
         pattern += " -";
      }
   }
   
   // this method returns access to current words being considered by Hangman Manager
   public Set<String> words() {
      return wordSet;
   }
   
   // this method returns how many guesses the player has left
   public int guessesLeft() {
      return guessesRemaining;
   }
   
   // this method returns letters that have been guessed by the user
   public Set<Character> guesses() {
      return guessedLetters;
   }
   
   // returns the current pattern of the word to be displayed in the game
   //    takes into account the letters that have been already guessed
   //    NOTE: letters not guessed display as dashes, otherwise displayed as letters
   //          no leading/trailing spaces displayed
   // Precondition: has to be words in the game to find pattern of
   //    throws IllegalStateException if not
   public String pattern() {
      if(wordSet.isEmpty()) {
         throw new IllegalStateException("No more words.");
      }
      return pattern;
   }
   
   // this method takes into account the next guess
   // also decides which set of words to use going forward as the game does not
   //    choose a word until it is forced to (NOTE: chooses most words possible)
   //    also creates a new pattern to display for the new words and returns number of occurrences
   //    of the guess within the pattern. updates the number of guesses left
   // Precondition: number of guesses left has to be 1 or greater, set of words cannot be empty
   //    (throws IllegalStateException if not) if this is not thrown, will throw
   //    IllegalArgumentException if guess was already guessed
   // Parameter:
   // char guess - next guess made by the player of the game
   public int record(char guess) {
      if(guessesRemaining < 1 || wordSet.isEmpty()) {
         throw new IllegalStateException("Not enough guesses/word set is empty");
      } 
      if(guessedLetters.contains(guess)) {
         throw new IllegalArgumentException("Already guessed.");
      }
      
      guessedLetters.add(guess);
      Map<String, Set<String>> letterPattern = new TreeMap<>();
      
      //creates pattern for each word and adds pattern/word to map
      for(String word : wordSet) {
         String result = createPattern(word, guess);
         if(!(letterPattern.containsKey(result))) {
            letterPattern.put(result, new TreeSet<>());
         }
         letterPattern.get(result).add(word);
      }
      String newWordSetKey = getNewSetPattern(letterPattern);
      int occurrences = countOccurences(newWordSetKey, guess);
      return occurrences;
   }
   
   // helper method to count occurrences of the guess within the existing pattern of word
   //    updates the pattern as well. 
   // Parameter:
   // String newWordSetKey - new pattern for set of words to be used in the game
   // char guess - guess made by the player
   private int countOccurences(String newWordSetKey, char guess) {
      int occurrences = 0;
      for(int j = 0; j < newWordSetKey.length(); j++) {
         if(newWordSetKey.charAt(j) == guess) {
            occurrences++;
            // updates the new pattern with the guesses accounted for (replaces dash with guess)
            // NOTE: haven't been guessed (dashes)
            pattern = pattern.substring(0, j) + guess + pattern.substring(j + 1);
         }
      }
      if(occurrences == 0) {
         guessesRemaining--;
      }
      return occurrences;
   }
   
   // helper method to get the new words to be used in the game and the pattern for those words
   // Parameter:
   // Map<String, Set<String>> letterPattern - patterns matched with words of that pattern
   private String getNewSetPattern(Map<String, Set<String>> letterPattern) {
      int size = 0;
      String newWordSetKey = "";
      for(String keyCheck : letterPattern.keySet()) {
         // finds the pattern with greatest set of words
         int keySize = letterPattern.get(keyCheck).size();
         if(keySize > size) {
            size = keySize;
            newWordSetKey = keyCheck; // new pattern
            wordSet = letterPattern.get(newWordSetKey); // updates the new set of words
         }
      }
      return newWordSetKey;
   }
   
   // helper method to create a pattern for any word
   // NOTE: dashes are letters that have not been guessed yet
   // Parameter:
   // String word - word to be turned into a pattern
   // char guess - guess made by the player of the game
   private String createPattern(String word, char guess) {
      String result = "";
      for(int i = 0; i < word.length(); i++) {
         if(word.charAt(i) == guess) {
            result += guess + " ";
         } else {
            result += "- ";
         }
      }
      // accounts for trailing spaces
      // NOTE: trying to fencepost would be redundant since you have to figure out first character
      return result.substring(0, result.length() - 1);
   }
}