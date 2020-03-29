# tartar
Kotlin lexical analysis and pratt-parsing as a DSL

### Example code
```kotlin
// Create a lexer as simple as a method call.
val lexer: Lexer<Token<TokenType>> = createLexer {
    // Implement a token type per line.
    '+' { process(makeToken(PLUS)) }
    // Built-in extension functions.
    '"' { process(makeToken(STRING, readString(it), offset = 1)) }
    // NOOP tokens.
    ' '()
    '\n'()
}

// Create a pratt-parser grammar. Dead simple.
val grammar: Grammar<TokenType, String> = createGrammar {
    // Create a prefix parselet as a lambda function.
    prefix(STRING) { token -> token.value.removeSurrounding("") }
    // Create a infix parselet, with support to precedence as a lambda function.
    infix(PLUS, 1) { left, token -> left + parseExpression() }
}

// Use your grammar to create a pratt-parser.
val parser = createParser(grammar) { // Actual code run by the parser. 
    // Extension function: Throws if there's still tokens.
    ensureEOF { 
        // Parses a expression using this parsers' grammar.
        parseExpression()
    }
}

// One line of code to rule them all.
val result = parser.parse(classpathSource { "input.str" }, lexer) // "tartar is awesome"
```