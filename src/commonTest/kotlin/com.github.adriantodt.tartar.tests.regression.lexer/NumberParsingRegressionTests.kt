package com.github.adriantodt.tartar.tests.regression.lexer

import com.github.adriantodt.tartar.api.dsl.CharPredicate
import com.github.adriantodt.tartar.api.lexer.Lexer
import com.github.adriantodt.tartar.api.lexer.Source
import com.github.adriantodt.tartar.api.parser.Token
import com.github.adriantodt.tartar.extensions.LexicalNumber
import com.github.adriantodt.tartar.extensions.processToken
import com.github.adriantodt.tartar.extensions.readNumber
import kotlin.test.Test
import kotlin.test.assertEquals

class NumberParsingRegressionTests {
    @Test
    fun parseNumberWithDecimal() {
        val lexer = Lexer.create<Token<String>> {
            'A' { processToken(it.toString()) }
            'B' { processToken(it.toString()) }
            matching(CharPredicate.isDigit).configure {
                when (val n = readNumber(it)) {
                    is LexicalNumber.Invalid -> processToken("INVALID", n.string)
                    is LexicalNumber.Decimal -> processToken("DECIMAL", n.value.toString(), n.string.length)
                    is LexicalNumber.Integer -> processToken("INTEGER", n.value.toString(), n.string.length)
                }
            }
            ' '()
        }

        val list = lexer.parseToList(Source("A B 2.0"))

        assertEquals(3, list.size)
        assertEquals("A", list[0].type)
        assertEquals("A", list[0].section.substring)
        assertEquals("B", list[1].type)
        assertEquals("B", list[1].section.substring)
        assertEquals(2.0, list[2].value.toDoubleOrNull())
        assertEquals("2.0", list[2].section.substring)
    }
}
