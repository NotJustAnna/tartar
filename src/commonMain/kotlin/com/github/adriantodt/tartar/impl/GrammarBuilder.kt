package com.github.adriantodt.tartar.impl

import com.github.adriantodt.tartar.api.dsl.GrammarDSL
import com.github.adriantodt.tartar.api.dsl.InfixFunction
import com.github.adriantodt.tartar.api.dsl.PrefixFunction
import com.github.adriantodt.tartar.api.grammar.Grammar
import com.github.adriantodt.tartar.api.grammar.InfixParselet
import com.github.adriantodt.tartar.api.grammar.PrefixParselet
import com.github.adriantodt.tartar.api.parser.Token

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
            throw IllegalArgumentException("Prefix parselet associated with $type already exists. Did you forget to enable overriding?")
        }
        prefix[type] = parselet
    }

    override fun prefix(type: T, override: Boolean, block: PrefixFunction<T, K, E>) {
        prefix(type, PrefixParseletImpl(block))
    }

    override fun infix(type: T, parselet: InfixParselet<T, K, E>, override: Boolean) {
        if (!override && type in infix) {
            throw IllegalArgumentException("Infix parselet associated with $type already exists. Did you forget to enable overriding?")
        }
        infix[type] = parselet
    }

    override fun infix(type: T, precedence: Int, override: Boolean, block: InfixFunction<T, K, E>) {
        infix(type, InfixParseletImpl(precedence, block))
    }

    internal fun build() = Grammar(prefix.toMap(), infix.toMap())
}

