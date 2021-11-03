package com.github.adriantodt.tartar.api.lexer

/**
 * A lexing context, created by a [Lexer.parse] call, which exposes an interface for advanced lexing.
 *
 * @param T The type of tokens the lexer generates.
 * @author AdrianTodt
 */
public interface LexerContext<T> {
    /**
     * The original source of this context.
     */
    public val source: Source

    /**
     * This context' reader.
     */
    public val reader: StringReader

    /**
     * The current index.
     */
    public val index: Int

    /**
     * Peeks the next character of the reader.
     */
    public fun peek(): Char

    /**
     * Peeks a character a distance far away of the reader.
     */
    public fun peek(distance: Int): Char

    /**
     * Peeks a string with a predefined length of the reader.
     */
    public fun peekString(length: Int): String

    /**
     * Peeks the next character and, if equals the expected character, consumes it.
     * Returns true if the peeked character were equals the expected consumer and consumed.
     */
    public fun match(expect: Char): Boolean

    /**
     * Checks if there's more characters ahead.
     */
    public fun hasNext(): Boolean

    /**
     * Returns the next character of the reader.
     */
    public fun next(): Char

    /**
     * Returns a predefined length of characters of the reader, as a String.
     */
    public fun nextString(length: Int): String

    /**
     * Calls the [Lexer.parse]'s token consumer.
     */
    public fun process(token: T)

    /**
     * Lexes once and return the processed tokens.
     */
    public fun parseOnce(): List<T>
}
