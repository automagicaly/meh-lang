package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner("1\n2 + 2\nlet something_-1nice\n print something_-1nice\n\\hello_world*/0-1\t if ->:\n\"Hello World!\"");
        List<Token> tokens = scanner.scanTokens();
        for (Token t: tokens) {
            System.out.println(t);
        }
    }
}