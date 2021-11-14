package com.github.adriantodt.tartar.api.dsl

import com.github.adriantodt.tartar.api.parser.ParserContext

/**
 * Function used by [GrammarDSL] to configure a
 * [Prefix Parselet][com.github.adriantodt.tartar.api.grammar.PrefixParselet]
 * in a functional way.
 */
public typealias PrefixFunction<T, K, E> = ParserContext<T, K, E>.(token: K) -> E

/**
 * Function used by [GrammarDSL] to configure a
 * [Infix Parselet][com.github.adriantodt.tartar.api.grammar.InfixParselet]
 * in a functional way.
 */
public typealias InfixFunction<T, K, E> = ParserContext<T, K, E>.(left: E, token: K) -> E

/**
 * Function which receives a [LexerDSL] as its receiver.
 */
public typealias GrammarConfig<T, K, E> = GrammarDSL<T, K, E>.() -> Unit
