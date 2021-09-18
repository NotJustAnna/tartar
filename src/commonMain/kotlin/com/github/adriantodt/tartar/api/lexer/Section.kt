package com.github.adriantodt.tartar.api.lexer

import kotlin.math.max
import kotlin.math.min

/**
 * Represents a span of characters.
 *
 * @param source The section's source.
 * @param index The section's start index.
 * @param length The section's length.
 * @author An Tran, AdrianTodt
 */
data class Section(val source: Source, val index: Int, val length: Int) {
    init {
        val bounds = 0..source.content.length
        require(index in bounds) {
            "Section index ($index) must be within content's bounds (0..${source.content.length})"
        }
        val end = index + length
        require(end in bounds) {
            "Section end ($end) must be within content's bounds (0..${source.content.length})"
        }
    }

    /**
     * The range of this section.
     */
    val range by lazy { index..(index + length) }

    /**
     * The substring this section represents.
     */
    val substring by lazy { source.content.substring(range) }

    /**
     * The lines of this section.
     */
    val lines by lazy {
        source.lines.dropWhile { range.first > it.range.last }.takeWhile { range.last > it.range.first }
    }

    /**
     * The line number of the start of the section.
     */
    val startLineNumber by lazy { lines.first().lineNumber }

    /**
     * The line index of the start of the section.
     */
    val startLineIndex by lazy { range.first - lines.first().range.first }

    /**
     * The line number of the end of the section.
     */
    val endLineNumber by lazy { lines.last().lineNumber }

    /**
     * The line index of the end of the section.
     */
    val endLineIndex by lazy { range.last - lines.last().range.last }

    /**
     * Creates a new section which spans across this and another section.
     */
    fun span(other: Section): Section {
        require(source == other.source) {
            "Sections $this and $other have different sources and thus can't be spanned."
        }

        val start = min(range.first, other.range.first)
        val end = max(range.last, other.range.last)

        return Section(source, start, end - start)
    }

    /**
     * Returns a string representation of the section.
     */
    override fun toString() = "(${source.name}:$startLineNumber:$startLineIndex)"
}
