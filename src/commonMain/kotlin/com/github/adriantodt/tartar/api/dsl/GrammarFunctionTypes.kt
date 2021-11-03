package com.github.adriantodt.tartar.api.dsl

import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.Token

/**
 * Function used by [GrammarDSL] to configure a
 * [Prefix Parselet][com.github.adriantodt.tartar.api.parser.PrefixParselet]
 * in a functional way.
 */
public typealias PrefixFunction<T, E> = ParserContext<T, E>.(token: Token<T>) -> E

/**
 * Function used by [GrammarDSL] to configure a
 * [Infix Parselet][com.github.adriantodt.tartar.api.parser.InfixParselet]
 * in a functional way.
 */
public typealias InfixFunction<T, E> = ParserContext<T, E>.(left: E, token: Token<T>) -> E

/**
 * Function which receives a [LexerDSL] as its receiver.
 */
public typealias GrammarConfig<T, E> = GrammarDSL<T, E>.() -> Unit
