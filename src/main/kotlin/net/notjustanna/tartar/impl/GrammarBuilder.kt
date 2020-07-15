package net.notjustanna.tartar.impl

import net.notjustanna.tartar.api.GrammarDSL
import net.notjustanna.tartar.api.InfixFunction
import net.notjustanna.tartar.api.PrefixFunction
import net.notjustanna.tartar.api.parser.Grammar
import net.notjustanna.tartar.api.parser.InfixParser
import net.notjustanna.tartar.api.parser.PrefixParser

class GrammarBuilder<T, E> : GrammarDSL<T, E> {
    private val prefixParsers = LinkedHashMap<T, PrefixParser<T, E>>()
    private val infixParsers = LinkedHashMap<T, InfixParser<T, E>>()

    override fun import(override: Boolean, vararg grammars: Grammar<T, E>) {
        grammars.forEach {
            it.prefixParsers.forEach { (k, v) -> prefix(k, v, override) }
            it.infixParsers.forEach { (k, v) -> infix(k, v, override) }
        }
    }

    override fun prefix(type: T, parselet: PrefixParser<T, E>, override: Boolean) {
        if (!override && type in prefixParsers) {
            throw IllegalArgumentException("Prefix ṕarselet associated with $type already exists. Did you forget to enable overriding?")
        }
        prefixParsers[type] = parselet
    }

    override fun prefix(type: T, override: Boolean, block: PrefixFunction<T, E>) {
        prefix(type, PrefixParserImpl(block))
    }

    override fun infix(type: T, parselet: InfixParser<T, E>, override: Boolean) {
        if (!override && type in infixParsers) {
            throw IllegalArgumentException("Infix parselet associated with $type already exists. Did you forget to enable overriding?")
        }
        infixParsers[type] = parselet
    }

    override fun infix(type: T, precedence: Int, override: Boolean, block: InfixFunction<T, E>) {
        infix(type, InfixParserImpl(precedence, block))
    }

    fun build() = Grammar(prefixParsers.toMap(), infixParsers.toMap())
}

