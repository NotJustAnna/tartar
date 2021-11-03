package net.notjustanna.tartar.api.parser

import net.notjustanna.tartar.api.dsl.ParserFunction
import net.notjustanna.tartar.api.grammar.Grammar
import net.notjustanna.tartar.api.lexer.Lexer
import net.notjustanna.tartar.api.lexer.Source

/**
 * Reads characters from a [Source] with a [lexer][Lexer], parsing the resulting tokens with a [pratt-parser][Parser].
 *
 * @param T The parser's (and grammar's) token type.
 * @param E The parser's (and grammar's) expression result.
 * @param R The parser's result.
 * @param lexer The underlying lexer.
 * @param parser The underlying parser.
 * @author NotJustAnna
 */
public data class SourceParser<T, E, R>(public val lexer: Lexer<Token<T>>, public val parser: Parser<T, E, R>) {
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
         * @author NotJustAnna
         */
        public fun <T, E, R> create(
            lexer: Lexer<Token<T>>,
            grammar: Grammar<T, E>,
            block: ParserFunction<T, E, R>
        ): SourceParser<T, E, R> {
            return SourceParser(lexer, Parser.create(grammar, block))
        }
    }
}
