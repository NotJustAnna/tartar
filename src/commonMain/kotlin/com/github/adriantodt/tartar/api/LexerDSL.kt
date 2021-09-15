package com.github.adriantodt.tartar.api

import com.github.adriantodt.tartar.api.lexer.LexerContext

/**
 * A builder of [Lexers][com.github.adriantodt.tartar.api.lexer.Lexer], as a domain-specific language (DSL).
 *
 * @param T The type of tokens the lexer generates.
 * @author AdrianTodt
 */
interface LexerDSL<T> {
    /**
     * Configures the lexer to execute a specific block of code when this character matches.
     */
    operator fun Char.invoke(block: ClosureFunction<LexerContext<T>, Char, Unit> = {}) {
        matching(this).configure(block)
    }

    /**
     * Configures the lexer to execute a specific block of code when this sequence of characters matches.
     */
    operator fun String.invoke(block: ClosureFunction<LexerContext<T>, Char, Unit> = {}) {
        matching(this).configure(block)
    }

    /**
     * Returns a matcher for a sequence of characters.
     */
    fun matching(string: String): LexerDSL<T>

    /**
     * Returns a matcher for a sequence of characters and configures it.
     */
    fun matching(string: String, block: Closure<LexerDSL<T>, Unit>) {
        matching(string).block()
    }

    /**
     * Returns a matcher for a character.
     */
    fun matching(char: Char): LexerDSL<T>

    /**
     * Returns a matcher for a characters and configures it.
     */
    fun matching(char: Char, block: Closure<LexerDSL<T>, Unit>) {
        matching(char).block()
    }

    /**
     * Returns a matcher for a predicate.
     */
    fun matching(block: CharPredicate): LexerDSL<T>

    /**
     * Configures the lexer to execute a specific block of code when match.
     */
    fun configure(block: ClosureFunction<LexerContext<T>, Char, Unit>)
}
