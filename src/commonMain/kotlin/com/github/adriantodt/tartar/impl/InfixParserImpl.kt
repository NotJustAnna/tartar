package com.github.adriantodt.tartar.impl

import com.github.adriantodt.tartar.api.InfixFunction
import com.github.adriantodt.tartar.api.parser.InfixParser
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.Token

class InfixParserImpl<T, E>(override val precedence: Int, private val block: InfixFunction<T, E>) : InfixParser<T, E> {
    override fun parse(ctx: ParserContext<T, E>, left: E, token: Token<T>) = block(ctx, left, token)
}
