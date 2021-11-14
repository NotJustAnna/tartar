package net.notjustanna.tartar.impl

import net.notjustanna.tartar.api.dsl.PrefixFunction
import net.notjustanna.tartar.api.grammar.PrefixParselet
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.Token

internal class PrefixParseletImpl<T, K : Token<T>, E>(
    private val block: PrefixFunction<T, K, E>
) : PrefixParselet<T, K, E> {
    override fun parse(ctx: ParserContext<T, K, E>, token: K) = block(ctx, token)
}
