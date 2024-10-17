## Objectives
* Create a project complex enough to exercise Java 17 features.
* Create a small strongly typed programming language inspired by Elm, Clojure, and, Erlang
* Optimize Scanner to use DFA
* Generate byte-code/class file/jars so the program can run on the JVM
* Implement default library in Java
* Program runs from top to bottom like Python
* Not use Java as an intermediary language

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
* Support modules / multiple files
* Handle exceptions

## Lexicon
```
KEYWORDS = let|\|return|if|else|and|or|not|int|str|void|bool|true|false|byte|float|double|file|as|is
NUMBER_INTEGER = [1-9][0-9_]*
NUMBER_HEX_INTEGER = 0x[0-9A-Fa-f]+
NUMBER_FLOAT = [1-9][0-9_]*[.][0-9][0-9_]*
STRING = .*  # take care of scaped characters
OPERATIONS = [%+*/&|,=()><:-]|->|[|]|<=|>=|==|!=|>>|<<
IDENTIFIER = [a-zA-Z_-][a-zA-Z_0-9-]*
NEW_LINE = \n
SPACE = [ \t]
```

## Grammar
```
S = S S
S = 'let' SPACE+ IDENTIFIER OPTIONAL_TYPE SPACE* '=' SPACE* EXPR END
S = EXPR SPACE* END
S = 'return' SPACE+ EXPR SPACE* END
S = 'if' SPACE+ EXPR NEW_LINE BODY | 'if' SPACE+ EXPR NEW_LINE BODY 'else' NEW_LINE BODY
S = '\' IDENTIFIER SPACE+ PARAM* '->' SPACE+ TYPE SPACE* NEW_LINE BODY
ARRAY_TYPES = BASE_TYPES '[]'
BASE_TYPES = 'bool' |'int' | 'str' | 'byte' | 'float' | 'double' | 'file' | 'void' | 'any'
BODY = SPACE+ S BODY | _
BOOL = 'true' | 'false'
BINARY_EXPR = EXPR OP EXPR
CAST = EXPR SPACE+ 'as' SPACE+ TYPE
END = NEW_LINE | EOF
EXPR = LITERAL|IDENTIFIER|INDEXED_IDENTIFIER|GROUPING|BINARY_EXPR|FUNTION_CALL|LAMBDA|CAST|TYPE_CHECK|UNARY_EXPR
FLOAT = [1-9][0-9_]* '.' [0-9][0-9_]*
FUNCTION_CALL = IDENTIFIER SPACE+ FUN_VAL | '(' LAMBDA ')' SPACE+ FUN_VAL
FUN_VAL = EXPR SPACE+ FUN_VAL | EXPR | _
GROUPING = '(' EXPR ')'
HEX_INTEGER = '0x' [0-9A-Fa-f]+  
IDENTED_EXPR = SAPCE* EXPR SPACE*
IDENTIFIER = [a-zA-Z_-][a-zA-Z_0-9-]*
INDEXED_IDENTIFIER = IDENTIFIER '[' EXPR ']'
INTEGER = [0-9][0-9_]* 
LAMBDA = '\' LAMBDA_VAR '->' SPACE+ EXPR
LAMBDA_ARG_TYPES = TYPE SPACE+ LAMBDA_ARG_TYPES | TYPE | _
LAMBDA_VAR = IDENTIFIER OPTIONAL_TYPE SPACE+ LAMBDA_VAR | _
LIST_LITERAL = '[' SPACE* NEW_LINE? LIST_ITEMS SPACE* NEW_LINE ']'
LIST_ITEMS = EXPR | EXPR ',' SPACE* NEW_LINE? LIST_ITEMS | _ 
LITERAL = NUMBER|STRING|BOOL|LIST_LITERAL
NEW_LINE = '\n'
NUMBER = INTEGER | FLOAT | HEX_INTEGER
OP = '+'|'-'|'*'|'/'|'&'|'|'|'<'|'<='|'<<'|'>'|'>='|'>>'|'=='|'!='|'and'|'or'|'%'
OPTIONAL_TYPE = SPACE* ':' TYPE SPACE* | _
PARAM = VAR_DEC SPACE+ PARAM | _
SCAPED_STRING = SCAPED_STRING '\"' SCAPED_STRING | [^"] SCAPED_STRING | _
SPACE = ' '|'\t'
STRING = '"' SCAPED_STRING '"'
TYPE = BASE_TYPES | ARRAY_TYPES | '(' SPACE* '\' LAMBDA_ARG_TYPES SPACE+ '->' SPACE+ TYPE SPACE* ')'
TYPE_CHECK = EXPR SPACE+ 'is' SPACE+ TYPE
UNARY_EXPR = UNARY_OP EXPR
UNARY_OP = 'not' | '-' | '+'
VAR_DEC = IDENTIFIER SPACE* ':' SPACE* TYPE
```

