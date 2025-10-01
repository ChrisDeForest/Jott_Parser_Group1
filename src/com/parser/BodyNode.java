package parser;
import provided.JottTree;

public class BodyNode implements JottTree{

    public BodyNode() {
    }

    public static BodyNode parseBodyNode() {
        return new BodyNode();
    }

	@Override
	public String convertToJava(String indent) {
		// TODO: Implement method logic
		return "";
	}

	@Override
	public boolean validateTree() {
		// TODO: Implement method logic
		return true;
	}

	@Override
	public String convertToJott() {
		// TODO: Implement method logic
		return "";
	}

	@Override
	public String convertToC() {
		// TODO: Implement method logic
		return "";
	}

	@Override
	public String convertToPython() {
		// TODO: Implement method logic
		return "";
	}
}
