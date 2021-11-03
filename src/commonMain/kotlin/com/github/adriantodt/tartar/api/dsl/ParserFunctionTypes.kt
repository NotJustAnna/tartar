package com.github.adriantodt.tartar.api.dsl

import com.github.adriantodt.tartar.api.parser.ParserContext

/**
 * Function which receives a [ParserContext] as its receiver, and returns a result.
 */
public typealias ParserFunction<T, E, R> = ParserContext<T, E>.() -> R
