package project;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/

import java.util.ArrayList;

public class JottTokenizer {
  private static final String WHITESPACE = " ";
  private static final String NEWLINE = "\n";
  private static final String COMMENT = "#";
  private static final String COMMA = ",";
  private static final String RBRACKET = "]";
  private static final String LBRACKET = "[";
  private static final String RBRACE = "}";
  private static final String LBRACE = "{";
  private static final String EQUALS = "=";
  private static final String LRARROW = "<>";
  private static final String MULTIPLY = "*";
  private static final String DIVIDE = "/";
  private static final String ADD = "+";
  private static final String SUBTRACT = "-";
  private static final String SEMICOLON = ";";
  private static final String DECIMAL = ".";
  private static final String COLON = ":";
  private static final String EXCLAMATION = "!";
  private static final String QUOTE = "\"";
  private static final String DIGIT_REGEX = "\\d";
  private static final String DIGITS_REGEX = "\\d+";
  private static final String LETTER_REGEX = "[A-Za-z]";
  private static final String LETTERS_REGEX = "[A-Za-z]+";

  /**
   * Takes in a filename and tokenizes that file into Tokens
   * based on the rules of the Jott Language
   * 
   * @param filename the name of the file to tokenize; can be relative or absolute
   *                 path
   * @return an ArrayList of Jott Tokens
   */
  public static ArrayList<Token> tokenize(String filename) {
    String input = readFile(filename);
    if (input == null)
      return null;

    ArrayList<Token> tokens = new ArrayList<>();
    int i = 0;
    int line = 1;

    while (i < input.length()) {
      // whitespace & newlines
      char c = input.charAt(i);
      if (isSpace(c)) {
        i++;
        continue;
      }
      if (isNewline(c)) {
        line++;
        i++;
        continue;
      }

      // comments (# to end of line)
      if (match(input, i, COMMENT)) {
        i = skipCommentToEOL(input, i);
        continue;
      }

      // strings
      if (match(input, i, QUOTE)) {
        Result r = scanString(input, i, line, filename);
        if (r == null)
          return null;
        tokens.add(r.token);
        i = r.nextIndex;
        line = r.nextLine;
        continue;
      }

      // identifiers & keywords (LETTER (LETTER|DIGIT|_)*)
      if (isLetter(c)) {
        Result r = scanIdentifier(input, i, line, filename);
        if (r == null)
          return null;
        tokens.add(r.token);
        i = r.nextIndex;
        line = r.nextLine;
        continue;
      }

      // numbers (DIGITS ( . DIGITS )?)
      if (isDigit(c)) {
        Result r = scanNumber(input, i, line, filename);
        if (r == null)
          return null;
        tokens.add(r.token);
        i = r.nextIndex;
        line = r.nextLine;
        continue;
      }

      // two-character tokens first (REL_OP & FC_HEADER)
      if (i + 1 < input.length()) {
        String two = input.substring(i, i + 2);
        if (two.equals(LRARROW) || two.equals("==") || two.equals("!=") || two.equals("<=") || two.equals(">=")) {
          tokens.add(makeToken(two, filename, line, TokenType.REL_OP));
          i += 2;
          continue;
        }
        if (two.equals("::")) {
          tokens.add(makeToken(two, filename, line, TokenType.FC_HEADER));
          i += 2;
          continue;
        }
      }

      // single-character tokens
      // brackets / braces / comma / semicolon / colon
      if (match(input, i, COMMA)) {
        tokens.add(makeToken(COMMA, filename, line, TokenType.COMMA));
        i++;
        continue;
      }
      if (match(input, i, LBRACKET)) {
        tokens.add(makeToken(LBRACKET, filename, line, TokenType.L_BRACKET));
        i++;
        continue;
      }
      if (match(input, i, RBRACKET)) {
        tokens.add(makeToken(RBRACKET, filename, line, TokenType.R_BRACKET));
        i++;
        continue;
      }
      if (match(input, i, LBRACE)) {
        tokens.add(makeToken(LBRACE, filename, line, TokenType.L_BRACE));
        i++;
        continue;
      }
      if (match(input, i, RBRACE)) {
        tokens.add(makeToken(RBRACE, filename, line, TokenType.R_BRACE));
        i++;
        continue;
      }
      if (match(input, i, SEMICOLON)) {
        tokens.add(makeToken(SEMICOLON, filename, line, TokenType.SEMICOLON));
        i++;
        continue;
      }
      if (match(input, i, COLON)) {
        tokens.add(makeToken(COLON, filename, line, TokenType.COLON));
        i++;
        continue;
      }

      // relational singletons < or >
      if (input.charAt(i) == '<' || input.charAt(i) == '>') {
        tokens.add(makeToken(String.valueOf(input.charAt(i)), filename, line, TokenType.REL_OP));
        i++;
        continue;
      }

      // assignment '='
      if (match(input, i, EQUALS)) {
        tokens.add(makeToken(EQUALS, filename, line, TokenType.ASSIGN));
        i++;
        continue;
      }

      // exclamation point must be followed by '=' to be valid
      if (match(input, i, EXCLAMATION)) {
        // if we got here, it was not handled as "!=" above; so it's an error.
        syntaxError("Invalid token \"!\". \"!\" expects following \"=\".", filename, line);
        return null;
      }

      // math ops
      if (match(input, i, ADD) || match(input, i, SUBTRACT) || match(input, i, MULTIPLY) || match(input, i, DIVIDE)) {
        tokens.add(makeToken(String.valueOf(input.charAt(i)), filename, line, TokenType.MATH_OP));
        i++;
        continue;
      }

      // decimal dot cannot stand alone
      if (match(input, i, DECIMAL)) {
        syntaxError("Invalid standalone decimal point.", filename, line);
        return null;
      }

      // nnknown character
      syntaxError("Invalid token \"" + input.charAt(i) + "\".", filename, line);
      return null;
    }

    return tokens;

  }

