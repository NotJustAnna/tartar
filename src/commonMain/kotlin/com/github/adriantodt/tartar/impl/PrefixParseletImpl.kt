package com.github.adriantodt.tartar.impl

import com.github.adriantodt.tartar.api.dsl.PrefixFunction
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.grammar.PrefixParselet
import com.github.adriantodt.tartar.api.parser.Token

internal class PrefixParseletImpl<T, E>(private val block: PrefixFunction<T, E>) : PrefixParselet<T, E> {
    override fun parse(ctx: ParserContext<T, E>, token: Token<T>) = block(ctx, token)
}
