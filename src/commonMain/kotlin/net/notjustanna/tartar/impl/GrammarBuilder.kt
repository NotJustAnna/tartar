package net.notjustanna.tartar.impl

import net.notjustanna.tartar.api.dsl.GrammarDSL
import net.notjustanna.tartar.api.dsl.InfixFunction
import net.notjustanna.tartar.api.dsl.PrefixFunction
import net.notjustanna.tartar.api.grammar.Grammar
import net.notjustanna.tartar.api.grammar.InfixParselet
import net.notjustanna.tartar.api.grammar.PrefixParselet
import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.tartar.exceptions.ConfigurationException

internal class GrammarBuilder<T, K : Token<T>, E> : GrammarDSL<T, K, E> {
    private val prefix = LinkedHashMap<T, PrefixParselet<T, K, E>>()
    private val infix = LinkedHashMap<T, InfixParselet<T, K, E>>()

    override fun import(override: Boolean, vararg grammars: Grammar<T, K, E>) {
        grammars.forEach {
            it.prefix.forEach { (k, v) -> prefix(k, v, override) }
            it.infix.forEach { (k, v) -> infix(k, v, override) }
        }
    }

    override fun prefix(type: T, parselet: PrefixParselet<T, K, E>, override: Boolean) {
        if (!override && type in prefix) {
            throw ConfigurationException("Prefix parselet associated with $type already exists. Did you forget to enable overriding?")
        }
        prefix[type] = parselet
    }

    override fun prefix(type: T, override: Boolean, block: PrefixFunction<T, K, E>) {
        prefix(type, PrefixParseletImpl(block))
    }

    override fun infix(type: T, parselet: InfixParselet<T, K, E>, override: Boolean) {
        if (!override && type in infix) {
            throw ConfigurationException("Infix parselet associated with $type already exists. Did you forget to enable overriding?")
        }
        infix[type] = parselet
    }

    override fun infix(type: T, precedence: Int, override: Boolean, block: InfixFunction<T, K, E>) {
        infix(type, InfixParseletImpl(precedence, block))
    }

    internal fun build() = Grammar(prefix.toMap(), infix.toMap())
}

