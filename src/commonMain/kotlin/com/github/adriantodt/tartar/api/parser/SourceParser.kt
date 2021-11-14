package com.github.adriantodt.tartar.api.parser

import com.github.adriantodt.tartar.api.dsl.ParserFunction
import com.github.adriantodt.tartar.api.grammar.Grammar
import com.github.adriantodt.tartar.api.lexer.Lexer
import com.github.adriantodt.tartar.api.lexer.Source

/**
 * Reads characters from a [Source] with a [lexer][Lexer], parsing the resulting tokens with a [pratt-parser][Parser].
 *
 * @param T The parser's (and grammar's) token type.
 * @param E The parser's (and grammar's) expression result.
 * @param R The parser's result.
 * @param lexer The underlying lexer.
 * @param parser The underlying parser.
 * @author AdrianTodt
 */
public data class SourceParser<T, K : Token<T>, E, R>(
    public val lexer: Lexer<K>,
    public val parser: Parser<T, K, E, R>
) {
    /**
     * Parses tokens from a source, using the bundled lexer, with this pratt-parser, and returns the computed result.
     *
     * @param source A source of characters.
     * @return The computed result.
     */
    public fun parse(source: Source): R {
        return parser.parse(source, lexer)
    }

    public companion object {
        /**
         * Creates and configures a [SourceParser].
         *
         * @param T The parser's (and grammar's) token type.
         * @param E The parser's (and grammar's) expression result.
         * @param R The parser's result.
         * @param lexer The underlying parser.
         * @param grammar The grammar used by this parser.
         * @param block The parser function.
         * @return A configured Parser.
         * @author AdrianTodt
         */
        public fun <T, K: Token<T>, E, R> create(
            lexer: Lexer<K>,
            grammar: Grammar<T, K, E>,
            block: ParserFunction<T, K, E, R>
        ): SourceParser<T, K, E, R> {
            return SourceParser(lexer, Parser.create(grammar, block))
        }
    }
}
