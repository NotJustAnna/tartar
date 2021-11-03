package com.github.adriantodt.tartar.impl

import com.github.adriantodt.tartar.api.dsl.InfixFunction
import com.github.adriantodt.tartar.api.grammar.InfixParselet
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.Token

internal class InfixParseletImpl<T, E>(
    override val precedence: Int,
    private val block: InfixFunction<T, E>
) : InfixParselet<T, E> {
    override fun parse(ctx: ParserContext<T, E>, left: E, token: Token<T>) = block(ctx, left, token)
}
