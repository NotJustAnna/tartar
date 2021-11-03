package net.notjustanna.tartar.api.grammar

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.Token

/**
 * A interface for infix-based parsing, with support to precedence.
 *
 * @param T The grammar's token type.
 * @param E The grammar's expression result.
 * @author An Tran
 */
public interface InfixParselet<T, E> {
    /**
     * This infix parser's precedence.
     */
    public val precedence: Int

    /**
     * This infix parser's parsing implementation.
     */
    public fun parse(ctx: ParserContext<T, E>, left: E, token: Token<T>): E
}