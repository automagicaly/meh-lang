package io.lorde;

import io.lorde.ast.Root;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner("""
        \\main -> void
            print "Voce e o Renan :)"
        main "wrong" 1
        \\times2 a: int[] -> int[]
            return map a (\\i -> i * 2)
        print (times2 [1,2,3])
        
        let n: int = 42
        \\is_odd a: int -> bool
            if a % 2 == 0
                return true
            else
                return false
        print is_odd n
        
        print "\\"Hello there!\\""
        
        \\fib n: int -> int
            if n < 2
                return 0
                
            return (fib n - 1) + (fib n - 2)
        print fib 5
        
        print ((((0x0 | 0xF0) & 0x80) >> 8) == 0x1)
        print ((((0x0 | 0xF0) & 0x80) << 1) == 0)
        
        let x = (\\i -> i ) 42
        
        \\filter list: int[] predicate: (\\int -> bool) -> int[]
            if is_empty list
                return []
            if predicate list[0]
                return concat [list[0]] (filter (tail list) predicate)
            else
                return filter (tail list) predicate
        print filter [ 1, 2 ,3 ] is_odd
        """);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        Root root = parser.parse();
        System.out.println(root.accept(new AstPrinter()));
    }
}