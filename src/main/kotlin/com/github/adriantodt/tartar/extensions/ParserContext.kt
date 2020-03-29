package com.github.adriantodt.tartar.extensions

import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.SyntaxException

/**
 * Ensures there's no character files after this block of code.
 */
fun <R> ParserContext<*, *>.ensureEOF(block: () -> R) : R {
    try {
        return block()
    } finally {
        if (!eof) throw SyntaxException("Should've reached end of content", eat().section)
    }
}