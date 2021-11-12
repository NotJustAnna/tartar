package com.github.adriantodt.tartar.api.parser

import com.github.adriantodt.tartar.api.lexer.Section
import com.github.adriantodt.tartar.api.lexer.Sectional

/**
 * Indicates that a syntax exception happened.
 *
 * @param message The detailed message.
 * @param section The [section][Section] where it happened.
 * @author An Tran
 */
public open class SyntaxException(
    message: String,
    override val section: Section? = null
) : RuntimeException("$message at ${section?.toString() ?: "<unknown section>"}"), Sectional
