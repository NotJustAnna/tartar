package net.notjustanna.tartar.tests.regression.lexer

import net.notjustanna.tartar.api.lexer.Lexer
import net.notjustanna.tartar.api.lexer.Source
import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.tartar.extensions.makeToken
import kotlin.test.Test
import kotlin.test.assertEquals

class SectionRegressionTests {
    @Test
    fun tokenSectionSubstringOutOfBounds() {
        val lexer = Lexer.create<Token<Char>> {
            'A' { process(makeToken(it)) }
            'B' { process(makeToken(it)) }
            ' '()
        }

        val list = lexer.parseToList(Source("A B"))

        assertEquals(2, list.size)
        assertEquals('A', list[0].type)
        assertEquals('B', list[1].type)
    }
}
