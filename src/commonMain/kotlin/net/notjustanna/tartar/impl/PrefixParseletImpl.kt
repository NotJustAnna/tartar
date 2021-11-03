package net.notjustanna.tartar.impl

import net.notjustanna.tartar.api.dsl.PrefixFunction
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.grammar.PrefixParselet
import net.notjustanna.tartar.api.parser.Token

internal class PrefixParseletImpl<T, E>(private val block: PrefixFunction<T, E>) : PrefixParselet<T, E> {
    override fun parse(ctx: ParserContext<T, E>, token: Token<T>) = block(ctx, token)
}