  private static boolean isSpace(char c) {
    return c == WHITESPACE.charAt(0) || c == '\t' || c == '\r';
  }

  private static boolean isNewline(char c) {
    return c == NEWLINE.charAt(0);
  }

  private static boolean match(String s, int i, String lit) {
    return i < s.length() && s.charAt(i) == lit.charAt(0);
  }

  private static boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  private static boolean isLetter(char c) {
    return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
  }

  private static boolean isIdChar(char c) {
    return isLetter(c) || isDigit(c) || c == '_';
  }

  private static int skipCommentToEOL(String input, int i) {
    // assumes input.charAt(i) == '#'
    while (i < input.length() && input.charAt(i) != '\n')
      i++;
    return i;
  }

  private static Result scanString(String input, int i, int line, String filename) {
    // assumes starting at opening quote
    int start = i;
    i++; // skip opening "
    StringBuilder sb = new StringBuilder();
    while (i < input.length()) {
      char c = input.charAt(i);
      if (c == NEWLINE.charAt(0)) {
        syntaxError("String literal cannot span lines.", filename, line);
        return null;
      }
      if (c == QUOTE.charAt(0)) { // closing "
        i++; // consume closing quote
        String lexeme = QUOTE + sb.toString() + QUOTE;
        return new Result(makeToken(lexeme, filename, line, TokenType.STRING), i, line);
      }
      sb.append(c);
      i++;
    }
    syntaxError("Unterminated string literal.", filename, line);
    return null;
  }

  private static Result scanIdentifier(String input, int i, int line, String filename) {
    int start = i;
    i++; // already consumed first letter
    while (i < input.length() && isIdChar(input.charAt(i)))
      i++;
    String lexeme = input.substring(start, i);
    return new Result(makeToken(lexeme, filename, line, TokenType.ID_KEYWORD), i, line);
  }

  private static Result scanNumber(String input, int i, int line, String filename) {
    int start = i;
    while (i < input.length() && isDigit(input.charAt(i)))
      i++;
    boolean sawDot = false;
    if (i < input.length() && input.charAt(i) == DECIMAL.charAt(0)) {
      // look ahead for at least one digit after dot
      if (i + 1 >= input.length() || !isDigit(input.charAt(i + 1))) {
        syntaxError("Invalid number: missing digits after decimal point.", filename, line);
        return null;
      }
      sawDot = true;
      i++; // consume dot
      while (i < input.length() && isDigit(input.charAt(i)))
        i++;
    }
    String lexeme = input.substring(start, i);
    return new Result(makeToken(lexeme, filename, line, TokenType.NUMBER), i, line);
  }

  private static Token makeToken(String lexeme, String filename, int line, TokenType type) {
    return new Token(lexeme, filename, line, type);
  }

  private static void syntaxError(String msg, String filename, int line) {
    System.err.print("Syntax Error: ");
    System.err.println(msg);
    System.err.println("\tFilepath: " + filename + ":" + line);
  }

  // Reads the file content into a String
  private static String readFile(String filename) {
    java.io.File file = new java.io.File(filename);
    try (java.io.FileReader fr = new java.io.FileReader(file);
        java.io.BufferedReader br = new java.io.BufferedReader(fr)) {
      StringBuilder sb = new StringBuilder();
      String line;
      boolean first = true;
      while ((line = br.readLine()) != null) {
        if (!first) sb.append('\n');
        sb.append(line);
        first = false;
      }
      return sb.toString();
    } catch (Exception e) {
      System.err.println("Error reading file: " + e.getMessage());
      return null;
    }
  }

  /* small struct for returning (token, nextIndex, nextLine) */
  private static class Result {
    final Token token;
    final int nextIndex;
    final int nextLine;

    Result(Token t, int idx, int line) {
      this.token = t;
      this.nextIndex = idx;
      this.nextLine = line;
    }
  }
}
