package net.notjustanna.tartar.extensions.lexer

import net.notjustanna.tartar.api.lexer.LexerContext
import net.notjustanna.tartar.api.parser.StringToken
import net.notjustanna.tartar.api.parser.Token

/**
 * Creates a token, based off the current state of the [LexerContext].
 *
 * When called with default values, the returned token matches the last character consumed.
 * @param type The type of the token to be created.
 * @param back The amount of characters to offset back from the lexer's current index.
 * @param length The amount of characters this section is composed off.
 * @param offset This value added to [back] and [length] as an offset.
 */
public fun <T> LexerContext<in Token<T>>.token(type: T, back: Int = 1, length: Int = back, offset: Int = 0): Token<T> {
    return Token(type, section(back, length, offset))
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
public fun <T> LexerContext<in StringToken<T>>.token(
    type: T,
    value: String,
    back: Int = value.length,
    length: Int = back,
    offset: Int = 0
): StringToken<T> {
    return StringToken(type, value, section(back, length, offset))
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
public fun <T> LexerContext<in Token<T>>.processToken(type: T, back: Int = 1, length: Int = back, offset: Int = 0) {
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
public fun <T> LexerContext<in StringToken<T>>.processToken(
    type: T,
    value: String,
    back: Int = value.length,
    length: Int = back,
    offset: Int = 0
) {
    process(token(type, value, back, length, offset))
}
