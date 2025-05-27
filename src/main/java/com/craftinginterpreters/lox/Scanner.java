package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.List;

import static com.craftinginterpreters.lox.TokenType.*;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    // points to the first character of the current lexeme being scanned
    private int start = 0;

    // points at the character currently being examined
    private int current = 0;

    // tracks what source line we're on
    private int line = 1;

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.

            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }
    /**
     * Checks if the current position is at the end of the source code.
     *
     * e.g. if the source is "var", and current is 3, then we're at the end.
     *
     * @return true if the current position is at the end of the source code, false otherwise
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     * Scans the next character in the source code and determines its type.
     * It recognizes single-character tokens, two-character tokens, and ignores whitespace.
     * If it encounters an unexpected character, it reports an error.
     */
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;

            // Ignore whitespace
            case ' ':
            case '\r':
            case '\t':
                break;

            case '\n':
                line++;
                break;
            case '/':
                if (match('/')) {
                    // A comment goes until the end of the line.
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;
            case '"': {
                string();
                break;
            }

            default:
                Lox.error(line, "Unexpected character.");
        }
    }


    /**
     * Checks if the next character in the source matches the expected character.
     * If it matches, advances the current position and returns true; otherwise, returns false.
     *
     * Used for recognizing two-character tokens like '!=', '==', '<=', '>='.
     *
     * Example:
     *   // Suppose source = "==", current = 0
     *   match('='); // returns true, current becomes 1
     *   match('='); // returns true, current becomes 2
     *   match('='); // returns false, current remains 2
     *
     * @param expected the character to match against the current character in the source
     * @return true if the next character matches and advances, false otherwise
     */
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    /**
     * Returns the character at the current position without advancing the current position.
     * If the current position is at the end of the source, it returns a null character ('\0').
     *
     * Example:
     *   // Suppose source = "hello", current = 0
     *   peek();    // returns 'h', current remains at 0
     *   advance(); // returns 'h', current becomes 1
     *   peek();    // returns 'e', current still at 1
     *
     * @return the character at the current position, or '\0' if at the end of the source
     */
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    /**
     * Advances the current position in the source code and returns the character at that position.
     * If the current position is at the end of the source, it returns a null character ('\0').
     *
     * After calling this method, the current position is incremented by one.
     *
     * Example:
     *   // Suppose source = "hello", current = 0
     *   advance(); // returns 'h', current becomes 1
     *   advance(); // returns 'e', current becomes 2
     *
     * @return the character at the current position, or '\0' if at the end of the source
     */
    private char advance() {
        return source.charAt(current++);
    }

    /**
     * Grabs the text of the current lexeme and creates a token of the given type.
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * Grabs the text of the current lexeme and creates a token of the given type.
     * Also takes a literal value to associate with the token.
     */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }
    }
}