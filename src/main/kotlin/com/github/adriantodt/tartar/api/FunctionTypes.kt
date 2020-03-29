package com.github.adriantodt.tartar.api

import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.Token

typealias Closure<T, R> = T.() -> R

typealias ClosureFunction<T, E, R> = T.(E) -> R

typealias PrefixFunction<T, E> = ParserContext<T, E>.(Token<T>) -> E

typealias InfixFunction<T, E> = ParserContext<T, E>.(E, Token<T>) -> E

typealias CharPredicate = (Char) -> Boolean