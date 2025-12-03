package semantics;

import provided.JottTokenizer;
import provided.JottTree;
import provided.JottParser;
import provided.Token;
import parser.ParseException;

import java.util.ArrayList;

public class Jott {
    public static void main(String[] args){
        try {
            ArrayList<Token> tokens = JottTokenizer.tokenize(args[0]);
            JottTree root = JottParser.parse(tokens);
            root.validateTree();
            System.exit(0);
        } catch (SemanticException se) {
            System.err.println(se.getMessage());
            System.exit(1);
        } catch (ParseException pe) {
            System.err.println(pe.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.exit(1);
        }
    }
}