## Comments
Comments starts with `//`


## Base Types

* bool -> Boolean value; Can be either `true` or `false`
* bool[] -> array of booleans
* byte -> 8bit unsigned integer
* byte[] -> array of 8bit unsigned integers
* double -> double
* double[] -> array of doubles
* file -> An open file
* file[] -> array of files
* float -> float
* float[] -> array of floats
* int -> 64bit integer
* int[] -> array of 64bit integers
* any -> Represents any value and every type is implicitly a void type as well.
* any[] -> array of values

## Casting
Every type is compatible with `any` without casting. You can cast anything using `as` e.g. `let a: int = 2.3 as int`.

## Array Literal
While inferring the type of array of numbers  unless explicitly expressed numbers with decimal places will be treated as `doubles`.

## Heterogeneous Array
Arrays of the type `any[]` can store any kind of value e.g.

```
let arr: any[] = [1 "Hello" true [1.2 4]]
let arr2 = [2 "there!" false [3 4.5]]
```

## Error handling
**THERE IS NO ERROR HANDLING!**
**ALL ERRORS WILL CAUSE THE PROGRAM TO CRASH!**

## Built-in Functions

### push
Appends a value at the end of an array, increasing its size.

**Definition:**
```
\push arr: bool[] element: bool -> bool[]
\push arr: byte[] element: byte -> byte[]
\push arr: double[] element: double -> double[]
\push arr: file[] element: file -> file[]
\push arr: float[] element: float -> float[]
\push arr: int[] element: int -> int[]
\push arr: any[] element: any -> any[]
\push arr: any[] element: any[] -> any[]
```

**Examples:**
```
push [true] false // [true false]
push [0x01 0x2 0x3] 4 // [0x01 0x02 0x03 0x04]
push [1.0 2.3 3.25] 4.7 // [1.0 2.3 3.25 4.7]
push [(open "path1")] (open "path2") // results in an array with both of the files
push [1 2 3] 4 // [1 2 3 4]
push [0 "Hello there!"] "General Kenobi!" // [0 "Hello there!" "General Kenobi!"]
push [["key" "val"]] ["key2" "val2"] // [["key" "val"] ["key2" "val2"]]
```

### filter
Create a new array with only elements where the filter function returns `true`

**Definition:**
```
\filter arr: any[] fun: (\any -> bool) -> any[]
```

**Examples:**
```
let a = filter [0 1 2 3] (\i -> (i as int) % 2 == 0)
print to_string (a as int[])  // [0 2]

\is_odd n: int -> bool
    return n % 2 == 1
let a = filter [0 1 2 3] is_even
print to_string (a as int[])  // [1 3]
```

### map
Maps an array into another array.

**Definition:**
```
\map arr: any[] fun: (\any -> any) -> any[]
```

**Examples:**
```
let a = map [true false] (\i -> not i)
a as bool[] // [false true]
```

### print
Prints into the standard output

**Definition:**
```
\print s: str -> void
```

**Examples:**
```
print to_array 2
print "Hello there!"
print "General Kenobi!"
```

### read
Read all the content of a file or standard input, as an array of strings.

**Definition:**
```
\read -> str[]
\read f: file -> str[]
```

**Examples:**
```
let s = read 
print s

let f = open "/file/path"
let s2 = read f
print s2
close f
```

### read_bin
Reads all the content of a file as an array of bytes.

