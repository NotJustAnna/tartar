package net.notjustanna.tartar.impl

import net.notjustanna.tartar.api.PrefixFunction
import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser

class PrefixParserImpl<T, E>(private val block: PrefixFunction<T, E>) : PrefixParser<T, E> {
    override fun parse(parser: ParserContext<T, E>, token: Token<T>) = block(parser, token)
}