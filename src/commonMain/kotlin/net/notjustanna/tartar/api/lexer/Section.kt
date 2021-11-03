package net.notjustanna.tartar.api.lexer

import kotlin.math.max
import kotlin.math.min

/**
 * Represents a span of characters.
 *
 * @param source The section's source.
 * @param index The section's start index.
 * @param length The section's length.
 * @author An Tran, NotJustAnna
 */
public data class Section(public val source: Source, public val index: Int, public val length: Int) {
    init {
        val sourceBounds = source.bounds
        require(index in sourceBounds) {
            "Section index ($index) must be within content's bounds (0..${source.content.length})"
        }
        val end = index + length
        require(end in sourceBounds) {
            "Section end ($end) must be within content's bounds (0..${source.content.length})"
        }
    }

    /**
     * The range of this section.
     */
    public val range: IntRange
        get() = index..(index + length)

    /**
     * The substring this section represents.
     */
    public val substring: String
        get() = source.content.substring(index, index + length)

    /**
     * The lines of this section.
     */
    public val lines: List<Source.Line> = source.lines
        .dropWhile { index > it.range.last }
        .takeWhile { (index + length) > it.range.first }

    /**
     * The first line of this section.
     */
    public val firstLine: Source.Line
        get() = lines.first()

    /**
     * The last line of this section.
     */
    public val lastLine: Source.Line
        get() = lines.last()

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
        return "(${source.name}:${firstLine.lineNumber}:${index - firstLine.range.first})"
    }
}
