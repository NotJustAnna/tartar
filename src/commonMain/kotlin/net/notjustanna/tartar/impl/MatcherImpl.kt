package net.notjustanna.tartar.impl

import net.notjustanna.tartar.api.CharPredicate
import net.notjustanna.tartar.api.ClosureFunction
import net.notjustanna.tartar.api.LexerDSL
import net.notjustanna.tartar.api.lexer.LexerContext

class MatcherImpl<T> : LexerDSL<T> {
    val trie = LinkedHashMap<Char, MatcherImpl<T>>()
    val predicates = ArrayList<Pair<CharPredicate, MatcherImpl<T>>>()
    var onMatch: ClosureFunction<LexerContext<T>, Char, Unit>? = null

    fun isEmpty() = trie.isEmpty() && predicates.isEmpty() && onMatch == null

    override fun matching(string: String): MatcherImpl<T> {
        return when (string.length) {
            0 -> this
            1 -> matching(string.first())
            else -> string.substring(1).fold(matching(string.first()), MatcherImpl<T>::matching)
        }
    }

    override fun matching(char: Char): MatcherImpl<T> {
        return trie.getOrPut(char, ::MatcherImpl)
    }

    override fun matching(block: CharPredicate): MatcherImpl<T> {
        val m = MatcherImpl<T>()
        predicates.add(block to m)
        return m
    }

    override fun configure(block: ClosureFunction<LexerContext<T>, Char, Unit>) {
        onMatch = block
    }
}
