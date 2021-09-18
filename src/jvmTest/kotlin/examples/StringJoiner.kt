package examples

import com.github.adriantodt.tartar.api.lexer.Lexer
import com.github.adriantodt.tartar.api.parser.Grammar
import com.github.adriantodt.tartar.api.parser.Token
import com.github.adriantodt.tartar.classpathSource
import com.github.adriantodt.tartar.createGrammar
import com.github.adriantodt.tartar.createLexer
import com.github.adriantodt.tartar.createParser
import com.github.adriantodt.tartar.extensions.ensureEOF
import com.github.adriantodt.tartar.extensions.makeToken
import com.github.adriantodt.tartar.extensions.readString
import examples.TokenType.PLUS
import examples.TokenType.STRING

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
        // Create an infix parselet, with support to precedence as a lambda function.
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
