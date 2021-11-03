package net.notjustanna.tartar.api.parser

import net.notjustanna.tartar.api.lexer.Section
import net.notjustanna.tartar.api.lexer.Sectional

/**
 * A [Parser] token.
 *
 * @param T The type of the token.
 * @param type The type of the token.
 * @param value The value of this token.
 * @param section The section of this token.
 * @author An Tran
 */
public data class Token<T>(public val type: T, public val value: String, override val section: Section) : Sectional {
    /**
     * Returns a string representation of the token.
     */
    override fun toString(): String {
        return "$type[$value] $section"
    }
}
