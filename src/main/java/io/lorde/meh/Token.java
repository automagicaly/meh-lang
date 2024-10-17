package io.lorde.meh;

public record Token(TokenType type, String lexeme, Object literal, int line) {}
