package testers;

import provided.JottTokenizer;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import semantics.SemanticException;
import parser.ParseException;

import java.util.ArrayList;

import execution.RuntimeEnv;

public class JottExecutionTester {

    public static void main(String[] args) {
        try {
            String testFile = "phase4testcases\\test1.jott";
            ArrayList<Token> tokens = JottTokenizer.tokenize(testFile);
            JottTree root = JottParser.parse(tokens);
            root.validateTree();

            RuntimeEnv.reset(); // reset env, sets it up
            root.evaluate()
        } catch (SemanticException se) {
            System.err.println("Semantic Error: " + se.getMessage());
        } catch (ParseException pe) {
            System.err.println("Parse Error: " + pe.getMessage());
        } catch (RuntimeException re) {
            System.err.println("Runtime Error: " + re.getMessage());
        }
    }
}