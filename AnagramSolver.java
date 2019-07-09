// James Chen
// CSE 143 AE with Alex Johnson
// Homework 6
// Hunter Schafer
// The AnagramSolver class uses a dictionary to final all combos of words with the same letters
//    as a given phrase/text

import java.util.*;

public class AnagramSolver {
   
   private Map<String, LetterInventory> dictInventory; // holds dictionary phrases and inventory
                                                       //   on letters corresponding to phrase
   private List<String> dict; // dictionary holding phrases
   
   // this method constructs a structure that holds the phrase from the dictionary and also
   //    preprocesses the inventory of that phrase into the structure as well
   // Parameter: List<String> dictionary - list of phrases/texts given
   //    NOTE: dictionary is nonempty and contains no duplicates
   public AnagramSolver(List<String> dictionary) {
      dict = dictionary;
      dictInventory = new HashMap<>();
      for(String phrase : dict) {
         LetterInventory inventory = new LetterInventory(phrase);
         dictInventory.put(phrase, inventory);
      }
   }
   
   // this method looks at the dictionary to create a new dictionary without the non-possiblities
   //    of anagrams to speed up the process, also creates an inventory of the letters for the
   //    given text to find anagrams of within the dictionary
   // Precondition: max must be greater than 0 (throw IllegalArgumentException if not)
   // Parameters:
   // String text - phrase to be analyzed for anagrams within the dictionary
   // int max - max number of words tha the anagram can possess
   //    NOTE: if max = 0, find all possiblities, if non-zero, find up to max amt of words
   public void print(String text, int max) {
      if(max < 0) {
         throw new IllegalArgumentException("");
      }
      LetterInventory inventory = new LetterInventory(text);
      List<String> newDictionary = new ArrayList<>();
      for(int i = 0; i < dict.size(); i++) {
         String key = dict.get(i);
         if(inventory.subtract(dictInventory.get(key)) != null) {
            newDictionary.add(key);
         }
      }
      if(inventory.isEmpty()) {
         // since the dictionary is always nonempty, that means the phrase passed in does not contain letters
         //    and no anagrams can break created printing an empty list
         System.out.println("[]");
      } else {
         print(newDictionary, inventory, max, new ArrayList<String>());
      }
   }
   
   // helper method to do the finding of combinations of words that have the same letters
   //    of the given text/phrase and outputs all combinations of words from dictionary that
   //    are anagrams and at most max words
   // Precondition: max must be greater than 0 (throw IllegalArgumentException if not)
   //    NOTE: thrown in the public pair to this method so no need to throw here
   // Parameters:
   // List<String> newDictionary - updated dictionary of words with all non-possibilities pruned
   // LetterInventory phrase - inventory of the letters within the given phrase to find anagrams
   // int max - max number of words the anagram can possess
   //    NOTE: AGAIN if max = 0, show all possibilities, if not, show up to max number of words
   // List<String answer - holds all possible words that can make up the anagram to be outputted
   private void print(List<String> newDictionary, LetterInventory phrase, int max,
                        List<String> answer) {
      for(String key : newDictionary) {
         LetterInventory result = phrase.subtract(dictInventory.get(key));
         // prevent null pointer exceptions when recursing
         if(result != null) {
            // need to do choose/unchoose here bc it stops one word early due to the for each loop
            //    EX: If on last element, wont go through again to print 
            if(result.isEmpty()) {
               answer.add(key);
               // up to max words or show all possiblities
               if(max >= answer.size() || max == 0) {
                  System.out.println(answer);
               }
               answer.remove(key);
            } else {
               answer.add(key); // choose
               print(newDictionary, result, max, answer); // explore
               answer.remove(key); // unchoose
            }
         }
      }
   }
   
}