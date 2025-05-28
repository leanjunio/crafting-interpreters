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

    @Test
    void unterminatedStringReportsError() {
        Scanner scanner = new Scanner("\"This is an unterminated string");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(1, tokens.size()); // Only EOF token should be present
        assertEquals(EOF, tokens.get(0).type);
    }

    @Test
    void validStringLiteralCreatesToken() {
        Scanner scanner = new Scanner("\"This is a string\"");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(2, tokens.size()); // STRING token + EOF
        assertEquals(STRING, tokens.get(0).type);
        assertEquals("This is a string", tokens.get(0).literal);
    }

    @Test
    void emptySourceProducesOnlyEOFToken() {
        Scanner scanner = new Scanner("");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(1, tokens.size()); // Only EOF token
        assertEquals(EOF, tokens.get(0).type);
    }

    @Test
    void handlesMultipleLinesCorrectly() {
        Scanner scanner = new Scanner("(\n)\n{\n}");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(5, tokens.size()); // 4 tokens + EOF
        assertEquals(LEFT_PAREN, tokens.get(0).type);
        assertEquals(RIGHT_PAREN, tokens.get(1).type);
        assertEquals(LEFT_BRACE, tokens.get(2).type);
        assertEquals(RIGHT_BRACE, tokens.get(3).type);
        assertEquals(EOF, tokens.get(4).type);
    }

    @Test
    void ignoresWhitespaceBetweenTokens() {
        Scanner scanner = new Scanner("  (   ) {   } ");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(5, tokens.size()); // 4 tokens + EOF
        assertEquals(LEFT_PAREN, tokens.get(0).type);
        assertEquals(RIGHT_PAREN, tokens.get(1).type);
        assertEquals(LEFT_BRACE, tokens.get(2).type);
        assertEquals(RIGHT_BRACE, tokens.get(3).type);
        assertEquals(EOF, tokens.get(4).type);
    }

    @Test
    void unterminatedCommentDoesNotCrashScanner() {
        Scanner scanner = new Scanner("/* This is an unterminated comment");
        assertDoesNotThrow(scanner::scanTokens);
    }

    @Test
    void validNumberLiteralCreatesToken() {
        Scanner scanner = new Scanner("12345");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(2, tokens.size()); // NUMBER token + EOF
        assertEquals(NUMBER, tokens.get(0).type);
        assertEquals(12345.0, tokens.get(0).literal);
    }

    @Test
    void validFloatingPointNumberCreatesToken() {
        Scanner scanner = new Scanner("123.456");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(2, tokens.size()); // NUMBER token + EOF
        assertEquals(NUMBER, tokens.get(0).type);
        assertEquals(123.456, tokens.get(0).literal);
    }

    @Test
    void malformedNumberReportsError() {
        Scanner scanner = new Scanner("123abc");
        assertDoesNotThrow(scanner::scanTokens);
    }

    @Test
    void validIdentifierCreatesToken() {
        Scanner scanner = new Scanner("variableName");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(2, tokens.size()); // IDENTIFIER token + EOF
        assertEquals(IDENTIFIER, tokens.get(0).type);
        assertEquals("variableName", tokens.get(0).lexeme);
    }

    @Test
    void unterminatedStringDoesNotCrashScanner() {
        Scanner scanner = new Scanner("\"Unterminated string");
        assertDoesNotThrow(scanner::scanTokens);
    }

    @Test
    void keywordsAreRecognizedCorrectly() {
        Scanner scanner = new Scanner("and class else false for fun if nil or print return super this true var while");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(17, tokens.size()); // 16 keywords + EOF
        assertEquals(AND, tokens.get(0).type);
        assertEquals(CLASS, tokens.get(1).type);
        assertEquals(ELSE, tokens.get(2).type);
        assertEquals(FALSE, tokens.get(3).type);
        assertEquals(FOR, tokens.get(4).type);
        assertEquals(FUN, tokens.get(5).type);
        assertEquals(IF, tokens.get(6).type);
        assertEquals(NIL, tokens.get(7).type);
        assertEquals(OR, tokens.get(8).type);
        assertEquals(PRINT, tokens.get(9).type);
        assertEquals(RETURN, tokens.get(10).type);
        assertEquals(SUPER, tokens.get(11).type);
        assertEquals(THIS, tokens.get(12).type);
        assertEquals(TRUE, tokens.get(13).type);
        assertEquals(VAR, tokens.get(14).type);
        assertEquals(WHILE, tokens.get(15).type);
    }

    @Test
    void keywordsAndIdentifiersAreDifferentiated() {
        Scanner scanner = new Scanner("var variable if ifstatement class classname");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(7, tokens.size()); // 6 tokens + EOF
        assertEquals(VAR, tokens.get(0).type);
        assertEquals(IDENTIFIER, tokens.get(1).type);
        assertEquals(IF, tokens.get(2).type);
        assertEquals(IDENTIFIER, tokens.get(3).type);
        assertEquals(CLASS, tokens.get(4).type);
        assertEquals(IDENTIFIER, tokens.get(5).type);
    }
}

