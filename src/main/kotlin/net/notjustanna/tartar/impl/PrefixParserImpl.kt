package net.notjustanna.tartar.impl

import net.notjustanna.tartar.api.PrefixFunction
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token

class PrefixParserImpl<T, E>(private val block: PrefixFunction<T, E>) : PrefixParser<T, E> {
    override fun parse(ctx: ParserContext<T, E>, token: Token<T>) = block(ctx, token)
}