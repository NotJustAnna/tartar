package com.github.adriantodt.tartar.tests.regression.lexer

import com.github.adriantodt.tartar.api.lexer.Lexer
import com.github.adriantodt.tartar.api.lexer.Source
import com.github.adriantodt.tartar.api.parser.Token
import com.github.adriantodt.tartar.extensions.makeToken
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
