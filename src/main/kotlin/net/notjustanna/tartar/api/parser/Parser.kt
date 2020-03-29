package net.notjustanna.tartar.api.parser

import net.notjustanna.tartar.api.lexer.Lexer
import net.notjustanna.tartar.api.lexer.Source

/**
 * Reads tokens from a list and parses it with a pratt-parser.
 *
 * @param T The parser's (and grammar's) token type.
 * @param E The parser's (and grammar's) expression result.
 * @param R The parser's result.
 * @author NotJustAnna
 */
interface Parser<T, E, R> {
    /**
     * The [Grammar] of this pratt-parser.
     */
    val grammar: Grammar<T, E>

    /**
     * Parses tokens with this pratt-parser and returns the computed result.
     *
     * @param tokens A list of tokens, probably created with [Lexer].
     * @return The computed result.
     */
    fun parse(tokens: List<Token<T>>): R

    /**
     * Parses tokens from a source, using a specified lexer, with this pratt-parser, and returns the computed result.
     *
     * @param source A source of characters.
     * @param lexer A lexer to parse the source.
     * @return The computed result.
     */
    fun parse(source: Source, lexer: Lexer<Token<T>>): R {
        return parse(lexer.parseToList(source))
    }
}