**Definition:**
```
\read_bin f: file -> byte[]
```

**Examples:**
```
let f = open "/path/to/file" "b"
let b = read_bin f
print to_string b
close f
```

### reduce
Reduces an array of values into another value.

**Definition:**
```
\reduce arr: any[] fun: (\any any -> any) -> any
```

**Examples:**
```
let a = reduce [true false] (\i j -> i or j)
print to_string (a as bool)  // true

let a = reduce [1 0 1] (\i j -> i + j)
print to_string (a as int)  // 2
```

### to_string
Converts something into a string.

**Definition:**
```
\to_str int -> str
\to_str int[] -> str
\to_str float -> str
\to_str float[] -> str
\to_str double -> str
\to_str double[] -> str
\to_str byte -> str
\to_str byte[] -> str
\to_str file -> str
\to_str file[] -> str
\to_str any -> str
\to_str any[] -> str
\to_str bool -> str
\to_str bool[] -> str
```

**Examples:**
```
print to_str 1
print to_str [1 2 3]
print to_str 0.25
print to_str [0.25]
print to_str 0x1
print to_str [0x11 0x22 0x33]
let f = open "/path/to/file"
print to_str f // prints the name of the file
print to_str [(open "path1") (open "path2")] // prints the name of the files
let a: void = 1
print to_str a // will always print "[SOMETHING]"
print to_str [a a] // will always print "[ARRAY OF SOMETHING]"
print to_str true
print to_str [true false]
```

### write
Writes into a file and returns the number of bytes/characters written.

**Definition:**
```
\write f: file b: bytes[] -> int
\write f: file s: str[] -> int
```

**Examples:**
```
let f = open "/path/to/file" "wb"
let b = [0x01 0x02 0x03]
let written = write f b
print concat "Bytes written: " (to_string written)
close f

let f = open "/path/to/file" "w"
let s = ["Hello" "there!\n"]
let written = write f s
print concat "Bytes written: " (to_string written)
close f
```

### head
Gets the first elements of array.

**Definition:**
```
\head arr: any[] ->any
```

**Examples:**
```
print to_str (head [1 2 3]) as int // prints 1
```

### tail
Copy the array minus the first element.

**Definition:**
```
\tail arr: any[] -> any[]
```

**Examples:**
```
print to_str (tail [1 2 3]) as int[] // prints [2 3]
```

### open
Opens a file for read/write.

**Modes**
Modes can be combined.

| Mode | Description                                                               |
|------|---------------------------------------------------------------------------|
| r    | Open file for reading. If file doesn't exists an error will be generated. |
| w    | Open file for writing. If file doesn't exists a new one will be created.  |
| b    | Open file in binary format                                                |

**Definition:**
```
\open path: str -> file // assumes mode is 'wr'
\open path: str mode: str -> file
```

**Examples:**
```
let f = open "/path/to/file" 
let f2 = open "/path/to/file" "rb"
```

### close
Closes an open file. If the file was already closed an error will be generated.

**Definition:**
```
\close f: file -> void
```

**Examples:**
```
let f = open "/some/path"
close f
```

### is_empty
Return if an array is empty.

**Definition:**
```
\is_empty arr: any[] -> bool
```

**Examples:**
```
is_empty [] // true
is_empty [1 2 3] // false
is_empty ["hello" "there"] // false
```

### size_of
Returns the number of elements in an array.

**Definition:**
``` 
\size_of arr: any[] -> int
```

**Examples:**
```
size_of [1 2 3] // 3
size_of ["hello" 1 [true false]] // 3 
```

### concat
Concatenates two string or arrays.

**Definition:**
```
\concat a: str b: str -> str
\concat a: any[] b: any[] -> any[]
```

**Examples:**
```
concat "Hello " "there!" // "Hello there!"
concat [1 2 3] [4]  // [1 2 3 4]
```

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

\filter list: int[] predicate: (\int -> bool) -> int[]
    if is_empty list
        return []
    if predicate list[0]
        return concat [list[0]] (filter (tail list) predicate)
    else
        return filter (tail list) predicate
        
print filter [ 1, 2 ,3 ] is_odd
```