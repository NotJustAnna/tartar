package net.notjustanna.tartar.api.parser

import net.notjustanna.tartar.api.lexer.Section
import net.notjustanna.tartar.api.lexer.Sectional


/**
 * Object which represents the output of a lexical analysis and input to parsing.
 *
 * This class (and its subclasses) are the recommended output of a given
 * [Lexer][net.notjustanna.tartar.api.lexer.Lexer] of this library.
 * Furthermore, this class and its subclasses are the only inputs accepted by a given [Parser] of this library.
 *
 * This implementation does not hold any data, but subclasses may implement any desired contents.
 *
 * @constructor Creates a token with a given type and (optionally) a section.
 * @param T The type of the token.
 * @param type The type of the token.
 * @param section The section of the token.
 * @property type The type of this token.
 * @property section The section of this token.
 * @author An Tran, NotJustAnna
 * @see StringToken
 */
public open class Token<out T>(public val type: T, public override val section: Section? = null) : Sectional {
    /**
     * Returns a string representation of this token.
     */
    override fun toString(): String {
        return "$type $section"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Token<*>

        if (type != other.type) return false
        if (section != other.section) return false

        return true
    }

    override fun hashCode(): Int {
        return 31 * (type?.hashCode() ?: 0) + (section?.hashCode() ?: 0)
    }
}
