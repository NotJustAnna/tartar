package com.github.adriantodt.tartar.tests.regression.lexer

import com.github.adriantodt.tartar.api.dsl.CharPredicate
import com.github.adriantodt.tartar.api.lexer.Lexer
import com.github.adriantodt.tartar.api.lexer.Source
import com.github.adriantodt.tartar.api.parser.Token
import com.github.adriantodt.tartar.extensions.LexicalNumber
import com.github.adriantodt.tartar.extensions.makeToken
import com.github.adriantodt.tartar.extensions.readNumber
import kotlin.test.Test
import kotlin.test.assertEquals

class NumberParsingRegressionTests {
    @Test
    fun parseNumberWithDecimal() {
        val lexer = Lexer.create<Token<String>> {
            'A' { process(makeToken(it.toString())) }
            'B' { process(makeToken(it.toString())) }
            matching(CharPredicate.isDigit).configure {
                process(
                    when (val n = readNumber(it)) {
                        is LexicalNumber.Invalid -> makeToken("INVALID", n.string)
                        is LexicalNumber.Decimal -> makeToken("DECIMAL", n.value.toString())
                        is LexicalNumber.Integer -> makeToken("INTEGER", n.value.toString())
                    }
                )
            }
            ' '()
        }

        val list = lexer.parseToList(Source("A B 2.0"))

        assertEquals(3, list.size)
        assertEquals("A", list[0].type)
        assertEquals("B", list[1].type)
        assertEquals("2.0", list[2].value)
    }
}
