package io.lorde;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Scanner {
    private static final Map<String, TokenType> keywords;
    private static final Map<Character, TokenType> trivialTokens;
    private final String source;
    private final List<Token> tokens;
    private int start;
    private int current;
    private int line;

    static {
        keywords = new HashMap<>();
        keywords.put("let", TokenType.LET);
        keywords.put("return", TokenType.RETURN);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("and", TokenType.AND);
        keywords.put("or", TokenType.OR);
        keywords.put("not", TokenType.NOT);
        keywords.put("void", TokenType.VOID);
        keywords.put("bool", TokenType.BOOL);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("byte", TokenType.BYTE);
        keywords.put("float", TokenType.FLOAT);
        keywords.put("double", TokenType.DOUBLE);
        keywords.put("file", TokenType.FILE);
        keywords.put("as", TokenType.AS);
        keywords.put("is", TokenType.IS);
        keywords.put("any", TokenType.ANY);
        keywords.put("int", TokenType.INTEGER);

        trivialTokens = new HashMap<>();
        trivialTokens.put('%', TokenType.PERCENT);
        trivialTokens.put(',', TokenType.COMMA);
        trivialTokens.put('&', TokenType.AMP);
        trivialTokens.put('|', TokenType.PIPE);
        trivialTokens.put('+', TokenType.PLUS);
        trivialTokens.put(':', TokenType.COLON);
        trivialTokens.put('*', TokenType.STAR);
        trivialTokens.put('(', TokenType.OPEN_PARENTHESIS);
        trivialTokens.put(')', TokenType.CLOSE_PARENTHESIS);
        trivialTokens.put('[', TokenType.OPEN_SQUARE_BRACKET);
        trivialTokens.put(']', TokenType.CLOSE_SQUARE_BRACKET);
        trivialTokens.put(' ', TokenType.SPACE);
        trivialTokens.put('\t', TokenType.SPACE);
        trivialTokens.put('\\', TokenType.BACK_SLASH);
    }

    Scanner(String source) {
        this.source = source;
        this.tokens = new ArrayList<>();
        this.start = 0;
        this.current = 0;
        this.line = 1;
    }

    public List<Token> scanTokens() {
        while(!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        TokenType t = trivialTokens.getOrDefault(c, null);
        if (t != null){
            addToken(t);
            return;
        }
        switch (c) {
            case '=':
                addToken(match('=') ? TokenType.EQUAL_TO : TokenType.EQUALS);
                break;
            case '<':
                if (match('=')) {
                    addToken(TokenType.LESS_OR_EQUAL_TO);
                } else if (match('<')) {
                    addToken(TokenType.SHIFT_LEFT);
                } else {
                    addToken(TokenType.LESS_THAN);
                }
                break;
            case '>':
                if (match('=')) {
                    addToken(TokenType.GREATER_OR_EQUAL_TO);
                } else if (match('>')) {
                    addToken(TokenType.SHIFT_RIGHT);
                } else {
                    addToken(TokenType.GREATER_THAN);
                }
                break;
            case '-':
                addToken(match('>') ? TokenType.ARROW : TokenType.MINUS);
                break;
            case '!':
                if (peek() != '=') {
                    throw new RuntimeException("Expecting '=' on line " + line);
                }
                addToken(TokenType.NOT_EQUAL_TO);
                break;
            case '"':
                scanString();
                if (peek() != '"') {
                    throw new RuntimeException("Expecting  '\"' on line " + line);
                }
                addToken(TokenType.STRING, source.substring(start, current));
                advanceCurrent();
                break;
            case '/':
                // ignore comments
                if (match('/')) {
                    advanceUntilNewLine();
                } else {
                    addToken(TokenType.SLASH);
                }
                break;
            case '\n':
                addToken(TokenType.NEW_LINE);
                line++;
                break;
            // ignores \r
            case '\r':
                break;
            default:
                if (isDigit(c)) {
                    scanNumber();
                } else if (isAlpha(c)){
                    scanIdentifier();
                } else{
                    throw new RuntimeException("Unexpected character");
                }
        }
    }

    private void scanString() {
        start++; // get rid of the first "
        while (!isAtEnd() && peek() != '"') {
           if (peek() == '\\' && peekNext() == '"') {
                advanceCurrent();
            }
            advanceCurrent();
        }
    }

    private void advanceUntilNewLine() {
        while (!isAtEnd() && peek() != '\n') {
            advanceCurrent();
        }
    }

    private void scanIdentifier() {
        while (isAlphaNumeric(peek())) {
            advanceCurrent();
        }
        String text = source.substring(start, current);
        addToken(keywords.getOrDefault(text, TokenType.IDENTIFIER));
    }

    private char peekNext() {
        if (isAtEnd() || current + 1 == source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private char peekPrevious() {
        if (current == 0) {
            return '\0';
        }
        return source.charAt(current - 1);
    }

    private boolean isAlphaNumeric(char c) {
        return isDigit(c) || isAlpha(c);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')
                || c == '_' || c == '-';
    }

    private void scanNumber() {
        if (peekPrevious() == '0' && peek() == 'x') {
            advanceCurrent();
            advanceCurrent();
            while (isHex(peek())) {
                advanceCurrent();
            }
            addToken(TokenType.NUMBER_HEX_INTEGER, Integer.parseInt(source.substring(start + 2, current), 16));
            return;
        }

        while (isDigit(peek()) || peek() == '_') {
            advanceCurrent();
        }

        if (peek() == '.') {
            advanceCurrent();
            if (! isDigit(peek())) {
                throw new RuntimeException("Expecting rest of the float/double number");
            }
            while (isDigit(peek()) || peek() == '_') {
                advanceCurrent();
            }
            addToken(TokenType.NUMBER_FLOAT, Double.parseDouble(source.substring(start, current)));
            return;
        }

        addToken(TokenType.NUMBER_INTEGER, Integer.parseInt(source.substring(start, current)));
    }

    private boolean isHex(char c) {
        return isDigit(c)
                || (c >= 'a' &&  c <= 'f')
                || (c >= 'A' &&  c <= 'F');
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private boolean match(char expected) {
        if (peek() != expected) {
            return false;
        }

        advanceCurrent();
        return true;
    }

    private char advance() {
        return source.charAt(advanceCurrent());
    }

    private int advanceCurrent() {
        return current++;
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}