package examples

import net.notjustanna.tartar.api.dsl.CharPredicate
import net.notjustanna.tartar.api.lexer.Lexer
import net.notjustanna.tartar.api.lexer.Source
import net.notjustanna.tartar.api.lexer.classpath
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.tartar.extensions.*
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
        '+' { process(makeToken(PLUS)) }
        '-' { process(makeToken(MINUS)) }
        '*' { process(makeToken(TIMES)) }
        "++" { process(makeToken(INCREMENT)) }
        "--" { process(makeToken(DECREMENT)) }
        '=' { process(makeToken(ASSIGN)) }
        "==" { process(makeToken(EQ)) }
        '>' { process(makeToken(GT)) }
        '<' { process(makeToken(LT)) }
        ">=" { process(makeToken(GE)) }
        "<=" { process(makeToken(LE)) }
        ';' { process(makeToken(SEMICOLON)) }
        '(' { process(makeToken(LPAREN)) }
        ')' { process(makeToken(RPAREN)) }
        '{' { process(makeToken(LBRACKET)) }
        '}' { process(makeToken(RBRACKET)) }
        matching(CharPredicate.isDigit).configure {
            when (val n = readNumber(it)) {
                is LexicalNumber.Decimal -> process(makeToken(NUMBER, n.string))
                is LexicalNumber.Integer -> process(makeToken(NUMBER, n.string))
                else -> throw SyntaxException("Illegal number", section(n.string.length))
            }
        }
        matching { it.isLetter() || it == '_' }.configure {
            process(makeToken(IDENTIFIER, readIdentifier(it)))
        }
    }

    val list1 = lexer.parseToList(Source.classpath { "input.c" })
    val list2 = lexer.parseToList(Source.classpath { "input.min.c" })

    // Compares types and values, but not sections.
    val isEqual = list1.zip(list2).all { (o1, o2) -> o1.type == o2.type && o1.value == o2.value }
    println(isEqual)
}
