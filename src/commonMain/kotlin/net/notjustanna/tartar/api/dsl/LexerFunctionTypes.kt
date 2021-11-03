package net.notjustanna.tartar.api.dsl

import net.notjustanna.tartar.api.lexer.LexerContext

/**
 * Function which receives a [LexerContext] as its receiver and a matched [Char] as its parameter.
 */
public typealias MatchFunction<T> = LexerContext<T>.(char: Char) -> Unit

/**
 * Function which receives a [LexerDSL] as its receiver.
 */
public typealias LexerConfig<T> = LexerDSL<T>.() -> Unit
