package com.github.adriantodt.tartar.api.lexer

/**
 * Indicates that a specified object has a [section][Section] assigned to it.
 *
 * @author An Tran
 */
interface Sectional {
    /**
     * The assigned section.
     */
    val section: Section

    /**
     * Creates a section which spans across this and another section.
     */
    fun span(other: Sectional) = section.span(other.section)
}
