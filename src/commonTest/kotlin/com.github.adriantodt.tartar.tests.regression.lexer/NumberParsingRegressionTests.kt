package com.github.adriantodt.tartar.tests.regression.lexer

import com.github.adriantodt.tartar.api.dsl.CharPredicate
import com.github.adriantodt.tartar.api.lexer.Lexer
import com.github.adriantodt.tartar.api.lexer.Source
import com.github.adriantodt.tartar.api.parser.StringToken
import com.github.adriantodt.tartar.api.parser.Token
import com.github.adriantodt.tartar.extensions.lexer.LexicalNumber
import com.github.adriantodt.tartar.extensions.lexer.processToken
import com.github.adriantodt.tartar.extensions.lexer.readNumber
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

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

        val (first, second, third) = list
        assertEquals("A", first.type)
        assertEquals("A", first.section?.substring)
        assertEquals("B", second.type)
        assertEquals("B", second.section?.substring)
        assertIs<StringToken<String>>(third)
        assertEquals(2.0, third.value.toDoubleOrNull())
        assertEquals("2.0", third.section?.substring)
    }
}
