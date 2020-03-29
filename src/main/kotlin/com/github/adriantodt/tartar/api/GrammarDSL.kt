package com.github.adriantodt.tartar.api

import com.github.adriantodt.tartar.api.parser.Grammar
import com.github.adriantodt.tartar.api.parser.InfixParser
import com.github.adriantodt.tartar.api.parser.PrefixParser

/**
 * A builder of [Grammars][com.github.adriantodt.tartar.api.parser.Grammar], as a domain-specific language (DSL).
 *
 * @param T The grammar's token type.
 * @param E The grammar's expression result.
 * @author AdrianTodt
 */
interface GrammarDSL<T, E> {
    /**
     * Imports all parselets from other grammars.

     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     * @param grammars The grammars to import.
     */
    fun import(override: Boolean = false, vararg grammars: Grammar<T, E>)

    /**
     * Registers a prefix parselets into the grammar.
     * @param type The token type to associate the parselet with.
     * @param parselet The prefix parselet to register.
     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     */
    fun prefix(type: T, parselet: PrefixParser<T, E>, override: Boolean = false)

    /**
     * Registers a prefix parselets into the grammar.
     * @param type The token type to associate the parselet with.
     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     * @param block The code to execute when the type matches.
     */
    fun prefix(type: T, override: Boolean = false, block: PrefixFunction<T, E>)

    /**
     * Registers a infix parselets into the grammar.
     * @param type The token type to associate the parselet with.
     * @param parselet The infix parselet to register.
     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     */
    fun infix(type: T, parselet: InfixParser<T, E>, override: Boolean = false)

    /**
     * Registers a infix parselets into the grammar.
     * @param type The token type to associate the parselet with.
     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     * @param block The code to execute when the type matches.
     */
    fun infix(type: T, precedence: Int, override: Boolean = false, block: InfixFunction<T, E>)
}
