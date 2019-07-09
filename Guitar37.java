// James Chen
// CSE 143 AE with Alex Johnson
// Homework 2
// Hunter Schafer
// Guitar37 class keeps track of a musical instrument with mutliple strings, specifically a guitar
//    This class deals with a specific guitar that has 37 different notes/strings

public class Guitar37 implements Guitar {
   
   // class constant to store the keyboard layout of the notes
   public static final String KEYBOARD =
      "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
   
   private GuitarString[] strings; // stores the 37 different notes
   private int timeTracker; // tracks the number of tics that have gone by
   
   // constructs a location to store the 37 different notes/strings
   // constructs those 37 different strings and stores them as well
   public Guitar37() {
      strings = new GuitarString[37];
      timeTracker = 0;
      // uses equation to find the different frequencies of each note to create a string
      for(int i = 0; i < strings.length; i++) {
         strings[i] = new GuitarString(440 * Math.pow(2, (i - 24.0) / 12.0));
      }
   }
   
   // this method first checks to see if the note/pitch can be played (ignores it if it cannot)
   // this method also plays a note based on certain pitches
   public void playNote(int pitch) {
      if(!(pitch > 12 || pitch < -24)) {
         strings[pitch + 24].pluck();
      }
   }
   
   // this method returns if the key corresponds to any of the 37 notes before it can be played
   // Parameters:
   // char key - key that corresponds to a specific note to be played
   public boolean hasString(char key) {
      // indexOf will return -1 if key does not match up to any notes
      return KEYBOARD.indexOf(key) != -1;
   }
   
   // this method plays a certain note based on a corresponding key (different for each note)
   // makes sure the key matches up to a note first (throws IllegalArgumentException if not one 
   //    of 37 notes the instrument is able to play)
   // Parameters:
   // char key - key that corresponds to a specific note to be played
   public void pluck(char key) {
      if(hasString(key) == false) {
         throw new IllegalArgumentException("Bad key: " + key);
      }  
      strings[KEYBOARD.indexOf(key)].pluck();
   }
   
   // this method returns the current sound sample (sum of all the samples from the
   //    strings of the instrument)
   public double sample() {
      double totalSample = 0;
      // adds up the sample of all the strings in the instrument
      for(int i = 0; i < strings.length; i++) {
         totalSample += strings[i].sample();
      }
      return totalSample;
   }
   
   // this method advances time forward and progresses another tic
   public void tic() {
      timeTracker++;
      for(int i = 0; i < strings.length; i++) {
         strings[i].tic();
      }
   }
   
   // this method returns how much time/tics have gone by
   public int time() {
      return timeTracker;
   }
   
}