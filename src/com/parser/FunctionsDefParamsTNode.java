package parser;
import provided.JottTree;

public class FunctionsDefParamsTNode implements JottTree {
	
    public FunctionsDefParamsTNode() {
    }

    public static FunctionsDefParamsTNode parseFunctionsDefParamsTNode() {
        return new FunctionsDefParamsTNode();
    }

	@Override
	public String convertToJava(String indentLevel) {
		// TODO: Implement this method
		return "";
	}

	@Override
	public boolean validateTree() {
		// TODO: Implement this method
		return true;
	}

	@Override
	public String convertToJott() {
		// TODO: Implement this method
		return "";
	}

	@Override
	public String convertToC() {
		// TODO: Implement this method
		return "";
	}

	@Override
	public String convertToPython() {
		// TODO: Implement this method
		return "";
	}
}
