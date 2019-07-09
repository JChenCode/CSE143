// James Chen
// CSE 143 AE with Alex Johnson
// Homework 1
// Hunter Schafer
// LetterInventory class is used to keep track of an inventory of letters of the alphabet for
//    any phrase that is given by the client

public class LetterInventory {
   
   public static final int INVENTORY_CAPACITY = 26; // constant for the size of the inventory
   
   private int[] inventory; // count of each letter in the alphabet within the phrase
   private int totalSize; // total alphabetical letters counted from the phrase
   
   // constructs an inventory to keep track of the count of alphabetical letters within the phrase
   // inventory has default capacity of the number of letters in the alphabet
   // ignores the case of letters and non-alphabetic characters
   // Parameters:
   // String data - phrase given by the client to be put into the letter inventory
   public LetterInventory(String data) {
      inventory = new int[INVENTORY_CAPACITY];
      totalSize = 0;
      data = data.toLowerCase();
      
      // constructs the inventory from the phrase that is given by the client
      for(int i = 0; i < data.length(); i++) {
         if(Character.isLetter(data.charAt(i))) {
            inventory[data.charAt(i) - 'a']++;
            totalSize++;
         }
      }
   }
   
   // returns the sum of all of the counts in this inventory
   public int size() {
      return totalSize;
   }
   
   // returns if the inventory is empty (if all counts equate to 0)
   public boolean isEmpty() {
      return totalSize == 0;
   }
   
   // returns a count of how many of this letter are in the inventory, case insensitive
   // Precondition: Should be an alphabetical letter (throws IllegalArgumentException if not)
   // Parameters:
   // char letter - letter to find the count of in the inventory
   public int get(char letter) {
      checkLetter(letter);
      return inventory[Character.toLowerCase(letter) - 'a'];
   }
   
   // Sets the count for the given letter to the given value, case insensitive
   // Precondition: Should be an alphabetical letter (throws IllegalArgumentException if not)
   // Parameters:
   // char letter - letter to set the count for
   // int value - value for the count of the letter to be set to
   public void set(char letter, int value) {
      if(value < 0) {
         throw new IllegalArgumentException("");
      }
      checkLetter(letter);
      totalSize -= inventory[Character.toLowerCase(letter) - 'a'] - value;
      inventory[Character.toLowerCase(letter) - 'a'] = value;
   }
   
   // checks if the character is a letter or not
   // throwing an IllegalArgumentException if not
   private void checkLetter(char letter) {
      if(!(Character.isLetter(letter))) {
         throw new IllegalArgumentException("Invalid Character: is nonalphabetical letter.");
      }
   }
   
   // adds the counts of each letter of two different LetterInventory objects
   //    to construct and return a new LetterInventory object
   // does not change the two old LetterInventory objects
   // Parameters:
   // LetterInventory other - LetterInventory object to be added to another LetterInventory object
   public LetterInventory add(LetterInventory other) {
      LetterInventory resultInventory = new LetterInventory("");
      for(int i = 0; i < INVENTORY_CAPACITY; i++) {
         resultInventory.inventory[i] = this.inventory[i] + other.inventory[i];
      }
      resultInventory.totalSize = this.size() + other.size();
      return resultInventory;
   }
   
   // subtracts the counts of each letter of two different LetterInventory objects
   //    to construct and return a new LetterInventory object
   // does not change the two old LetterInventory objects
   // Parameters:
   // LetterInventory other - LetterInventory object to be added to another LetterInventory object
   public LetterInventory subtract(LetterInventory other) {
      LetterInventory resultInventory = new LetterInventory("");
      for(int i = 0; i < INVENTORY_CAPACITY; i++) {
         if(this.inventory[i] - other.inventory[i] < 0) {
            return null;
         }
         resultInventory.inventory[i] = this.inventory[i] - other.inventory[i];
      }
      resultInventory.totalSize = this.size() - other.size();
      return resultInventory;
   }
   
   // returns a readible representation of the inventory with the letters all in lowercase,
   //    in sorted order, and surrounded by square brackets
   // number of occurrences of each letter will match count in the inventory
   // example return value: inventory of 1 a's and 2 b's is [abb]
   public String toString() {
      String result = "[";
      for(int i = 0; i < INVENTORY_CAPACITY; i++) {
         for(int j = 0; j < inventory[i]; j++) {
            result += (char) ('a' + i);
         }
      }
      result += "]";
      return result;
   }
}