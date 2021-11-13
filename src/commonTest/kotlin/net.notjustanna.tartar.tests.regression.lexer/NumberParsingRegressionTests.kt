package net.notjustanna.tartar.tests.regression.lexer

import net.notjustanna.tartar.api.dsl.CharPredicate
import net.notjustanna.tartar.api.lexer.Lexer
import net.notjustanna.tartar.api.lexer.Source
import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.tartar.extensions.LexicalNumber
import net.notjustanna.tartar.extensions.processToken
import net.notjustanna.tartar.extensions.readNumber
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
