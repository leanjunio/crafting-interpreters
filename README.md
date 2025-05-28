# Crafting Interpreters - Java Lox Implementation

This project is a Java implementation of the Lox language, as described in the book [Crafting Interpreters](https://craftinginterpreters.com/) by Bob Nystrom. It includes a scanner (lexer) and supporting classes for tokenizing Lox source code.

## Project Structure

- `src/main/java/com/craftinginterpreters/lox/`  
  Core source files for the Lox interpreter, including:
  - `Lox.java`: Main entry point for running the interpreter.
  - `Scanner.java`: Lexical analyzer for Lox source code.
  - `Token.java`, `TokenType.java`: Token representation and types.
- `src/test/java/com/craftinginterpreters/lox/`  
  Unit tests for the scanner and related functionality.
- `pom.xml`  
  Maven build configuration.

## Requirements

- Java 17 or higher
- Maven 3.x

## Building and Running

To build the project and run all tests:

```sh
mvn clean test
```

To run the interpreter on a Lox source file:

```sh
mvn compile
java -cp target/classes com.craftinginterpreters.lox.Lox path/to/script.lox
```

Or to start an interactive prompt:

```sh
mvn compile
java -cp target/classes com.craftinginterpreters.lox.Lox
> 2 + 2
NUMBER 2 2.0
PLUS + null
NUMBER 2 2.0
EOF  null
> if i == x
IF if null
IDENTIFIER i null
EQUAL_EQUAL == null
IDENTIFIER x null
EOF  null
> 

```

## Testing

Unit tests are written using JUnit 5. To run the tests:

```sh
mvn test
```

## References

- [Crafting Interpreters Book](https://craftinginterpreters.com/)
- [JUnit 5 Documentation](https://junit.org/junit5/)

