package net.notjustanna.tartar.impl

import net.notjustanna.tartar.api.dsl.GrammarDSL
import net.notjustanna.tartar.api.dsl.InfixFunction
import net.notjustanna.tartar.api.dsl.PrefixFunction
import net.notjustanna.tartar.api.grammar.Grammar
import net.notjustanna.tartar.api.grammar.InfixParselet
import net.notjustanna.tartar.api.grammar.PrefixParselet

internal class GrammarBuilder<T, E> : GrammarDSL<T, E> {
    private val prefix = LinkedHashMap<T, PrefixParselet<T, E>>()
    private val infix = LinkedHashMap<T, InfixParselet<T, E>>()

    override fun import(override: Boolean, vararg grammars: Grammar<T, E>) {
        grammars.forEach {
            it.prefix.forEach { (k, v) -> prefix(k, v, override) }
            it.infix.forEach { (k, v) -> infix(k, v, override) }
        }
    }

    override fun prefix(type: T, parselet: PrefixParselet<T, E>, override: Boolean) {
        if (!override && type in prefix) {
            throw IllegalArgumentException("Prefix parselet associated with $type already exists. Did you forget to enable overriding?")
        }
        prefix[type] = parselet
    }

    override fun prefix(type: T, override: Boolean, block: PrefixFunction<T, E>) {
        prefix(type, PrefixParseletImpl(block))
    }

    override fun infix(type: T, parselet: InfixParselet<T, E>, override: Boolean) {
        if (!override && type in infix) {
            throw IllegalArgumentException("Infix parselet associated with $type already exists. Did you forget to enable overriding?")
        }
        infix[type] = parselet
    }

    override fun infix(type: T, precedence: Int, override: Boolean, block: InfixFunction<T, E>) {
        infix(type, InfixParseletImpl(precedence, block))
    }

    internal fun build() = Grammar(prefix.toMap(), infix.toMap())
}

