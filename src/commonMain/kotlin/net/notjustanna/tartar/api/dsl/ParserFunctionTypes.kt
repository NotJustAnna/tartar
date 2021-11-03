package net.notjustanna.tartar.api.dsl

import net.notjustanna.tartar.api.parser.ParserContext

/**
 * Function which receives a [ParserContext] as its receiver, and returns a result.
 */
public typealias ParserFunction<T, E, R> = ParserContext<T, E>.() -> R
