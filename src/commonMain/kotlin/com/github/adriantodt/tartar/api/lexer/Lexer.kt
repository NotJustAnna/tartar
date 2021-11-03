package com.github.adriantodt.tartar.api.lexer

import com.github.adriantodt.tartar.api.dsl.LexerConfig
import com.github.adriantodt.tartar.impl.LexerImpl
import com.github.adriantodt.tartar.impl.MatcherImpl
import kotlin.jvm.JvmStatic

/**
 * Reads characters from a [Source] and outputs tokens.
 *
 * @param T The type of tokens the lexer generates.
 * @author AdrianTodt
 */
public interface Lexer<T> {
    /**
     * Parses a source and outputs tokens into a consumer.
     *
     * @param source A source of characters.
     * @param output The consumer of tokens.
     */
    public fun parse(source: Source, output: (T) -> Unit)

    /**
     * Parses a source and adds all tokens into a collection.
     *
     * @param source A source of characters.
     * @param collection The collection to add all tokens into.
     * @return The collection with the tokens.
     */
    public fun <C : MutableCollection<in T>> parseTo(source: Source, collection: C): C {
        parse(source) { collection.add(it) }
        return collection
    }

    /**
     * Parses a source and adds all tokens into a list.
     *
     * @param source A source of characters.
     * @return A list with the tokens.
     */
    public fun parseToList(source: Source): List<T> {
        return parseTo(source, ArrayList())
    }

    public companion object {
        /**
         * Creates and configures a [Lexer].
         *
         * @param T The type of tokens the lexer generates.
         * @param block The lexer configurator.
         * @return A configured Lexer.
         * @author AdrianTodt
         */
        @JvmStatic
        public fun <T> create(block: LexerConfig<T>): Lexer<T> {
            return LexerImpl(MatcherImpl<T>().apply(block))
        }
    }
}
