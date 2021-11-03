package com.github.adriantodt.tartar.api.grammar

import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.Token

/**
 * A interface for prefix-based parsing.
 *
 * @param T The grammar's token type.
 * @param E The grammar's expression result.
 * @author An Tran
 */
public interface PrefixParselet<T, E> {
    /**
     * This prefix parser's parsing implementation.
     */
    public fun parse(ctx: ParserContext<T, E>, token: Token<T>): E
}
