package net.notjustanna.tartar.api.parser

/**
 * A grammar for pratt-parsers.
 *
 * @param T The grammar's token type.
 * @param E The grammar's expression result.
 * @param prefixParsers A map of prefix parsers for each token type.
 * @param infixParsers A map of prefix parsers for each token type.
 * @author An Tran
 */
data class Grammar<T, E>(val prefixParsers: Map<T, PrefixParser<T, E>>, val infixParsers: Map<T, InfixParser<T, E>>)
