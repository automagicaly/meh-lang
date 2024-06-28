# *TODO*
* structs
* generics
* define built-in functions
* doubles
* bytes
* array of structs
* dictionaries
* algebraic enums e.g. None | Some int ; return None ; return Some 23
* basic string manipulation functions
* bind value after variable declaration

## Objectives
* Create a project complex enough to exercise Java 17 features.
* Create a small strongly typed programming language inspired by Elm, Clojure, and, Erlang
* Optimize Scanner to use DFA
* Generate byte-code/class file/jars so the program can run on the JVM
* Implement as much as possible of the default library on the language itself 

## Non-Objectives
* Implement an extensive default library
* Integration with java
* Networking
* Multi-threading
* Argument parsing
* Reading resources from JAR
* DB connections/drivers
* Compiler driven optimization
* Good runtime performance
* Extensive type system
* Good error messages
* To be production ready

## Comments
Comments starts with `//`

## Lexicon
```
KEYWORDS = let|\|return|if|else|and|or|not|int|str|void|bool|true|false
NUMBER = [1-9][0-9_]*
STRING = ".*"  # take care of scaped characters
OPERATIONS = [%+*/&|,=()><:-]|->|[|]|<=|>=|==|!=|>>|<<
IDENTIFIER = [a-zA-Z_-][a-zA-Z_0-9-]*
NEW_LINE = \n
SPACE = [ \t]
```

## Grammar
```
S = 'let' SPACE+ IDENTIFIER OPTIONAL_TYPE SPACE* '=' SPACE* EXPR NEW_LINE
S = EXPR SPACE* NEW_LINE
S = 'return' SPACE+ EXPR SPACE* NEW_LINE
S = 'if' SPACE+ EXPR NEW_LINE BODY | 'if' SPACE+ EXPR NEW_LINE BODY 'else' NEW_LINE BODY
S = '\' IDENTIFIER SPACE+ PARAM* '->' SPACE+ TYPE SPACE* NEW_LINE BODY
BODY = SPACE+ S BODY | _
EXPR = LITERAL|IDENTIFIER|INDEXED_IDENTIFIER|'(' EXPR ')'|EXPR OP EXPR|SAPCE* EXPR SPACE*|FUNTION_CALL|LAMBDA
FUNCTION_CALL = IDENTIFIER SPACE+ FUN_VAL | '(' LAMBDA ')' SPACE+ FUN_VAL
FUN_VAL = EXPR SPACE+ FUN_VAL | EXPR | _
IDENTIFIER = [a-zA-Z_-][a-zA-Z_0-9-]*
INDEXED_IDENTIFIER = IDENTIFIER '[' EXPR ']'
LAMBDA = '\' LAMBDA_VAR '->' SPACE+ EXPR
LAMBDA_ARG_TYPES = TYPE SPACE+ LAMBDA_ARG_TYPES | TYPE 
LAMBDA_VAR = IDENTIFIER SPACE+ LAMBDA_VAR | _
LIST_LITERAL = '[' SPACE* NEW_LINE? LIST_ITEMS SPACE* NEW_LINE ']'
LIST_ITEMS = EXPR | EXPR ',' SPACE* NEW_LINE? LIST_ITEMS | _ 
LITERAL = NUMBER|STRING|LIST_LITERAL
NEW_LINE = '\n'
NUMBER = [0-9][0-9_]* | '0x' [0-9A-Fa-f]+ 
OP = '+'|'-'|'*'|'/'|'&'|'|'|'<'|'<='|'<<'|'>'|'>='|'>>'|'=='|'!='|'and'|'or'|'not'|'%'
OPTIONAL_TYPE = SPACE* ':' SPACE*
PARAM = VAR_DEC SPACE+ PARAM | _
SCAPED_STRING = SCAPED_STRING '\"' SCAPED_STRING | [^"] SCAPED_STRING | _
SPACE = ' '|'\t'
STRING = '"' SCAPED_STRING '"'
TYPE = 'bool' |'int' | 'str' | 'int[]' | 'str[]' | 'void' | '(' SPACE* '\' LAMBDA_ARG_TYPES SPACE+ '->' SPACE+ TYPE SPACE* ')'
VAR_DEC = IDENTIFIER SPACE* ':' SPACE* TYPE
```

## Built-in Functions

### print

### read

### write

### map

### reduce

### filter

### append

### head

### tail

### open

### close

### is_empty

### size_of

### concat

## Examples
```
\times2 a: int[] -> int[]
    return map a (\i -> i * 2)
print (times2 [1,2,3]) 

let n: int = 42
\is_odd a: int -> bool
    if a % 2 == 0
        return true
    else
        return false
print is_odd n

print "\"Hello there!\""

\fib n: int -> int
    if n < 2
        return 0
    
    return fib(n - 1) + fib(n - 2)
print fib 5

print ((((0x0 | 0xF0) & 0x80) >> 8) == 0x1)
print ((((0x0 | 0xF0) & 0x80) << 1) == 0)

let x = (\i -> i ) 42

\filter list: int[] should_add: (\int -> bool) -> int[]
    if is_empty list
        return []
    if should_add list[0]
        return concat [list[0]] (filter (tail list) should_add)
    else
        return filter (tail list) should_add
        
print filter [ 1, 2 ,3 ] is_odd
```