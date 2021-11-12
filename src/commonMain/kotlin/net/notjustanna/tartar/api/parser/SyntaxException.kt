package net.notjustanna.tartar.api.parser

import net.notjustanna.tartar.api.lexer.Section
import net.notjustanna.tartar.api.lexer.Sectional

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
