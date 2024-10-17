package io.lorde.meh;

import io.lorde.meh.ast.Program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Meh {
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            compileFile(args[1]);
        } else {
            repl();
        }
    }

    public static void compileFile(String filePath) {
        System.out.println(filePath);
    }

    public static void repl() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in)
        );
        while (true) {
            System.out.print("meh> ");

            String input = readInput(reader);

            if (input == null) {
                break;
            }

            Lexer lexer = new Lexer(input);
            List<Token> tokens = lexer.scanTokens();
            Parser parser = new Parser(tokens);
            Program program = parser.parse();
            System.out.println(program.accept(new AstPrinter()));
        }
    }

    private static String readInput(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            String input = reader.readLine();

            // repl exited / EOF detected
            if (input == null) {
                return null;
            }

            if (input.isEmpty()) {
                return sb.toString();
            }

            sb.append(input);
            sb.append("\n");
            System.out.print("   > ");
        }
    }

    public static class ParserError extends RuntimeException {}

    public static ParserError error(Token token, String errorMessage) {
        // TODO better error reporting
        System.out.println("Line " + token.line() + ": " + errorMessage);
        return new ParserError();
    }
}