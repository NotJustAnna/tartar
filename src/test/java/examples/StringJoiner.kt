package examples

import examples.TokenType.PLUS
import examples.TokenType.STRING
import net.notjustanna.tartar.api.lexer.Lexer
import net.notjustanna.tartar.api.parser.Grammar
import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.tartar.classpathSource
import net.notjustanna.tartar.createGrammar
import net.notjustanna.tartar.createLexer
import net.notjustanna.tartar.createParser
import net.notjustanna.tartar.extensions.ensureEOF
import net.notjustanna.tartar.extensions.makeToken
import net.notjustanna.tartar.extensions.readString

private enum class TokenType {
    PLUS,
    STRING
}

fun main() {
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
        prefix(STRING) { token -> token.value }
        // Create a infix parselet, with support to precedence as a lambda function.
        infix(PLUS, 1) { left, _ -> left + parseExpression() }
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
    val result = parser.parse(classpathSource { "input.str" }, lexer)
    println(result)
}
