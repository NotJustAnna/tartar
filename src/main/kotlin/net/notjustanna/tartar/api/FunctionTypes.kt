package net.notjustanna.tartar.api

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.Token

typealias Closure<T, R> = T.() -> R

typealias ClosureFunction<T, E, R> = T.(E) -> R

typealias PrefixFunction<T, E> = ParserContext<T, E>.(Token<T>) -> E

typealias InfixFunction<T, E> = ParserContext<T, E>.(E, Token<T>) -> E

typealias CharPredicate = (Char) -> Boolean