package com.github.adriantodt.tartar.impl

import com.github.adriantodt.tartar.api.PrefixFunction
import com.github.adriantodt.tartar.api.parser.Token
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.PrefixParser

class PrefixParserImpl<T, E>(private val block: PrefixFunction<T, E>) : PrefixParser<T, E> {
    override fun parse(parser: ParserContext<T, E>, token: Token<T>) = block(parser, token)
}