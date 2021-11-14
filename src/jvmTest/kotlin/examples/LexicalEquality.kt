package examples

import com.github.adriantodt.tartar.api.dsl.CharPredicate
import com.github.adriantodt.tartar.api.lexer.Lexer
import com.github.adriantodt.tartar.api.lexer.Source
import com.github.adriantodt.tartar.api.lexer.classpath
import com.github.adriantodt.tartar.api.parser.StringToken
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token
import com.github.adriantodt.tartar.extensions.lexer.*
import examples.extra.CTokenType
import examples.extra.CTokenType.*

fun main() {
    /*
     * A minimal subset of C was implemented.
     */
    val lexer = Lexer.create<Token<CTokenType>> {
        // NOOP
        ' '()
        '\n'()

        // Tokens
        '+' { processToken(PLUS) }
        '-' { processToken(MINUS) }
        '*' { processToken(TIMES) }
        "++" { processToken(INCREMENT, 2) }
        "--" { processToken(DECREMENT, 2) }
        '=' { processToken(ASSIGN) }
        "==" { processToken(EQ, 2) }
        '>' { processToken(GT) }
        '<' { processToken(LT) }
        ">=" { processToken(GE, 2) }
        "<=" { processToken(LE, 2) }
        ';' { processToken(SEMICOLON) }
        '(' { processToken(LPAREN) }
        ')' { processToken(RPAREN) }
        '{' { processToken(LBRACKET) }
        '}' { processToken(RBRACKET) }
        matching(CharPredicate.isDigit).configure {
            when (val n = readNumber(it)) {
                is LexicalNumber.Decimal -> processToken(NUMBER, n.string)
                is LexicalNumber.Integer -> processToken(NUMBER, n.string)
                else -> throw SyntaxException("Illegal number", section(n.string.length))
            }
        }
        matching { it.isLetter() || it == '_' }.configure {
            processToken(IDENTIFIER, readIdentifier(it))
        }
    }

    val list1 = lexer.parseToList(Source.classpath { "input.c" })
    val list2 = lexer.parseToList(Source.classpath { "input.min.c" }.copy(name = "input.c"))

    // Compares types and values, but not sections.
    val isEqual = list1.zip(list2).all { (o1, o2) ->
        o1.type == o2.type && o1::class == o2::class && (o1 !is StringToken || o2 !is StringToken || o1.value == o2.value)
    }
    println(isEqual)
}
