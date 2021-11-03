package net.notjustanna.tartar.impl

import net.notjustanna.tartar.api.dsl.InfixFunction
import net.notjustanna.tartar.api.grammar.InfixParselet
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.Token

internal class InfixParseletImpl<T, E>(
    override val precedence: Int,
    private val block: InfixFunction<T, E>
) : InfixParselet<T, E> {
    override fun parse(ctx: ParserContext<T, E>, left: E, token: Token<T>) = block(ctx, left, token)
}
