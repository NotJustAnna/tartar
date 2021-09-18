package com.github.adriantodt.tartar.api.lexer

/**
 * Portable implementation of a character stream whose source is a string.
 */
expect class StringReader(s: String) {
    /**
     * Reads a single character.
     */
    fun read(): Int

    /**
     * Skips a specified amount of characters in the stream, returning the amount of characters actually skipped.
     */
    fun skip(n: Long): Long

    /**
     * Marks the stream's current position. Calling reset() reverts the stream to this position.
     */
    fun mark(readAheadLimit: Int)

    /**
     * Resets the stream to the last marked position.
     * If mark() was never called, the stream resets to the beginning of the string.
     */
    fun reset()
}
