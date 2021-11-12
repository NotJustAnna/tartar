package com.github.adriantodt.tartar.api.lexer

/**
 * Indicates that a specified object has a [section][Section] assigned to it.
 *
 * @author An Tran
 */
public interface Sectional {
    /**
     * The assigned section.
     */
    public val section: Section?

    /**
     * Creates a section which spans across this and another section.
     */
    public fun span(other: Sectional): Section? {
        val a = section
        val b = other.section
        return when {
            a == null -> b
            b == null -> a
            else -> a.span(b)
        }
    }
}
