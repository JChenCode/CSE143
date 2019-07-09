// James Chen
// CSE 143 AE with Alex Johnson
// Homework 7
// Hunter Schafer
// The QuestionsGame class represents a slight different version of the game of twenty questions
// In this version, it will represent the computer's tree of yes/no questions and gradually adds
//    to the tree to make a smarter guess next time. Game is not limited to 20 questions

import java.io.*;
import java.util.*;

public class QuestionsGame {
   
   private Scanner console; // used to read in incut from the user
   private QuestionNode overallRoot; // used to build the tree of yes/no questions
   
   // this starts a new game with a single answer computer
   //    and build the tree off of that assumption
   public QuestionsGame() {
      overallRoot = new QuestionNode("computer");
      console = new Scanner(System.in);
   }
   
   // this method is used to replace the current tree that the computer has with another
   //    tree from a file.
   // NOTE: you can assume the file will be in standard format and tree will format correctly
   //    answers to questions will make sense
   // Parameters:
   // Scanner input - linked to the file that will replace the current tree
   public void read(Scanner input) {
      overallRoot = readHelper(input);
   }
   
   // this method is used to help replace the current tree that the computer has with another
   //    tree from a file
   // NOTE: you can assume the file will be in standard format and tree will format correctly
   //    answers to questions will make sense
   // Parameters:
   // Scanner input - linked to the file that will replace the current tree
   // Returns:
   // QuestionNode root - new root to be used to update overallRoot (tree)
   private QuestionNode readHelper(Scanner input) {
      String type = input.nextLine();
      String data = input.nextLine();
      QuestionNode root = new QuestionNode(data);
      if(input.hasNextLine() && type.contains("Q")) {
         root.left = readHelper(input);
         root.right = readHelper(input);
      }
      return root;
   }
   
   // this method stores the current yes/no questions tree in a file so that it can be used in
   //    a later game
   // will be stored in the same format that any file will be read in so that questions and 
   //    answers will make sense
   // Parameters:
   // PrintStream output - gives ability to export tree to the file
   public void write(PrintStream output) {
      writeHelper(overallRoot, output);
   }
   
   // this method helps stores the current yes/no questions tree in a file so that it can be
   //    used in a later game
   // will be stored in the same format that any file will be read in so that questions and 
   //    answers will make sense
   // Parameters:
   // PrintStream output - gives ability to export tree to the file
   // QuestionNode root - root to be exported to the file
   private void writeHelper(QuestionNode root, PrintStream output) {
      if(root.data.contains("?")) {
         output.println("Q:");
      } else {
         output.println("A:");
      }
      output.println(root.data);
      if(root.left != null) {
         writeHelper(root.left, output);
      }
      if(root.right != null) {
         writeHelper(root.right, output);
      }
   }
   
   // this method uses the current tree to play one complete guessing game with the user
   //    asking yes/no questions until reaching an answer to guess. Will tell user if they
   //    computer has won or ask for another hint at the answer
   public void askQuestions() {
      overallRoot = askQuestionsHelper(overallRoot);
   }
   
   // this method helps use the current tree to play one complete guessing game with the user
   //    asking yes/no questions until choosing an answer to guess. Will tell user if they
   //    computer has won or ask for another yes/no question and guess to add to tree
   //    so that it can make a smart guess next time
   // Parameters:
   // QuestionNode root - root to be used to update the tree of yes/no questions
   // Returns:
   // QuestionNode root - continously update the tree of yes/no questions
   private QuestionNode askQuestionsHelper(QuestionNode root) {
      boolean answer = true;
      String data = root.data; // used variable instead of root.data everytime
      if(data.contains("?")) {
         answer = yesTo(data);
      } else {
         answer = yesTo("Would your object happen to be " + data + "?");
      }
      
      if(answer && !data.contains("?")) {
         System.out.println("Great, I got it right!");
      } else if(answer) {
         root.left = askQuestionsHelper(root.left);
      } else if(!answer && !data.contains("?")) {
         root = newObjectHelper(root);
      } else {
         root.right = askQuestionsHelper(root.right);
      }
      return root;
   }
   
   // post: asks the user a question, forcing an answer of "y" or "n";
   //       returns true if the answer was yes, returns false otherwise
   // Parameters:
   // String prompt - prompt to ask for y/n answer for
   public boolean yesTo(String prompt) {
      System.out.print(prompt + " (y/n)? ");
      String response = console.nextLine().trim().toLowerCase();
      while (!response.equals("y") && !response.equals("n")) {
         System.out.println("Please answer y or n.");
         System.out.print(prompt + " (y/n)? ");
         response = console.nextLine().trim().toLowerCase();
      }
      return response.equals("y");
   }
   
   // this method helps create a new question with answers to add to
   //    the tree so it can make a smarter guess next time
   // Parameters:
   // QuestionNode root - node with previous question/answer to be attached to new question
   private QuestionNode newObjectHelper(QuestionNode root) {
      System.out.print("What is the name of your object? ");
      String object = console.nextLine();
      
      System.out.println("Please give me a yes/no question that");
      System.out.println("distinguishes between your object");
      System.out.print("and mine--> ");
      String question = console.nextLine();
      
      QuestionNode tempNode = new QuestionNode(question);
      
      if(yesTo("And what is the answer for your object?")) {
         tempNode.left = new QuestionNode(object);
         tempNode.right = root;
      } else {
         tempNode.left = root;
         tempNode.right = new QuestionNode(object);
      }
      return tempNode;
   }
   
   // this class constructs the structure of the tree branches to add to the tree
   private static class QuestionNode {
      public String data; // data stored at this node
      public QuestionNode left; // reference to left subtree
      public QuestionNode right; // reference to right subtree
      
      // Constructs a QuestionNode with the given data, initializes other fields to null
      // only constructor needed because, will always construct with data, null, null 
      // and change answers later if needed
      // Parameters:
      // String data - data to store in node 
      public QuestionNode(String data) {
         this.data = data;
         this.left = null;
         this.right = null;
      }
   }
}
