package com.github.adriantodt.tartar.api.parser

/**
 * A interface for infix-based parsing, with support to precedence.
 *
 * @param T The grammar's token type.
 * @param E The grammar's expression result.
 * @author An Tran
 */
interface InfixParser<T, E> {
    /**
     * This infix parser's precedence.
     */
    val precedence: Int

    /**
     * This infix parser's parsing implementation.
     */
    fun parse(ctx: ParserContext<T, E>, left: E, token: Token<T>): E
}
