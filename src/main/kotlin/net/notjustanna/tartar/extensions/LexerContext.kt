package net.notjustanna.tartar.extensions

import net.notjustanna.tartar.api.lexer.LexerContext
import net.notjustanna.tartar.api.lexer.Section
import net.notjustanna.tartar.api.parser.Token

/**
 * Creates a section.
 */
fun LexerContext<*>.section(offset: Int, length: Int = 0): Section {
    return Section(source, lineNumber, lineIndex - length - offset, length + offset)
}

/**
 * Creates a token.
 */
fun <T> LexerContext<Token<T>>.makeToken(tokenType: T, offset: Int = 1) = makeToken(tokenType, "", offset)

/**
 * Creates a token.
 */
fun <T> LexerContext<Token<T>>.makeToken(tokenType: T, string: String, offset: Int = 0) = Token(
    tokenType,
    string,
    Section(source, lineNumber, lineIndex - string.length - offset, string.length + offset)
)

/**
 * Reads a C-like identifier.
 */
fun LexerContext<*>.readIdentifier(firstChar: Char? = null): String {
    val buf = StringBuilder()
    firstChar?.let(buf::append)
    while (hasNext()) {
        val cc = peek()
        if (cc.isLetterOrDigit() || cc == '_') {
            buf.append(cc)
            next()
        } else {
            break
        }
    }
    return buf.toString()
}

/**
 * Reads a String up until a delimiter.
 */
fun LexerContext<*>.readString(delimiter: Char): String {
    val buf = StringBuilder()
    var eol = false
    while (hasNext()) {
        val c = peek()
        if (c == delimiter) {
            next()
            eol = true
            break
        } else {
            next()
            buf.append(c)
        }
    }
    if (!eol) {
        throw IllegalStateException("Unterminated string")
    }
    return buf.toString()
}

/**
 * Reads a number.
 */
fun LexerContext<*>.readNumber(c: Char): LexicalNumber {
    val buf = StringBuilder()

    if (c == '0') {
        when {
            match('x') -> {
                fillBufferNumbers(buf, true)
                val isLong = match('l') || match('L')
                val s = buf.toString()
                val number = s.toIntOrNull(16) ?: return LexicalNumber.Invalid(buf.toString())
                return LexicalNumber.Integer(s, number, 16, isLong)
            }
            match('b') -> {
                fillBufferNumbers(buf, false)
                val isLong = match('l') || match('L')
                val s = buf.toString()
                val number = s.toIntOrNull(2) ?: return LexicalNumber.Invalid(buf.toString())
                return LexicalNumber.Integer(s, number, 2, isLong)
            }
            else -> {
                buf.append('0')
            }
        }
    } else {
        buf.append(c)
    }

    fillBufferNumbers(buf, false)

    return when {
        peek() == '.' && peek(1).isDigit() -> {
            next()
            buf.append('.')
            fillBufferNumbers(buf, false)
            val isFloat = match('f') || match('F')
            val s = buf.toString()
            LexicalNumber.Decimal(s, s.toDouble(), isFloat)
        }
        else -> {
            val s = buf.toString()
            val int = s.toIntOrNull()
            if (int != null) {
                val isLong = match('l') || match('L')
                LexicalNumber.Integer(s, s.toInt(), 10, isLong)
            } else {
                LexicalNumber.Invalid(buf.toString())
            }
        }
    }
}

/**
 * Result of [readNumber].
 */
sealed class LexicalNumber {
    abstract val string: String

    /**
     * Read number is invalid.
     */
    data class Invalid(override val string: String) : LexicalNumber()

    /**
     * Read number is a decimal.
     */
    data class Decimal(override val string: String, val value: Double, val isFloat: Boolean = false) : LexicalNumber()

    /**
     * Read number is an integers.
     */
    data class Integer(override val string: String, val value: Int, val radix: Int = 10, val isLong: Boolean = false) : LexicalNumber()
}

private fun LexerContext<*>.fillBufferNumbers(buf: StringBuilder, allowHex: Boolean) {
    while (hasNext()) {
        val c = peek()
        if (c.isDigit() || (allowHex && c in 'A'..'F' || c in 'a'..'f')) {
            buf.append(next())
        } else {
            break
        }
    }
}
