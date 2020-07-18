package com.github.adriantodt.tartar.impl

import com.github.adriantodt.tartar.api.Closure
import com.github.adriantodt.tartar.api.lexer.Source
import com.github.adriantodt.tartar.api.parser.*

class ParserImpl<T, E, R>(
    override val grammar: Grammar<T, E>,
    private val block: Closure<ParserContext<T, E>, R>
) : Parser<T, E, R> {

    override fun parse(source: Source, tokens: List<Token<T>>): R {
        return ContextImpl(source, tokens, grammar).block()
    }

    inner class ContextImpl(
        override val source: Source,
        tokens: List<Token<T>>,
        override val grammar: Grammar<T, E>
    ) : ParserContext<T, E> {
        inner class ChildContextImpl(override val grammar: Grammar<T, E>) : ParserContext<T, E> by this {
            override fun parseExpression(precedence: Int): E = parseExpr(grammar, precedence)
        }

        private val tokens = tokens.toMutableList()

        override var index: Int = 0
            private set

        override val eof get() = index == tokens.size

        override val last get() = tokens[index - 1]

        override fun withGrammar(grammar: Grammar<T, E>) = ChildContextImpl(grammar)

        override fun parseExpression(precedence: Int): E = parseExpr(grammar, precedence)

        override fun parseExpressionInfix(expression: E, precedence: Int): E = parseInfix(expression, grammar, precedence)

        override fun eat(): Token<T> {
            if (eof) throw SyntaxException("Expected token but reached end of file", last.section)
            return tokens[index++]
        }

        override fun eat(type: T): Token<T> {
            if (eof) throw SyntaxException("Expected $type but reached end of file", last.section)
            val token = peek()
            if (token.type != type) {
                throw SyntaxException("Expected '$type' but found '${token.type}'.", token.section)
            }
            return eat()
        }

        override fun match(type: T): Boolean {
            return if (nextIs(type)) {
                eat()
                true
            } else {
                false
            }
        }

        override fun matchAny(vararg type: T): Boolean {
            return if (nextIsAny(*type)) {
                eat()
                true
            } else {
                false
            }
        }

        override fun back() = tokens[--index]

        override fun peek(distance: Int) = tokens[index + distance]

        override fun nextIs(type: T) = !eof && peek().type == type

        override fun nextIsAny(vararg types: T) = !eof && types.any { nextIs(it) }

        override fun peekAheadUntil(vararg type: T): List<Token<T>> {
            if (eof) return emptyList()
            val list = mutableListOf<Token<T>>()
            val lastIndex = index
            while (!eof && !nextIsAny(*type)) {
                list += eat()
            }
            index = lastIndex
            return list
        }

        override fun skipUntil(vararg type: T) {
            while (!eof && !nextIsAny(*type)) {
                eat()
            }
        }

        fun parseExpr(grammar: Grammar<T, E>, precedence: Int): E {
            return parseInfix(parsePrefix(grammar, precedence), grammar, precedence)
        }

        fun parsePrefix(grammar: Grammar<T, E>, precedence: Int): E {
            return eat().let {
                grammar.prefixParsers[it.type]?.parse(this, it)
                    ?: throw SyntaxException("Unexpected $it", it.section)
            }
        }

        fun parseInfix(expression: E, grammar: Grammar<T, E>, precedence: Int): E {
            var left = expression
            while (!eof && precedence < this.grammar.infixParsers[this.peek(0).type]?.precedence ?: 0) {
                left = eat().let {
                    grammar.infixParsers[it.type]?.parse(this, expression, it)
                        ?: throw SyntaxException("Unexpected $it", it.section)
                }
            }
            return left
        }
    }
}