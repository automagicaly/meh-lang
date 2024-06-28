package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.TokenType.*;

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
        keywords.put("let", LET);
        keywords.put("return", RETURN);
        keywords.put("if", IF);
        keywords.put("else", ELSE);
        keywords.put("and", AND);
        keywords.put("or", OR);
        keywords.put("not", NOT);
        keywords.put("void", VOID);
        keywords.put("bool", BOOL);
        keywords.put("true", TRUE);
        keywords.put("false", FALSE);

        trivialTokens = new HashMap<>();
        trivialTokens.put('%', PERCENT);
        trivialTokens.put(',', COMMA);
        trivialTokens.put('&', AMP);
        trivialTokens.put('|', PIPE);
        trivialTokens.put('+', PLUS);
        trivialTokens.put(':', COLON);
        trivialTokens.put('*', STAR);
        trivialTokens.put('(', OPEN_PARENTHESIS);
        trivialTokens.put(')', CLOSE_PARENTHESIS);
        trivialTokens.put('[', OPEN_SQUARE_BRACKET);
        trivialTokens.put(']', CLOSE_SQUARE_BRACKET);
        trivialTokens.put(' ', SPACE);
        trivialTokens.put('\t', SPACE);
        trivialTokens.put('\\', BACK_SLASH);
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
        tokens.add(new Token(EOF, "", null, line));
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
                addToken(match('=') ? EQUAL_TO : EQUALS);
                break;
            case '<':
                if (match('=')) {
                    addToken(LESS_OR_EQUAL_TO);
                } else if (match('<')) {
                    addToken(SHIFT_LEFT);
                } else {
                    addToken(LESS_THAN);
                }
                break;
            case '>':
                addToken(match('=') ? GREATER_OR_EQUAL_TO : GREATER_THAN);
                if (match('=')) {
                    addToken(GREATER_OR_EQUAL_TO);
                } else if (match('<')) {
                    addToken(SHIFT_RIGHT);
                } else {
                    addToken(GREATER_THAN);
                }
                break;
            case '-':
                addToken(match('>') ? ARROW : MINUS);
                break;
            case '!':
                if (peek() != '=') {
                    throw new RuntimeException("Expecting '=' on line " + line);
                }
                addToken(NOT_EQUAL_TO);
                break;
            case '"':
                scanString();
                if (peek() != '"') {
                    throw new RuntimeException("Expecting  '\"' on line " + line);
                }
                addToken(STRING, source.substring(start, current));
                advanceCurrent();
                break;
            case '/':
                // ignore comments
                if (match('/')) {
                    advanceUntil('\n');
                } else {
                    addToken(SLASH);
                }
                break;
            case '\n':
                addToken(NEW_LINE);
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
        while (!isAtEnd() && peek() != '"') {
           if (peek() == '\\' && peekNext() == '"') {
                advanceCurrent();
            }
            advanceCurrent();
        }
    }

    private void advanceUntil(char c) {
        while (!isAtEnd() && peek() != c) {
            advanceCurrent();
        }
    }

    private void scanIdentifier() {
        while (isAlphaNumeric(peek())) {
            advanceCurrent();
        }
        String text = source.substring(start, current);
        addToken(keywords.getOrDefault(text, IDENTIFIER));
    }

    private char peekNext() {
        if (isAtEnd() || current + 1 == source.length()) return '\0';
        return source.charAt(current + 1);
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
        if (peek() == '0' && peekNext() == 'x') {
            advanceCurrent();
            advanceCurrent();
            while (isHex(peek())) {
                advanceCurrent();
            }
            addToken(NUMBER, Integer.parseInt(source.substring(start + 2, current), 16));
            return;
        }

        while (isDigit(peek()) || peek() == '_') {
            advanceCurrent();
        }
        addToken(NUMBER, Integer.parseInt(source.substring(start, current)));
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