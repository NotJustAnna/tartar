package net.notjustanna.tartar.tests.regression.lexer

import net.notjustanna.tartar.api.lexer.Lexer
import net.notjustanna.tartar.api.lexer.Source
import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.tartar.extensions.token
import net.notjustanna.tartar.extensions.section
import kotlin.test.Test
import kotlin.test.assertEquals

class SectionRegressionTests {
    @Test
    fun tokenSectionSubstringOutOfBounds() {
        val lexer = Lexer.create<Token<Char>> {
            'A' { process(token(it)) }
            'B' { process(token(it)) }
            ' '()
        }

        val list = lexer.parseToList(Source("A B"))

        assertEquals(2, list.size)
        assertEquals('A', list[0].type)
        assertEquals('B', list[1].type)
    }

    @Test
    fun lexerContextSectionOutOfBounds() {
        val lexer = Lexer.create<String> {
            configure {
                this.nextString(5)
                process(section(2, 4).substring)
                while (this.hasNext()) this.next()
            }
        }

        val list = lexer.parseToList(Source("  |AABB|CCDD|  "))
        assertEquals(listOf("AABB"), list)
    }
}
