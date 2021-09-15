package com.github.adriantodt.tartar.api.parser

import com.github.adriantodt.tartar.api.lexer.Section

/**
 * Indicates that a syntax exception happened.
 *
 * @param message The detailed message.
 * @param position The [section][Section] where it happened.
 * @author An Tran
 */
open class SyntaxException(message: String, val position: Section) : RuntimeException("$message at $position")
