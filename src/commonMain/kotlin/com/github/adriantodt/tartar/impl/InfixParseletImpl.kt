package com.github.adriantodt.tartar.impl

import com.github.adriantodt.tartar.api.dsl.InfixFunction
import com.github.adriantodt.tartar.api.grammar.InfixParselet
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.Token

internal class InfixParseletImpl<T, K : Token<T>, E>(
    override val precedence: Int,
    private val block: InfixFunction<T, K, E>
) : InfixParselet<T, K, E> {
    override fun parse(ctx: ParserContext<T, K, E>, left: E, token: K) = block(ctx, left, token)
}
