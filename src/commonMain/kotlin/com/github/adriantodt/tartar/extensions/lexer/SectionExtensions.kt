package com.github.adriantodt.tartar.extensions.lexer

import com.github.adriantodt.tartar.api.lexer.LexerContext
import com.github.adriantodt.tartar.api.lexer.Section

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
