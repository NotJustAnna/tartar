package net.notjustanna.tartar.impl

import net.notjustanna.tartar.api.InfixFunction
import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.ParserContext

class InfixParserImpl<T, E>(override val precedence: Int, private val block: InfixFunction<T, E>) : InfixParser<T, E> {
    override fun parse(ctx: ParserContext<T, E>, left: E, token: Token<T>) = block(ctx, left, token)
}