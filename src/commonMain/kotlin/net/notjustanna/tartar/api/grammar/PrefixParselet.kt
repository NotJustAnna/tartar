package net.notjustanna.tartar.api.grammar

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.Token

/**
 * A interface for prefix-based parsing.
 *
 * @param T The grammar's token type.
 * @param E The grammar's expression result.
 * @author An Tran
 */
public interface PrefixParselet<T, K: Token<T>, E> {
    /**
     * This prefix parser's parsing implementation.
     */
    public fun parse(ctx: ParserContext<T, K, E>, token: K): E
}
