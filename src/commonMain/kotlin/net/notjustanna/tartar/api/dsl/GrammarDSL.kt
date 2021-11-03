package net.notjustanna.tartar.api.dsl

import net.notjustanna.tartar.api.grammar.Grammar
import net.notjustanna.tartar.api.grammar.InfixParselet
import net.notjustanna.tartar.api.grammar.PrefixParselet

/**
 * A builder of [Grammars][net.notjustanna.tartar.api.parser.Grammar], as a domain-specific language (DSL).
 *
 * @param T The grammar's token type.
 * @param E The grammar's expression result.
 * @author NotJustAnna
 */
public interface GrammarDSL<T, E> {
    /**
     * Imports all parselets from other grammars.

     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     * @param grammars The grammars to import.
     */
    public fun import(override: Boolean = false, vararg grammars: Grammar<T, E>)

    /**
     * Registers a prefix parselets into the grammar.
     * @param type The token type to associate the parselet with.
     * @param parselet The prefix parselet to register.
     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     */
    public fun prefix(type: T, parselet: PrefixParselet<T, E>, override: Boolean = false)

    /**
     * Registers a prefix parselets into the grammar.
     * @param type The token type to associate the parselet with.
     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     * @param block The code to execute when the type matches.
     */
    public fun prefix(type: T, override: Boolean = false, block: PrefixFunction<T, E>)

    /**
     * Registers a infix parselets into the grammar.
     * @param type The token type to associate the parselet with.
     * @param parselet The infix parselet to register.
     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     */
    public fun infix(type: T, parselet: InfixParselet<T, E>, override: Boolean = false)

    /**
     * Registers a infix parselets into the grammar.
     * @param type The token type to associate the parselet with.
     * @param override If set to true, imported parselets overrides existing ones. If false, they throw.
     * @param block The code to execute when the type matches.
     */
    public fun infix(type: T, precedence: Int, override: Boolean = false, block: InfixFunction<T, E>)
}
