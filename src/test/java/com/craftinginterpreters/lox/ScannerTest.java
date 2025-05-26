package com.craftinginterpreters.lox;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.craftinginterpreters.lox.TokenType.*;

class ScannerTest {
    @Test
    void testSingleCharacterTokens() {
        Scanner scanner = new Scanner("(){},.-+;*");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(11, tokens.size()); // 10 tokens + EOF
        assertEquals(LEFT_PAREN, tokens.get(0).type);
        assertEquals(RIGHT_PAREN, tokens.get(1).type);
        assertEquals(LEFT_BRACE, tokens.get(2).type);
        assertEquals(RIGHT_BRACE, tokens.get(3).type);
        assertEquals(COMMA, tokens.get(4).type);
        assertEquals(DOT, tokens.get(5).type);
        assertEquals(MINUS, tokens.get(6).type);
        assertEquals(PLUS, tokens.get(7).type);
        assertEquals(SEMICOLON, tokens.get(8).type);
        assertEquals(STAR, tokens.get(9).type);
        assertEquals(EOF, tokens.get(10).type);
    }

    @Test
    void testTwoCharacterTokens() {
        Scanner scanner = new Scanner("!= == <= >= = < > !");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(BANG_EQUAL, tokens.get(0).type);
        assertEquals(EQUAL_EQUAL, tokens.get(1).type);
        assertEquals(LESS_EQUAL, tokens.get(2).type);
        assertEquals(GREATER_EQUAL, tokens.get(3).type);
        assertEquals(EQUAL, tokens.get(4).type);
        assertEquals(LESS, tokens.get(5).type);
        assertEquals(GREATER, tokens.get(6).type);
        assertEquals(BANG, tokens.get(7).type);
    }

    @Test
    void testSlashAndComment() {
        Scanner scanner = new Scanner("/ // this is a comment\n+");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(SLASH, tokens.get(0).type);
        assertEquals(PLUS, tokens.get(1).type);
    }

    @Test
    void testWhitespaceAndNewlines() {
        Scanner scanner = new Scanner("( \n )\t{ }");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(LEFT_PAREN, tokens.get(0).type);
        assertEquals(RIGHT_PAREN, tokens.get(1).type);
        assertEquals(LEFT_BRACE, tokens.get(2).type);
        assertEquals(RIGHT_BRACE, tokens.get(3).type);
    }

    @Test
    void testUnexpectedCharacter() {
        // Should call Lox.error, but here we just check that it doesn't throw
        Scanner scanner = new Scanner("$");
        assertDoesNotThrow(scanner::scanTokens);
    }
}

