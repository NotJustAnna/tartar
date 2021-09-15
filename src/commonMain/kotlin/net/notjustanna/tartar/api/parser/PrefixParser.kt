package net.notjustanna.tartar.api.parser

/**
 * A interface for prefix-based parsing.
 *
 * @param T The grammar's token type.
 * @param E The grammar's expression result.
 * @author An Tran
 */
interface PrefixParser<T, E> {
    /**
     * This prefix parser's parsing implementation.
     */
    fun parse(parser: ParserContext<T, E>, token: Token<T>): E
}
