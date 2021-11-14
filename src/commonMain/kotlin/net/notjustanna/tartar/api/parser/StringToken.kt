package net.notjustanna.tartar.api.parser

import net.notjustanna.tartar.api.lexer.Section

/**
 * A subclass of [Token] which holds a [String] as its [value].
 *
 * @param T The type of the token.
 * @param type The type of the token.
 * @param value The value of this token.
 * @param section The section of this token.
 * @author An Tran, NotJustAnna
 *
 * @see Token
 */
public class StringToken<out T>(type: T, public val value: String, section: Section) : Token<T>(type, section) {
    /**
     * Returns a string representation of the token.
     */
    override fun toString(): String {
        if (value.isNotEmpty()) {
            return "$type[$value] $section"
        }
        return super.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        if (!super.equals(other)) return false

        other as StringToken<*>

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + value.hashCode()
    }
}
