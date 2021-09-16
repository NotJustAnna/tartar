package com.github.adriantodt.tartar.extensions

import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.SyntaxException

/**
 * Ensures there's no character files after this block of code.
 */
fun <R> ParserContext<*, *>.ensureEOF(block: () -> R): R {
    val r = block()
    if (!eof) throw SyntaxException("Should've reached end of content", eat().section)
    return r
}

/**
 * Eats tokens in a row. Returns a list, which can be used with a destructuring declaration.
 */
fun <T> ParserContext<T, *>.eatMulti(vararg types: T) = types.map(this::eat)
