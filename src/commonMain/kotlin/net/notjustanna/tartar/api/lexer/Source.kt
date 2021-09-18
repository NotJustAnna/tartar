package net.notjustanna.tartar.api.lexer

import net.notjustanna.tartar.impl.calculateLineRanges

/**
 * A source of text to [lexers][Lexer].
 *
 * @constructor Creates a source based of a string.
 * @param content The content of the source.
 * @param name The name of the source.
 * @param path The path to the source.
 * @author NotJustAnna, An Tran
 */
data class Source(val content: String, val name: String = "?", val path: String = "!!no path!!") {
    /**
     * The lines of the content.
     */
    val lines by lazy {
        content.calculateLineRanges().mapIndexed { index, range ->
            Line(index + 1, content.substring(range), range)
        }
    }

    /**
     * Represents a line from the source.
     * @param lineNumber The line's number.
     * @param content The line's content, including the line separator.
     * @param range The line's range spanning the source's content.
     */
    data class Line internal constructor(val lineNumber: Int, val content: String, val range: IntRange)
}
