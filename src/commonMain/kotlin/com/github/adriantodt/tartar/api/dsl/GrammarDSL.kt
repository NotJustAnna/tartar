package com.github.adriantodt.tartar.api.dsl

import com.github.adriantodt.tartar.api.grammar.Grammar
import com.github.adriantodt.tartar.api.grammar.InfixParselet
import com.github.adriantodt.tartar.api.grammar.PrefixParselet
import com.github.adriantodt.tartar.api.parser.Token

/**
 * A builder of [Grammars][com.github.adriantodt.tartar.api.parser.Grammar], as a domain-specific language (DSL).
 *
 * @param T The grammar's token type.
 * @param E The grammar's expression result.
 * @author AdrianTodt
 */
public interface GrammarDSL<T, K : Token<T>, E> {
    /**
     * Imports all parselets from other grammars.

     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     * @param grammars The grammars to import.
     */
    public fun import(override: Boolean = false, vararg grammars: Grammar<T, K, E>)

    /**
     * Registers a prefix parselets into the grammar.
     * @param type The token type to associate the parselet with.
     * @param parselet The prefix parselet to register.
     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     */
    public fun prefix(type: T, parselet: PrefixParselet<T, K, E>, override: Boolean = false)

    /**
     * Registers a prefix parselets into the grammar.
     * @param type The token type to associate the parselet with.
     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     * @param block The code to execute when the type matches.
     */
    public fun prefix(type: T, override: Boolean = false, block: PrefixFunction<T, K, E>)

    /**
     * Registers a infix parselets into the grammar.
     * @param type The token type to associate the parselet with.
     * @param parselet The infix parselet to register.
     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     */
    public fun infix(type: T, parselet: InfixParselet<T, K, E>, override: Boolean = false)

    /**
     * Registers a infix parselets into the grammar.
     * @param type The token type to associate the parselet with.
     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     * @param block The code to execute when the type matches.
     */
    public fun infix(type: T, precedence: Int, override: Boolean = false, block: InfixFunction<T, K, E>)
}
