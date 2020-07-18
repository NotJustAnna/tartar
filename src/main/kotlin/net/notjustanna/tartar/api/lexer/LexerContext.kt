package net.notjustanna.tartar.api.lexer

import java.io.Reader

/**
 * A lexing context, created by a [Lexer.parse] call, which exposes an interface for advanced lexing.
 *
 * @param T The type of tokens the lexer generates.
 * @author NotJustAnna
 */
interface LexerContext<T> {
    /**
     * The original source of this context.
     */
    val source: Source

    /**
     * This context' reader.
     */
    val reader: Reader

    /**
     * The current line number.
     */
    val lineNumber: Int

    /**
     * The current line index.
     */
    val lineIndex: Int

    /**
     * Peeks the next character of the reader.
     */
    fun peek(): Char

    /**
     * Peeks a character a distance far away of the reader.
     */
    fun peek(distance: Int): Char

    /**
     * Peeks a string with a predefined length of the reader.
     */
    fun peekString(length: Int): String

    /**
     * Peeks the next character and, if equals the expected character, consumes it.
     * Returns true if the peeked character were equals the expected consumer and consumed.
     */
    fun match(expect: Char): Boolean

    /**
     * Checks if there's more characters ahead.
     */
    fun hasNext(): Boolean

    /**
     * Returns the next character of the reader.
     */
    fun next(): Char

    /**
     * Returns a predefined length of characters of the reader, as a String.
     */
    fun nextString(length: Int): String

    /**
     * Calls the [Lexer.parse]'s token consumer.
     */
    fun process(token: T)

    /**
     * Lexes once and return the processed tokens.
     */
    fun parseOnce(): List<T>
}