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
        String[] testcases = {
            "phase4testcases\\divisionByZero.jott",
            "phase4testcases\\divisionByZeroFloat.jott",
            "phase4testcases\\doubleMathAndIf.jott",
            "phase4testcases\\functionChainMultipleTypes.jott",
            "phase4testcases\\helloWorld.jott",
            "phase4testcases\\intMathAndWhile.jott",
            "phase4testcases\\reallyLong.jott",
            "phase4testcases\\stringsConcatLength.jott",
            "phase4testcases\\test.jott",
            "phase4testcases\\test1.jott",
            "phase4testcases\\unitializedVariable.jott",
        };
        for (String testcase: testcases) {
            System.out.println("Running test case: " + testcase);
            try {
                ArrayList<Token> tokens = JottTokenizer.tokenize(testcase);
                JottTree root = JottParser.parse(tokens);
                root.validateTree();

                RuntimeEnv.reset(); // reset env, sets it up
                root.execute();
            } catch (SemanticException se) {
                System.err.println("Semantic Error: " + se.getMessage());
            } catch (ParseException pe) {
                System.err.println("Parse Error: " + pe.getMessage());
            } catch (RuntimeException re) {
                System.err.println("Runtime Error: " + re.getMessage());
            }
            System.out.println();
        }
        
    }
}