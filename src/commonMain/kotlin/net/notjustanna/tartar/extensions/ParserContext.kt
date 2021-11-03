package net.notjustanna.tartar.extensions

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token

/**
 * Ensures there's no character files after this block of code.
 */
public fun <R> ParserContext<*, *>.ensureEOF(block: () -> R): R {
    val r = block()
    if (!eof) throw SyntaxException("Should've reached end of content", eat().section)
    return r
}

/**
 * Eats tokens in a row. Returns a list, which can be used with a destructuring declaration.
 */
public fun <T> ParserContext<T, *>.eatMulti(vararg types: T): List<Token<T>> {
    return types.map(this::eat)
}
