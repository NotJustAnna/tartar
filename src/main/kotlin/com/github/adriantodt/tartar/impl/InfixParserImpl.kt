package com.github.adriantodt.tartar.impl

import com.github.adriantodt.tartar.api.InfixFunction
import com.github.adriantodt.tartar.api.parser.Token
import com.github.adriantodt.tartar.api.parser.InfixParser
import com.github.adriantodt.tartar.api.parser.ParserContext

class InfixParserImpl<T, E>(override val precedence: Int, private val block: InfixFunction<T, E>) : InfixParser<T, E> {
    override fun parse(ctx: ParserContext<T, E>, left: E, token: Token<T>) = block(ctx, left, token)
}