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
public data class Section(public val source: Source, public val index: Int, public val length: Int) {
    init {
        require(index in source.bounds) {
            "Section index ($index) must be within content's bounds (0..${source.content.length})"
        }
        val end = index + length
        require(end in source.bounds) {
            "Section end ($end) must be within content's bounds (0..${source.content.length})"
        }
    }

    /**
     * The range of this section.
     */
    public val range: IntRange = index..(index + length)

    /**
     * The substring this section represents.
     */
    public val substring: String = source.content.substring(range)

    /**
     * The lines of this section.
     */
    public val lines: List<Source.Line> = source.lines
        .dropWhile { range.first > it.range.last }
        .takeWhile { range.last > it.range.first }


    /**
     * The line number of the start of the section.
     */
    public val startLineNumber: Int = lines.first().lineNumber

    /**
     * The line index of the start of the section.
     */
    public val startLineIndex: Int = range.first - lines.first().range.first

    /**
     * The line number of the end of the section.
     */
    public val endLineNumber: Int = lines.last().lineNumber

    /**
     * The line index of the end of the section.
     */
    public val endLineIndex: Int = range.last - lines.last().range.last

    /**
     * Creates a new section which spans across this and another section.
     */
    public fun span(other: Section): Section {
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
    override fun toString(): String {
        return "(${source.name}:$startLineNumber:$startLineIndex)"
    }
}
