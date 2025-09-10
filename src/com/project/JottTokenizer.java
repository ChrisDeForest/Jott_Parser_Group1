package project;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/

import java.util.ArrayList;

public class JottTokenizer {
  // constants do not include numbers or letters, that would just be dumb
  private static final String WHITESPACE = " ", NEWLINE = "\n", COMMENT = "#", COMMA = ",", RBRACKET = "]", LBRACKET = "[", RBRACE = "}", LBRACE = "{";
  private static final String EQUALS = "=", LRARROW = "<>", MULTIPLY = "*", DIVIDE = "/", ADD = "+", SUBTRACT = "-", SEMICOLON = ";", DECIMAL = ".";
  private static final String COLON = ":", EXCLAMATION = "!", QUOTE = "\"";

	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
  public static ArrayList<Token> tokenize(String filename){
		return null;
	}
}

// any error, report to System.err, return NULL
// make repetitive code into helper functions when possible

