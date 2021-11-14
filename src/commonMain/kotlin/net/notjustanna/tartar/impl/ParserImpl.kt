package net.notjustanna.tartar.impl

import net.notjustanna.tartar.api.dsl.ParserFunction
import net.notjustanna.tartar.api.grammar.Grammar
import net.notjustanna.tartar.api.lexer.Source
import net.notjustanna.tartar.api.parser.*

internal class ParserImpl<T, K: Token<T>, E, R>(
    override val grammar: Grammar<T, K, E>,
    private val block: ParserFunction<T, K, E, R>
) : Parser<T, K, E, R> {

    override fun parse(source: Source, tokens: List<K>): R {
        return ContextImpl(source, tokens, grammar).block()
    }

    private inner class ContextImpl(
        override val source: Source,
        tokens: List<K>,
        override val grammar: Grammar<T, K, E>
    ) : ParserContext<T, K, E> {
        inner class ChildContextImpl(override val grammar: Grammar<T, K, E>) : ParserContext<T, K, E> by this {
            override fun parseExpression(precedence: Int): E = parseExpr(grammar, precedence)
        }

        private val tokens = tokens.toMutableList()

        override var index: Int = 0

        override val eof get() = index == tokens.size

        override val last get() = tokens[index - 1]

        override fun withGrammar(grammar: Grammar<T, K, E>) = ChildContextImpl(grammar)

        override fun parseExpression(precedence: Int): E = parseExpr(grammar, precedence)

        override fun eat(): K {
            if (eof) throw SyntaxException("Expected token but reached end of file", last.section)
            return tokens[index++]
        }

        override fun eat(type: T): K {
            if (eof) throw SyntaxException("Expected $type but reached end of file", last.section)
            val token = peek()
            if (token.type != type) {
                throw SyntaxException("Expected '$type' but found '${token.type}'.", token.section)
            }
            return eat()
        }

        override fun match(type: T): Boolean {
            val shouldEat = nextIs(type)
            if (shouldEat) eat()
            return shouldEat
        }

        override fun matchAny(vararg type: T): Boolean {
            val shouldEat = nextIsAny(*type)
            if (shouldEat) eat()
            return shouldEat
        }

        override fun back() = tokens[--index]

        override fun peek(distance: Int) = tokens[index + distance]

        override fun nextIs(type: T) = !eof && peek().type == type

        override fun nextIsAny(vararg types: T) = !eof && types.any { nextIs(it) }

        override fun peekAheadUntil(vararg type: T): List<K> {
            if (eof) return emptyList()
            val list = mutableListOf<K>()
            val lastIndex = index
            while (!eof && !nextIsAny(*type)) list += eat()
            index = lastIndex
            return list
        }

        override fun skipUntil(vararg type: T) {
            while (!eof && !nextIsAny(*type)) eat()
        }

        private fun parseExpr(grammar: Grammar<T, K, E>, precedence: Int): E {
            var left: E = eat().let {
                grammar.prefix[it.type]?.parse(this, it)
                    ?: throw SyntaxException("Unexpected $it", it.section)
            }
            while (!eof && precedence < (this.grammar.infix[this.peek(0).type]?.precedence ?: 0)) {
                left = eat().let {
                    grammar.infix[it.type]?.parse(this, left, it)
                        ?: throw SyntaxException("Unexpected $it", it.section)
                }
            }
            return left
        }
    }
}
