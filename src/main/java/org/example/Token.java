package org.example;

public record Token(TokenType type, String lexeme, Object literal, int line) {}
