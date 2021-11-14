package net.notjustanna.tartar.api.dsl

import net.notjustanna.tartar.api.parser.ParserContext

/**
 * Function used by [GrammarDSL] to configure a
 * [Prefix Parselet][net.notjustanna.tartar.api.grammar.PrefixParselet]
 * in a functional way.
 */
public typealias PrefixFunction<T, K, E> = ParserContext<T, K, E>.(token: K) -> E

/**
 * Function used by [GrammarDSL] to configure a
 * [Infix Parselet][net.notjustanna.tartar.api.grammar.InfixParselet]
 * in a functional way.
 */
public typealias InfixFunction<T, K, E> = ParserContext<T, K, E>.(left: E, token: K) -> E

/**
 * Function which receives a [LexerDSL] as its receiver.
 */
public typealias GrammarConfig<T, K, E> = GrammarDSL<T, K, E>.() -> Unit
