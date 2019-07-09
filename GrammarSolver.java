// James Chen
// CSE 143 AE with Alex Johnson
// Homework 5
// Hunter Schafer
// The GrammarSolver class takes in a line of grammar in Backus-Naur Form (BNF) and allows
//    the user to randomly generate elements of that grammar forming a sentence.

import java.util.*;

public class GrammarSolver {
   
   private Map<String, List<String[]>> grammarTracker; // Tracks the symbols (keys) and rules
   
   // this method separates the symbols and rules so that we can later generate the parts
   //    of the grammar
   // Precondition: grammar cannot be empty, cannot be duplicates of nonterminal symbols in
   //    grammar (throws IllegalArgumentException if not satisfied)
   // Parameters:
   // List<String> grammar - set of grammar with possible symbols and rules (cannot be changed)
   public GrammarSolver(List<String> grammar) {
      if(grammar.isEmpty()) {
         throw new IllegalArgumentException("No elements.");
      }
      
      grammarTracker = new TreeMap<>();
      
      for(String grammarLine : grammar) {
         // ex: <adjp>::=<adj>|<adj> <adjp>
         String[] splitGrammar = grammarLine.split("::="); // ex: [<adjp>, <adj>|<adj> <adjp>]
         
         // avoid problems with splitting strings leading with white space
         // you know that the nonterminal (at index 0) has no white space
         splitGrammar[1] = splitGrammar[1].trim();
         String key = splitGrammar[0];
         if(grammarTracker.containsKey(key)) {
            throw new IllegalArgumentException("Duplicate Nonterminals.");
         }
         
         String[] splitRule = splitGrammar[1].split("\\|"); // ex: [<adj>, <adj> <adjp>]
         List<String[]> rules = new ArrayList<>();
         for(String rule : splitRule) {
            // split individual rules into different arrays of strings
            // ex: rules contains [<adj>] [<adj>, <adjp>], each rule separated
            rule = rule.trim(); // avoid problems with splitting Strings leading with white spaces
            String[] singleValue = rule.split("\\s+");
            rules.add(singleValue);
         }
         grammarTracker.put(key, rules);
      }
      
   }
   
   // this method returns if symbol is nonterminal or not (yes - true, no - false)
   // Parameter:
   // String symbol - symbol to be determined if nonterminal or terminal
   public boolean grammarContains(String symbol) {
      return grammarTracker.containsKey(symbol);
   }
   
   // this method returns a representation of the nonterminal symbols from the grammar
   // format: sorted (alphabetically), ex: [<np>, <s>, <vp>]
   public String getSymbols() {
      return grammarTracker.keySet().toString();
   }
   
   // this method generates and returns a certain number of elements/sentences for a given symbol
   // Precondition: grammar must contain give nonterminal symbol, number of times must be
   //    0 or greater (throws IllegalArgumentException if not satisfied)
   // Parameters:
   // String symbol - nonterminal symbol for elements/sentences to be produced from
   // int times - number of elements/sentences to produce
   public String[] generate(String symbol, int times) {
      if(times < 0 || !(grammarTracker.containsKey(symbol))) {
         throw new IllegalArgumentException("Invalid input.");
      }
      String[] elements = new String[times];
      for(int i = 0; i < times; i++) {
         // number of sentences that are generated
         elements[i] = generate(symbol);
      }
      return elements;
   }
   
   // method helps with generating and returning the actual element/sentence from the given symbol
   // NOTE: each rule that is paired with the symbol has equal probablity of being chosen
   //    to choose what elements are added to the sentence
   // Parameter:
   // String symbol - nonterminal symbol for elements/sentences to be produced from
   private String generate(String symbol) {
      String resultSentence = "";
      Random r = new Random();
      if(!(grammarTracker.containsKey(symbol))) {
         return symbol; // if given a terminal symbol, return that symbol
      } else {
         List<String[]> rules = grammarTracker.get(symbol); // rules associated with symbol
         int randomNumber = r.nextInt(rules.size());
         for(String nextSymbol : rules.get(randomNumber)) {
            // keeps going through nonterminal symbols, recursively until it reaches a terminal
            resultSentence += generate(nextSymbol) + " ";
         }
      }
      resultSentence = resultSentence.trim(); // eliminates unneeded leading/trailing whitespaces
      return resultSentence;
   }
}