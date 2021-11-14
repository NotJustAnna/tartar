package com.github.adriantodt.tartar.impl

import com.github.adriantodt.tartar.api.dsl.PrefixFunction
import com.github.adriantodt.tartar.api.grammar.PrefixParselet
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.Token

internal class PrefixParseletImpl<T, K : Token<T>, E>(
    private val block: PrefixFunction<T, K, E>
) : PrefixParselet<T, K, E> {
    override fun parse(ctx: ParserContext<T, K, E>, token: K) = block(ctx, token)
}
