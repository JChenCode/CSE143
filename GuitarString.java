// James Chen
// CSE 143 AE with Alex Johnson
// Homework 2
// Hunter Schafer
// GuitarString class is used to model a vibrating guitar string of a given frequency
//    and will use a ring buffer to keep track of the displacement values of that string.

import java.util.*;

public class GuitarString {
   
   // class constant for the decay factor of the Karplus Strong update
   public static final double DECAY_FACTOR = .996;
   
   private Queue<Double> ringBuffer; // ring buffer to store the displacement values of string
   private int capacity; // total values allowed to be stored when given the frequency
   
   // this method constucts a guitar string with a given frequency
   // creates a ring buffer that holds a certain capacity of values
   // Precondition: Frequency should be greater than 0 and the amount of values in the
   //    ring buffer should be greater than or equal to 2 (throws IllegalArgumentException if not)
   // Parameters:
   // double frequency - frequency given by the client to construct the guitar string
   public GuitarString(double frequency) {
      if(frequency <= 0 || Math.round(StdAudio.SAMPLE_RATE / frequency) < 2) {
         throw new IllegalArgumentException("Invalid frequency/size of ring buffer");
      }
      ringBuffer = new LinkedList<Double>();
      // capacity is the sample rate divided by frequency rounded to nearest integer
      capacity = (int) Math.round(StdAudio.SAMPLE_RATE / frequency);
      // intializes all values of the ring buffer to 0.0
      for(int i = 0; i < capacity; i++) {
         ringBuffer.add(0.0);
      }
   }
   
   // this method constructs a guitar string as well but is instead given values to put into
   //    the ring buffer instead of constructing it to a certain capacity using the frequency
   // Note: this constructor is used for testing only for testing purposes
   // Precondition: client must pass more than two values (throws IllegalArgumentException if not)
   // Parameters:
   // double[] init - values passed by client to be put into ring buffer
   public GuitarString(double[] init) {
      if(init.length < 2) {
         throw new IllegalArgumentException("Invalid number of elements");
      }
      ringBuffer = new LinkedList<Double>();
      for(int i = 0; i < init.length; i++) {
         ringBuffer.add(init[i]);
      }
   }
   
   // this method replaces the elements in the ring buffer with random values between
   //    -.5 (inclusive) and +.5 (exclusive) up to the the capacity of the ring buffer
   public void pluck() {
      for(int i = 0; i < capacity; i++) {
         ringBuffer.remove();
         ringBuffer.add(Math.random() - .5);
      }
   }
   
   // this method applies the Karplus-Strong update once to the ring buffer
   public void tic() {
      // stored the updated value into variable to make code clearer instead of adding it directly
      // removes the first value from the ring buffer, peeks to next value to find average of two
      double update = ((ringBuffer.remove() + ringBuffer.peek()) / 2) * (DECAY_FACTOR);
      ringBuffer.add(update);
   }
   
   // this method returns the next value/sample (front/first) of the ring buffer
   public double sample() {
      return ringBuffer.peek();
   }
   
}