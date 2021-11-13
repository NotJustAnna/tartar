package examples

import net.notjustanna.tartar.api.grammar.Grammar
import net.notjustanna.tartar.api.lexer.Lexer
import net.notjustanna.tartar.api.lexer.Source
import net.notjustanna.tartar.api.lexer.classpath
import net.notjustanna.tartar.api.parser.SourceParser
import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.tartar.extensions.*
import examples.TokenType.PLUS
import examples.TokenType.STRING

private enum class TokenType {
    PLUS,
    STRING
}

fun main() {
    // Create a lexer as simple as a method call.
    val lexer = Lexer.create<Token<TokenType>> {
        // Implement a token type per line.
        '+' { processToken(PLUS) }
        // Built-in extension functions.
        '"' { processToken(STRING, readString(it), offset = 2) }
        // NOOP tokens.
        ' '()
        '\n'()
    }

    // Create a pratt-parser grammar. Dead simple.
    val grammar = Grammar.create<TokenType, String> {
        // Create a prefix parselet as a lambda function.
        prefix(STRING) { token -> token.value }
        // Create an infix parselet, with support to precedence as a lambda function.
        infix(PLUS, 1) { left, _ -> left + parseExpression() }
    }

    // Use your grammar to create a pratt-parser.
    val parser = SourceParser.create(lexer, grammar) { // Actual code run by the parser.
        // Extension function: Throws if there's still tokens.
        ensureEOF {
            // Parses a expression using this parsers' grammar.
            parseExpression()
        }
    }

    // One line of code to rule them all.
    val result = parser.parse(Source.classpath { "input.str" })
    println(result)
}
