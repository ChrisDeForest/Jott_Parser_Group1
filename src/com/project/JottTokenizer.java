package project;

import java.util.ArrayList;

public class JottTokenizer { // Tokenizes Jott source files into a list of Tokens
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

  // Reads file and returns a list of tokens
  public static ArrayList<Token> tokenize(String filename) {
    String input = readFile(filename);
    if (input == null) return null;

    ArrayList<Token> tokens = new ArrayList<>();
    int i = 0;
    int line = 1;

    while (i < input.length()) {
      Result r = matchAllTokens(input, i, line, filename); // match next token
      if (r == null) return null;
      i = r.nextIndex;
      line = r.nextLine;
      if (r.token != null) addToken(tokens, r.token); // add to list if a token was found
    }
    return tokens;
  }

  // Adds a token to the output list
  private static void addToken(ArrayList<Token> tokens, Token t) {
    tokens.add(t);
  }

  // Matches and returns the next token (or skips whitespace/comments)
  private static Result matchAllTokens(String input, int i, int line, String filename) {
    int n = input.length();
    while (i < n) {
      char c = input.charAt(i);
      if (isSpace(c)) { i++; continue; }
      if (isNewline(c)) { line++; i++; continue; }
      if (match(input, i, COMMENT)) { i = skipCommentToEOL(input, i); continue; }
      break;
    }
    if (i >= n) return new Result(null, i, line);

    char c = input.charAt(i);
    if (match(input, i, QUOTE)) return scanString(input, i, line, filename);
    if (isLetter(c)) return scanIdentifier(input, i, line, filename);
    if (isDigit(c) || (c == '.' && i + 1 < n && isDigit(input.charAt(i + 1)))) return scanNumber(input, i, line, filename);

    if (i + 1 < n) {
      String two = input.substring(i, i + 2);
      if (two.equals(LRARROW) || two.equals("==") || two.equals("!=") || two.equals("<=") || two.equals(">="))
        return new Result(makeToken(two, filename, line, TokenType.REL_OP), i + 2, line);
      if (two.equals("::"))
        return new Result(makeToken(two, filename, line, TokenType.FC_HEADER), i + 2, line);
    }

    if (match(input, i, COMMA)) return new Result(makeToken(COMMA, filename, line, TokenType.COMMA), i + 1, line);
    if (match(input, i, LBRACKET)) return new Result(makeToken(LBRACKET, filename, line, TokenType.L_BRACKET), i + 1, line);
    if (match(input, i, RBRACKET)) return new Result(makeToken(RBRACKET, filename, line, TokenType.R_BRACKET), i + 1, line);
    if (match(input, i, LBRACE)) return new Result(makeToken(LBRACE, filename, line, TokenType.L_BRACE), i + 1, line);
    if (match(input, i, RBRACE)) return new Result(makeToken(RBRACE, filename, line, TokenType.R_BRACE), i + 1, line);
    if (match(input, i, SEMICOLON)) return new Result(makeToken(SEMICOLON, filename, line, TokenType.SEMICOLON), i + 1, line);
    if (match(input, i, COLON)) return new Result(makeToken(COLON, filename, line, TokenType.COLON), i + 1, line);

    if (c == '<' || c == '>') return new Result(makeToken(String.valueOf(c), filename, line, TokenType.REL_OP), i + 1, line);
    if (match(input, i, EQUALS)) return new Result(makeToken(EQUALS, filename, line, TokenType.ASSIGN), i + 1, line);

    if (match(input, i, EXCLAMATION)) {
      syntaxError("Invalid token \"!\". \"!\" expects following \"=\".", filename, line);
      return null;
    }

    if (match(input, i, ADD) || match(input, i, SUBTRACT) || match(input, i, MULTIPLY) || match(input, i, DIVIDE))
      return new Result(makeToken(String.valueOf(c), filename, line, TokenType.MATH_OP), i + 1, line);

    if (match(input, i, DECIMAL)) {
      syntaxError("Invalid standalone decimal point.", filename, line);
      return null;
    }

    syntaxError("Invalid token \"" + c + "\".", filename, line);
    return null;
  }

  // Character checks
  private static boolean isSpace(char c) { return c == WHITESPACE.charAt(0) || c == '\t' || c == '\r'; }
  private static boolean isNewline(char c) { return c == NEWLINE.charAt(0); }
  private static boolean match(String s, int i, String lit) { return i < s.length() && s.charAt(i) == lit.charAt(0); }
  private static boolean isDigit(char c) { return c >= '0' && c <= '9'; }
  private static boolean isLetter(char c) { return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'); }
  private static boolean isIdChar(char c) { return isLetter(c) || isDigit(c) || c == '_'; }

  // Skip characters to end of line for a comment
  private static int skipCommentToEOL(String input, int i) {
    while (i < input.length() && input.charAt(i) != '\n') i++;
    return i;
  }

  // Scans a quoted string token
  private static Result scanString(String input, int i, int line, String filename) {
    i++;
    StringBuilder sb = new StringBuilder();
    while (i < input.length()) {
      char c = input.charAt(i);
      if (c == NEWLINE.charAt(0)) {
        syntaxError("String literal cannot span lines.", filename, line);
        return null;
      }
      if (c == QUOTE.charAt(0)) {
        i++;
        String lexeme = QUOTE + sb + QUOTE;
        return new Result(makeToken(lexeme, filename, line, TokenType.STRING), i, line);
      }
      sb.append(c);
      i++;
    }
    syntaxError("Unterminated string literal.", filename, line);
    return null;
  }

  // Scans an identifier or keyword
  private static Result scanIdentifier(String input, int i, int line, String filename) {
    int start = i;
    i++;
    while (i < input.length() && isIdChar(input.charAt(i))) i++;
    String lexeme = input.substring(start, i);
    return new Result(makeToken(lexeme, filename, line, TokenType.ID_KEYWORD), i, line);
  }

  // Scans a number token
  private static Result scanNumber(String input, int i, int line, String filename) {
    int start = i;
    if (input.charAt(i) == DECIMAL.charAt(0)) {
      i++;
      if (i >= input.length() || !isDigit(input.charAt(i))) {
        syntaxError("Invalid number: missing digits after decimal point.", filename, line);
        return null;
      }
      while (i < input.length() && isDigit(input.charAt(i))) i++;
    } else {
      while (i < input.length() && isDigit(input.charAt(i))) i++;
      if (i < input.length() && input.charAt(i) == DECIMAL.charAt(0)) {
        i++;
        while (i < input.length() && isDigit(input.charAt(i))) i++;
      }
    }
    String lexeme = input.substring(start, i);
    return new Result(makeToken(lexeme, filename, line, TokenType.NUMBER), i, line);
  }

  // Creates a token instance
  private static Token makeToken(String lexeme, String filename, int line, TokenType type) {
    return new Token(lexeme, filename, line, type);
  }

  // Prints a syntax error
  private static void syntaxError(String msg, String filename, int line) {
    System.err.print("Syntax Error: ");
    System.err.println(msg);
    System.err.println("\tFilepath: " + filename + ":" + line);
  }

  // Reads entire file into a string
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

  // Holds a token and position info after a scan
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
