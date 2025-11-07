package testers;

/*
  Jott semantic tester. This will test the semantic phase of the Jott
  project.

  This tester assumes a working and valid tokenizer and parser.
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import provided.JottTree;
import provided.Token;
import provided.JottTokenizer;
import provided.JottParser;

public class JottSemanticTester {
    ArrayList<TestCase> testCases;

    private static class TestCase{
        String testName;
        String fileName;
        String phase;
        boolean error;

        public TestCase(String testName, String fileName, String phase, boolean error) {
            this.testName = testName;
            this.fileName = fileName;
            this.phase = phase;
            this.error = error;
        }
    }

    private boolean tokensEqualNoFileData(Token t1, Token t2){
        return t1.getTokenType() == t2.getTokenType() &&
               t1.getToken().equals(t2.getToken());
    }

    private void createTestCases(){
        final String PARSER = "parser";
        final String SEMANTIC = "semantic";

        this.testCases = new ArrayList<>();

        // phase 2 testcases that are still relevant to phase 3
        this.testCases.add(new TestCase("function call param type not matching", "funcCallParamInvalid.jott", SEMANTIC, true));
        this.testCases.add(new TestCase("function not defined", "funcNotDefined.jott", SEMANTIC, true));
        this.testCases.add(new TestCase("i_expr relop d_expr function return", "funcReturnInExpr.jott", SEMANTIC, true));
        this.testCases.add(new TestCase("main must be void", "mainReturnNotInt.jott", SEMANTIC, true));
        this.testCases.add(new TestCase("mismatch return type", "mismatchedReturn.jott", SEMANTIC, true));
        this.testCases.add(new TestCase("missing main", "missingMain.jott", SEMANTIC, true));
        this.testCases.add(new TestCase("missing return", "missingReturn.jott", SEMANTIC, true));
        this.testCases.add(new TestCase("valid while loop", "validLoop.jott", SEMANTIC, false));
        this.testCases.add(new TestCase("while is keyword, cannot be used as id", "whileKeyword.jott", SEMANTIC, true));

        // phase 3 specific testcases 
        this.testCases.add(new TestCase("func wrong param type", "funcWrongParamType.jott", SEMANTIC, true));
        this.testCases.add(new TestCase("hello world", "helloWorld.jott", SEMANTIC, false));
        this.testCases.add(new TestCase("if stmt returns", "ifStmtReturns.jott", SEMANTIC, false));
        this.testCases.add(new TestCase("larger valid", "largerValid.jott", SEMANTIC, false));
        this.testCases.add(new TestCase("missing func params", "missingFuncParams.jott", SEMANTIC, true));
        this.testCases.add(new TestCase("no return if", "noReturnIf.jott", SEMANTIC, true));
        this.testCases.add(new TestCase("no return while", "noReturnWhile.jott", SEMANTIC, true));
        this.testCases.add(new TestCase("provided example 1", "providedExample1.jott", SEMANTIC, false));
        this.testCases.add(new TestCase("return id", "returnId.jott", SEMANTIC, true));
        this.testCases.add(new TestCase("void return", "voidReturn.jott", SEMANTIC, true));
    }

    private boolean semanticTest(TestCase test, String originalJottCode){
        try {
            final String baseDir = test.phase.equals("parser")
                    ? "parserTestCases/"
                    : "phase3testcases/";

            ArrayList<Token> tokens = JottTokenizer.tokenize(baseDir + test.fileName);
            if (tokens == null) {
                fail(test.testName, "Expected tokens; got null from tokenizer on " + baseDir + test.fileName);
                return false;
            }

            System.out.println(tokenListString(tokens));
            ArrayList<Token> cpyTokens = new ArrayList<>(tokens);

            JottTree root = JottParser.parse(tokens);

            if (test.phase.equals("parser")) {
                // Negative parser tests must fail to parse
                if (test.error) {
                    if (root == null) {
                        pass(test.testName, "Parser error occurred as expected (root == null).");
                        return true;
                    }
                    fail(test.testName, "Expected parser error; parser returned a tree.");
                    return false;
                }

                // Positive parser tests must produce a tree
                if (root == null) {
                    fail(test.testName, "Expected a parsed tree; parser returned null.");
                    return false;
                }

                String jottCode = root.convertToJott();
                if (jottCode == null) {
                    fail(test.testName, "convertToJott() returned null for a parser-positive test.");
                    return false;
                }

                Files.write(Paths.get(baseDir + "semanticTestTemp.jott"),
                            jottCode.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                ArrayList<Token> newTokens = JottTokenizer.tokenize(baseDir + "semanticTestTemp.jott");
                if (newTokens == null) {
                    fail(test.testName, "Round-trip tokenization returned null.");
                    return false;
                }
                if (newTokens.size() != cpyTokens.size()) {
                    fail(test.testName, "Round-trip token count mismatch.\nExpected: "
                            + tokenListString(cpyTokens) + "\nGot     : " + tokenListString(newTokens));
                    return false;
                }
                for (int i = 0; i < newTokens.size(); i++) {
                    if (!tokensEqualNoFileData(newTokens.get(i), cpyTokens.get(i))) {
                        fail(test.testName, "Round-trip token mismatch at index " + i + ".\nExpected: "
                                + tokenListString(cpyTokens) + "\nGot     : " + tokenListString(newTokens));
                        return false;
                    }
                }

                pass(test.testName, "Parser succeeded and round-trip tokens matched.");
                return true;
            }

            // SEMANTIC PHASE
            if (root == null) {
                fail(test.testName, "Parser returned null during semantic test; semantic phase requires a valid parse.");
                return false;
            }

            boolean semOK = root.validateTree(); 

            if (test.error) {
                if (!semOK) {
                    pass(test.testName, "Semantic error occurred as expected: validateTree()==false");
                    return true;
                }
                fail(test.testName, "Expected semantic error; validateTree() returned true.");
                return false;
            }

            // Positive semantic tests
            if (!semOK) {
                fail(test.testName, "Unexpected semantic error; validateTree() returned false.");
                return false;
            }

            String jottCode = root.convertToJott();
            if (jottCode == null) {
                fail(test.testName, "convertToJott() returned null for a semantic-positive test.");
                return false;
            }

            Files.write(Paths.get(baseDir + "semanticTestTemp.jott"),
                        jottCode.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            ArrayList<Token> newTokens = JottTokenizer.tokenize(baseDir + "semanticTestTemp.jott");
            if (newTokens == null) {
                fail(test.testName, "Round-trip tokenization returned null.");
                return false;
            }
            if (newTokens.size() != cpyTokens.size()) {
                fail(test.testName, "Round-trip token count mismatch.\nExpected: "
                        + tokenListString(cpyTokens) + "\nGot     : " + tokenListString(newTokens));
                return false;
            }
            for (int i = 0; i < newTokens.size(); i++) {
                if (!tokensEqualNoFileData(newTokens.get(i), cpyTokens.get(i))) {
                    fail(test.testName, "Round-trip token mismatch at index " + i + ".\nExpected: "
                            + tokenListString(cpyTokens) + "\nGot     : " + tokenListString(newTokens));
                    return false;
                }
            }

            pass(test.testName, "Semantic validation succeeded and round-trip tokens matched.");
            return true;

        } catch (Exception e) {
            fail(test.testName, "Unknown Exception occurred.");
            e.printStackTrace();
            return false;
        }
    }

    private void pass(String testName, String reason){
        System.out.println("\tPassed Test: " + testName);
        System.out.println("\tReason: " + reason + "\n");
    }

    private void fail(String testName, String reason){
        System.err.println("\tFailed Test: " + testName);
        System.err.println("\t" + reason);
    }

    private String tokenListString(ArrayList<Token> tokens){
        StringBuilder sb = new StringBuilder();
        for (Token t: tokens) {
            sb.append(t.getToken());
            sb.append(":");
            sb.append(t.getTokenType().toString());
            sb.append(" ");
        }
        return sb.toString();
    }

    private boolean runTest(TestCase test){
        System.out.println("Running Test: " + test.testName);
        String orginalJottCode;
        try {
            if (test.phase.equals("parser")){
                orginalJottCode = new String(
                        Files.readAllBytes(Paths.get("parserTestCases/" + test.fileName)));
            } else {
                orginalJottCode = new String(
                        Files.readAllBytes(Paths.get("phase3testcases/" + test.fileName)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return semanticTest(test, orginalJottCode);
    }

    public static void main(String[] args) {
        System.out.println("NOTE: System.err may print at the end. This is fine.");
        JottSemanticTester tester = new JottSemanticTester();

        int numTests = 0;
        int passedTests = 0;
        tester.createTestCases();
        for(JottSemanticTester.TestCase test: tester.testCases){
            numTests++;
            if(tester.runTest(test)) passedTests++;
        }

        System.out.printf("Passed: %d/%d%n", passedTests, numTests);
    }
}
