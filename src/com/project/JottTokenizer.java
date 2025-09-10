package project;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/

import java.util.ArrayList;

public class JottTokenizer {
  private static final String WHITESPACE = " ", NEWLINE = "\n", COMMENT = "#", COMMA = ",", RBRACKET = "]", LBRACKET = "[", RBRACE = "}", LBRACE = "{";
  private static final String EQUALS = "=", LRARROW = "<>", MULTIPLY = "*", DIVIDE = "/", ADD = "+", SUBTRACT = "-", SEMICOLON = ";", DECIMAL = ".";
  private static final String COLON = ":", EXCLAMATION = "!", QUOTE = "\"";
  private static final String DIGIT_REGEX = "\\d", DIGITS_REGEX = "\\d+", LETTER_REGEX = "[A-Za-z]", LETTERS_REGEX = "[A-Za-z]+";

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

