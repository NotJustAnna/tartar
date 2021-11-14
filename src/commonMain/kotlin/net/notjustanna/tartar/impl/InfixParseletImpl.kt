package net.notjustanna.tartar.impl

import net.notjustanna.tartar.api.dsl.InfixFunction
import net.notjustanna.tartar.api.grammar.InfixParselet
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.Token

internal class InfixParseletImpl<T, K : Token<T>, E>(
    override val precedence: Int,
    private val block: InfixFunction<T, K, E>
) : InfixParselet<T, K, E> {
    override fun parse(ctx: ParserContext<T, K, E>, left: E, token: K) = block(ctx, left, token)
}
