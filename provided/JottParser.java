package provided;

/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author
 */

import java.util.ArrayList;

import parser.ProgramNode;

public class JottParser {

    /**
     * Parses an ArrayList of Jotton tokens into a Jott Parse Tree.
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens){
      ProgramNode root = null;
      
      try {
        root = ProgramNode.parseProgramNode(tokens);
        return root;
      } catch (Exception e) {
        System.err.println("JottParser: Uncaught exception: " + e.getMessage());
        return null;
      }
    }
}
