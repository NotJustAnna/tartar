package net.notjustanna.tartar.api.lexer

import kotlin.math.max
import kotlin.math.min

/**
 * Portable (non-jvm) implementation of a character stream whose source is a string.
 */
public actual class StringReader actual constructor(s: String) {
    private var str: String = s
    private val length: Int = s.length
    private var next = 0
    private var mark = 0

    /**
     * Reads a single character.
     */
    public actual fun read(): Int {
        return if (next >= length) -1 else str[next++].code
    }

    /**
     * Skips a specified amount of characters in the stream, returning the amount of characters actually skipped.
     */
    public actual fun skip(ns: Long): Long {
        if (next >= length) return 0
        var n = min(length - next.toLong(), ns)
        n = max(-next.toLong(), n)
        next += n.toInt()
        return n
    }

    /**
     * Marks the stream's current position. Calling reset() reverts the stream to this position.
     */
    public actual fun mark(readAheadLimit: Int) {
        require(readAheadLimit >= 0) { "Read-ahead limit < 0" }
        mark = next
    }

    /**
     * Resets the stream to the last marked position.
     * If mark() was never called, the stream resets to the beginning of the string.
     */
    public actual fun reset() {
        next = mark
    }
}
