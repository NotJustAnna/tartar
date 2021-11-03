package net.notjustanna.tartar.api.parser

import net.notjustanna.tartar.api.lexer.Section

/**
 * Indicates that a syntax exception happened.
 *
 * @param message The detailed message.
 * @param position The [section][Section] where it happened.
 * @author An Tran
 */
public open class SyntaxException(message: String, public val position: Section) : RuntimeException("$message at $position")
