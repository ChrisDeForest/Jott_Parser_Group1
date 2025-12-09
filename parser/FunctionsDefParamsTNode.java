package parser;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class FunctionsDefParamsTNode implements JottTree {

	// store what we parse so the def-params node can read them
	private final Token idTok;
	private final Token typeTok;

	public FunctionsDefParamsTNode(Token idTok, Token typeTok) {
		this.idTok = idTok;
		this.typeTok = typeTok;
	}

	// NEW: accept the token list and actually parse: , <id> : <type>
	public static FunctionsDefParamsTNode parseFunctionsDefParamsTNode(ArrayList<Token> tokens) {
		if (tokens.isEmpty())
			throw new ParseException("parseFunctionsDefParamsTNode: Unexpected EOF in parameter tail", null);

		if (tokens.get(0).getTokenType() != TokenType.COMMA) {
			throw new ParseException("parseFunctionsDefParamsTNode: Expected ',' before additional parameter", tokens.get(0));
		}
		tokens.remove(0); // consume ','

		if (tokens.isEmpty())
			throw new ParseException("parseFunctionsDefParamsTNode: Unexpected EOF after ','", null);
		Token id = tokens.get(0);
		if (id.getTokenType() != TokenType.ID_KEYWORD) {
			throw new ParseException("parseFunctionsDefParamsTNode: Expected parameter name (id) after ','", id);
		}
		tokens.remove(0); // consume id

		if (tokens.isEmpty())
			throw new ParseException("parseFunctionsDefParamsTNode: Unexpected EOF after parameter name", null);
		if (tokens.get(0).getTokenType() != TokenType.COLON) {
			throw new ParseException("parseFunctionsDefParamsTNode: Expected ':' after parameter name", tokens.get(0));
		}
		tokens.remove(0); // consume ':'

		if (tokens.isEmpty())
			throw new ParseException("parseFunctionsDefParamsTNode: Unexpected EOF after ':'", null);
		Token typeTok = tokens.get(0);
		String tt = typeTok.getToken();
		if (!("Double".equals(tt) || "Integer".equals(tt) || "String".equals(tt) || "Boolean".equals(tt))) {
			throw new ParseException("Expected a type (Double | Integer | String | Boolean)", typeTok);
		}
		tokens.remove(0); // consume type

		return new FunctionsDefParamsTNode(id, typeTok);
	}

	// accessors (so call site doesn't need to touch fields directly)
	public Token getIdTok() {
		return idTok;
	}

	public Token getTypeTok() {
		return typeTok;
	}

	@Override
	public String convertToJava(String indentLevel) {
		return "";
	}

	@Override
	public boolean validateTree() {
		return true;
	}

	@Override
	public String convertToJott() {
		return "," + idTok.getToken() + ":" + typeTok.getToken();
	}

	@Override
	public String convertToC() {
		return "";
	}

	@Override
	public String convertToPython() {
		return "";
	}

	public Object execute(){
		return null;
	}
}
