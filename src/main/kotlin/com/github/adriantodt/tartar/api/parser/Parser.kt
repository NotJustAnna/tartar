package com.github.adriantodt.tartar.api.parser

import com.github.adriantodt.tartar.api.lexer.Lexer
import com.github.adriantodt.tartar.api.lexer.Source

/**
 * Reads tokens from a list and parses it with a pratt-parser.
 *
 * @param T The parser's (and grammar's) token type.
 * @param E The parser's (and grammar's) expression result.
 * @param R The parser's result.
 * @author AdrianTodt
 */
interface Parser<T, E, R> {
    /**
     * The [Grammar] of this pratt-parser.
     */
    val grammar: Grammar<T, E>

    /**
     * Parses tokens with this pratt-parser and returns the computed result.
     *
     * @param source A source of characters.
     * @param tokens A list of tokens, probably created with [Lexer].
     * @return The computed result.
     */
    fun parse(source: Source, tokens: List<Token<T>>): R

    /**
     * Parses tokens from a source, using a specified lexer, with this pratt-parser, and returns the computed result.
     *
     * @param source A source of characters.
     * @param lexer A lexer to parse the source.
     * @return The computed result.
     */
    fun parse(source: Source, lexer: Lexer<Token<T>>): R {
        return parse(source, lexer.parseToList(source))
    }
}