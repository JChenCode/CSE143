// James Chen
// CSE 143 AE with Alex Johnson
// Homework 8
// Hunter Schafer
// The HuffmanCode class helps in compressing and decompressing the data attached to a file
//    by keeping track of a tree representing characters to a particular message using the
//    Huffman Algorithm

import java.io.*;
import java.util.*;

public class HuffmanCode {
   
   private HuffmanNode overallRoot; // overall tree of characters of the particular message
   
   // this method creates a new tree from the message using the Huffman Algorithm
   // Parameters:
   // int[] frequencies - contains the count of each ascii value
   public HuffmanCode(int[] frequencies) {
      // priority queue to build huffman code based on frequency (less coming first)
      Queue<HuffmanNode> freq = new PriorityQueue<>();
      for(int i = 0; i < frequencies.length; i++) {
         // only use ascii values of those with nonzero count
         if(frequencies[i] > 0) {
            freq.add(new HuffmanNode(i, frequencies[i]));
         }
      }
      // keep combining nodes until only one node consisting of all the nodes in a tree
      while(freq.size() != 1) {
         HuffmanNode temp1 = freq.remove();
         HuffmanNode temp2 = freq.remove();
         // -1 acts as placeholder since ascii value is irrelevant
         freq.add(new HuffmanNode(-1, temp1.frequency + temp2.frequency, temp1, temp2));
      }
      overallRoot = freq.remove();
   }
   
   // this creates a new tree of characters from previously created huffman code
   //    NOTE: file will be in legal and valid standard format
   // Parameters:
   // Scanner input - code needed to be converted to a tree
   public HuffmanCode(Scanner input) {
      while(input.hasNext()) {
         int asciiValue = Integer.parseInt(input.nextLine());
         String code = input.nextLine();
         overallRoot = constructHelper(code, asciiValue, overallRoot);
      }
   }
   
   // this method helps in constructing a new tree of characters from previously created code
   // Parameters:
   // String code - code needed to form layout of new tree
   // int value - ascii value of character to be stored
   // HuffmanNode root - tree being constructed
   // Returns:
   // returns a root that is the tree being constructed
   private HuffmanNode constructHelper(String code, int value, HuffmanNode root) {
      // NOTE: 0 and 1 acts as placeholder since some values are irrelevant
      if(code.length() != 0) {
         if(root == null) {
            root = new HuffmanNode(-1, 0);
         }
         if(code.charAt(0) == '0') {
            root.left = constructHelper(code.substring(1), value, root.left);
         } else {
            root.right = constructHelper(code.substring(1), value, root.right);
         }
      } else {
         root = new HuffmanNode(value, 0);
      }
      return root;
   }
   
   // this method stores the current huffman codes of the tree in a standard formatted file
   //    to give ability to be decompressed later
   // Parameters:
   // PrintStream output - file for code to be outputted to
   public void save(PrintStream output) {
      saveHelper(output, overallRoot, "");
   }
   
   // this method helps store current huffman codes of the tree in standard formatted file
   //    to give ability to be decompressed later
   // Parameters:
   // PrintStream output - file for code to be outputted to
   // HuffmanNode root - tree being constructed
   // String path - path through the tree to get to a specific character value
   public void saveHelper(PrintStream output, HuffmanNode root, String path) {
      // you know in the tree, that the left and right subtrees will always both be
      //    null or not null
      if(root.left != null && root.right != null) {
         // only need to remove after going left because you are exploring tree from left to 
         //    right (preorder)
         saveHelper(output, root.left, path += "0");
         path = path.substring(0, path.length() - 1);
         saveHelper(output, root.right, path += "1");
      }
      if(root.left == null && root.right == null) {
         output.println(root.asciiValue);
         output.println(path);
      }
   }
   
   // this method reads individual bits from a file to decipher the character values
   //    of the message that the bits correspond to based on tree
   // NOTE: The file will contain a valid sequence of bits matching structure
   // Parameters:
   // BitInputStream input - sequence of bits that correspond to a message
   // PrintStream output - file for character values to be outputted to
   public void translate(BitInputStream input, PrintStream output) {
      HuffmanNode temp = overallRoot;
      while(input.hasNextBit()) {
         while(temp.left != null && temp.right != null) {
            int bit = input.nextBit();
            if(bit == 0) {
               temp = temp.left;
            } else {
               temp = temp.right;
            }
         }
         output.write(temp.asciiValue);
         temp = overallRoot;
      }
   }
   
   // this class constructs branches that holds the character values to add to the tree
   private static class HuffmanNode implements Comparable<HuffmanNode> {
      public int asciiValue; // Ascii value/character that corresponds to this branch
      public int frequency; // frequency/count of this character
      public HuffmanNode left; // reference to left subtree
      public HuffmanNode right; // reference to right subtree
      
      // this constructs the actual pieces of data about the character to add to the tree
      // Parameters:
      // int asciiValue - character (based on ascii value) that corresponds with this branch
      // int frequency - count of this character/number of appearances
      // HuffmanNode left - reference to the left branch of this branch
      // HuffmanNode right - reference object connected to the right branch of this branch
      public HuffmanNode(int asciiValue, int frequency, HuffmanNode left, HuffmanNode right) {
         this.asciiValue = asciiValue;
         this.frequency = frequency;
         this.left = left;
         this.right = right;
      }
      
      // this constructs the actual pieces of data about the character to add to the tree
      // Parameters:
      // int asciiValue - character (based on ascii value) that corresponds with this branch
      // int frequency - count of this character/number of appearances
      public HuffmanNode(int asciiValue, int frequency) {
         this(asciiValue, frequency, null, null); // call other constructor with designated values
      }
      
      // this method compares the frequency of different data about the characters
      //    NOTE: positive number if this frequency is greater than other
      //       negative number if not
      //       if equal, it is the same
      public int compareTo(HuffmanNode other) {
         return this.frequency - other.frequency;
      }
   }
}