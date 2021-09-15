package com.github.adriantodt.tartar.api.lexer

import kotlin.math.max
import kotlin.math.min

/**
 * Represents a span of characters.
 *
 * @param source The section's source.
 * @param lineNumber The section's line number.
 * @param lineIndex The section's line index.
 * @param length The section's length.
 * @author An Tran
 */
data class Section(val source: Source, val lineNumber: Int, val lineIndex: Int, val length: Int) {
    /**
     * The line of this section.
     */
    val line get() = source.lines[lineNumber - 1]

    /**
     * The substring this section represents.
     */
    val substring get() = source.lines[lineNumber - 1].substring(lineIndex, lineIndex + length)

    /**
     * Creates a new section which spans across this and another section.
     */
    fun span(other: Section): Section {
        if (source == other.source && lineNumber == other.lineNumber) {
            val min = min(lineIndex, other.lineIndex)
            val max = max(lineIndex + length, other.lineIndex + other.length)
            return Section(source, lineNumber, min, (max - min))
        }
        throw IllegalStateException("section sources")
    }

    /**
     * Returns a string representation of the section.
     */
    override fun toString() = "(${source.name}:$lineNumber:$lineIndex)"
}
