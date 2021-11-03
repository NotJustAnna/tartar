package net.notjustanna.tartar.api.parser

import net.notjustanna.tartar.api.dsl.ParserFunction
import net.notjustanna.tartar.api.grammar.Grammar
import net.notjustanna.tartar.api.lexer.Lexer
import net.notjustanna.tartar.api.lexer.Source
import net.notjustanna.tartar.impl.ParserImpl
import kotlin.jvm.JvmStatic

/**
 * Reads tokens from a list and parses it with a pratt-parser.
 *
 * @param T The parser's (and grammar's) token type.
 * @param E The parser's (and grammar's) expression result.
 * @param R The parser's result.
 * @author NotJustAnna
 */
public interface Parser<T, E, R> {
    /**
     * The [Grammar] of this pratt-parser.
     */
    public val grammar: Grammar<T, E>

    /**
     * Parses tokens with this pratt-parser and returns the computed result.
     *
     * @param source A source of characters.
     * @param tokens A list of tokens, probably created with [Lexer].
     * @return The computed result.
     */
    public fun parse(source: Source, tokens: List<Token<T>>): R

    /**
     * Parses tokens from a source, using a specified lexer, with this pratt-parser, and returns the computed result.
     *
     * @param source A source of characters.
     * @param lexer A lexer to parse the source.
     * @return The computed result.
     */
    public fun parse(source: Source, lexer: Lexer<Token<T>>): R {
        return parse(source, lexer.parseToList(source))
    }

    public companion object {
        /**
         * Creates and configures a [Parser].
         *
         * @param T The parser's (and grammar's) token type.
         * @param E The parser's (and grammar's) expression result.
         * @param R The parser's result.
         * @param grammar The grammar used by this parser.
         * @param block The parser function.
         * @return A configured Parser.
         * @author NotJustAnna
         */
        @JvmStatic
        public fun <T, E, R> create(grammar: Grammar<T, E>, block: ParserFunction<T, E, R>): Parser<T, E, R> {
            return ParserImpl(grammar, block)
        }
    }
}
