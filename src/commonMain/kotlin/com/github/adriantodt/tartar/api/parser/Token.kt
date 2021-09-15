package com.github.adriantodt.tartar.api.parser

import com.github.adriantodt.tartar.api.lexer.Section
import com.github.adriantodt.tartar.api.lexer.Sectional

/**
 * A [Parser] token.
 *
 * @param T The type of the token.
 * @param type The type of the token.
 * @param value The value of this token.
 * @param section The section of this token.
 * @author An Tran
 */
data class Token<T>(val type: T, val value: String, override val section: Section) : Sectional {
    /**
     * Returns a string representation of the token.
     */
    override fun toString() = "$type[$value] $section"
}
