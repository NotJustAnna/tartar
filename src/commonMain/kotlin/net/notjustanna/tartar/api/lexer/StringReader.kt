package net.notjustanna.tartar.api.lexer

/**
 * Portable implementation of a character stream whose source is a string.
 */
public expect class StringReader(s: String) {
    /**
     * Reads a single character.
     */
    public fun read(): Int

    /**
     * Skips a specified amount of characters in the stream, returning the amount of characters actually skipped.
     */
    public fun skip(ns: Long): Long

    /**
     * Marks the stream's current position. Calling reset() reverts the stream to this position.
     */
    public fun mark(readAheadLimit: Int)

    /**
     * Resets the stream to the last marked position.
     * If mark() was never called, the stream resets to the beginning of the string.
     */
    public fun reset()
}
