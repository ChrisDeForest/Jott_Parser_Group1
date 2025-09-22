package provided;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // check if no file paths were given on the command line
        if (args.length == 0) {
            // print usage instructions and exit if no args
            System.err.println("Usage: java project.Main <path-to-.jott> [more .jott files]");
            System.exit(1);
        }

        // loop through each file path passed as an argument
        for (String path : args) {
            // print the file currently being tokenized
            System.out.println("=== " + path + " ===");
            // call the tokenizer to turn the file into tokens
            ArrayList<Token> toks = JottTokenizer.tokenize(path);

            // if tokenizer returns null then there was a syntax error
            if (toks == null) {
                System.out.println("(tokenize returned null due to a syntax error)");
            } else {
                // otherwise print each token in a readable format
                for (Token t : toks) {
                    System.out.printf(
                            "Token{lexeme='%s', type=%s, line=%d, file=%s}%n",
                            t.getToken(), t.getTokenType(), t.getLineNum(), t.getFilename());
                }

                // print the total number of tokens found
                System.out.println("Total tokens: " + toks.size());
            }

            // print a blank line between file outputs
            System.out.println();
        }
    }
}
