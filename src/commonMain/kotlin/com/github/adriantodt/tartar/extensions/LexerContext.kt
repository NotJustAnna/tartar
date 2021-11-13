package com.github.adriantodt.tartar.extensions

import com.github.adriantodt.tartar.api.lexer.LexerContext
import com.github.adriantodt.tartar.api.lexer.Section
import com.github.adriantodt.tartar.api.parser.Token

/**
 * Creates a section, based off the current state of the [LexerContext].
 *
 * When called with default values, the returned section matches the last character consumed.
 * @param back The amount of characters to offset back from the lexer's current index.
 * @param length The amount of characters this section is composed off.
 * @param offset This value added to [back] and [length] as an offset.
 */
public fun LexerContext<*>.section(back: Int = 1, length: Int = back, offset: Int = 0): Section {
    return Section(source, index - back - offset, length + offset)
}

/**
 * Creates a token, based off the current state of the [LexerContext].
 *
 * When called with default values, the returned token matches the last character consumed.
 * @param type The type of the token to be created.
 * @param back The amount of characters to offset back from the lexer's current index.
 * @param length The amount of characters this section is composed off.
 * @param offset This value added to [back] and [length] as an offset.
 */
public fun <T> LexerContext<Token<T>>.token(type: T, back: Int = 1, length: Int = back, offset: Int = 0): Token<T> {
    return Token(type, "", section(back, length, offset))
}

/**
 * Creates a token with a value, based off the current state of the [LexerContext].
 *
 * When called with default values, the returned token matches the last N character consumers,
 * where N is [value]'s length.
 * @param type The type of the token to be created.
 * @param value The value of the token to be created.
 * @param back The amount of characters to offset back from the lexer's current index.
 * @param length The amount of characters this section is composed off.
 * @param offset This value added to [back] and [length] as an offset.
 */
public fun <T> LexerContext<Token<T>>.token(
    type: T,
    value: String,
    back: Int = value.length,
    length: Int = back,
    offset: Int = 0
): Token<T> {
    return Token(type, value, section(back, length, offset))
}

/**
 * Emits a token to the lexer, based off the current state of the [LexerContext].
 *
 * When called with default values, the returned token matches the last character consumed.
 * @param type The type of the token to be created.
 * @param back The amount of characters to offset back from the lexer's current index.
 * @param length The amount of characters this section is composed off.
 * @param offset This value added to [back] and [length] as an offset.
 */
public fun <T> LexerContext<Token<T>>.processToken(type: T, back: Int = 1, length: Int = back, offset: Int = 0) {
    process(token(type, back, length, offset))
}

/**
 * Emits a token with a value, based off the current state of the [LexerContext].
 *
 * When called with default values, the returned token matches the last N character consumers,
 * where N is [value]'s length.
 * @param type The type of the token to be created.
 * @param value The value of the token to be created.
 * @param back The amount of characters to offset back from the lexer's current index.
 * @param length The amount of characters this section is composed off.
 * @param offset This value added to [back] and [length] as an offset.
 */
public fun <T> LexerContext<Token<T>>.processToken(
    type: T,
    value: String,
    back: Int = value.length,
    length: Int = back,
    offset: Int = 0
) {
    process(token(type, value, back, length, offset))
}

/**
 * Creates a token.
 *
 * This function is deprecated and will be replaced by the [token] function.
 */
@Deprecated("Replaced with token", ReplaceWith("token(type, offset)"))
public fun <T> LexerContext<Token<T>>.makeToken(type: T, offset: Int = 1): Token<T> {
    return makeToken(type, "", offset)
}

/**
 * Creates a token.
 *
 * This function is deprecated and will be replaced by the [token] function.
 */
@Deprecated("Replaced with token", ReplaceWith("token(type, string)"))
public fun <T> LexerContext<Token<T>>.makeToken(type: T, string: String, offset: Int = 0): Token<T> {
    val section = Section(source, index - string.length - offset, string.length + offset)
    return Token(type, string, section)
}

/**
 * Reads a C-like identifier.
 */
public fun LexerContext<*>.readIdentifier(firstChar: Char? = null): String {
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
public fun LexerContext<*>.readString(delimiter: Char): String {
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
public fun LexerContext<*>.readNumber(c: Char): LexicalNumber {
    val buf = StringBuilder()

    if (c == '0') {
        when {
            match('x') -> {
                fillBufferNumbers(buf, true)
                val isLong = match('l') || match('L')
                val s = buf.toString()
                val number = s.toLongOrNull(16) ?: return LexicalNumber.Invalid(buf.toString())
                return LexicalNumber.Integer(s, number, 16, isLong)
            }
            match('b') -> {
                fillBufferNumbers(buf, false)
                val isLong = match('l') || match('L')
                val s = buf.toString()
                val number = s.toLongOrNull(2) ?: return LexicalNumber.Invalid(buf.toString())
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
            val int = s.toLongOrNull()
            if (match('f') || match('F')) {
                LexicalNumber.Decimal(s, s.toDouble(), true)
            } else if (int != null) {
                val isLong = match('l') || match('L')
                LexicalNumber.Integer(s, int, 10, isLong)
            } else {
                LexicalNumber.Invalid(buf.toString())
            }
        }
    }
}

/**
 * Result of [readNumber].
 */
public sealed class LexicalNumber {
    /**
     * The original string value of the number.
     */
    public abstract val string: String

    /**
     * Read number is invalid.
     */
    public data class Invalid(override val string: String) : LexicalNumber()

    /**
     * Read number is a decimal.
     */
    public data class Decimal(
        override val string: String, val value: Double, val isFloat: Boolean = false
    ) : LexicalNumber()

    /**
     * Read number is an integer.
     */
    public data class Integer(
        override val string: String, val value: Long, val radix: Int = 10, val isLong: Boolean = false
    ) : LexicalNumber()
}

private fun LexerContext<*>.fillBufferNumbers(buf: StringBuilder, allowHex: Boolean) {
    while (hasNext()) {
        val c = peek()
        if (c.isDigit() || (allowHex && (c in 'A'..'F' || c in 'a'..'f'))) {
            buf.append(next())
        } else {
            break
        }
    }
}
