package com.github.adriantodt.tartar.impl

import com.github.adriantodt.tartar.api.dsl.CharPredicate
import com.github.adriantodt.tartar.api.dsl.LexerDSL
import com.github.adriantodt.tartar.api.dsl.MatchFunction

internal class MatcherImpl<T> : LexerDSL<T> {
    internal class MatcherWithPredicate<T>(val predicate: CharPredicate, val matcher: MatcherImpl<T>) {
        fun isMatcherEmpty() = matcher.isEmpty()
    }

    val trie = LinkedHashMap<Char, MatcherImpl<T>>()
    val predicates = ArrayList<MatcherWithPredicate<T>>()
    var onMatch: MatchFunction<T>? = null

    fun isEmpty() = trie.isEmpty() && predicates.isEmpty() && onMatch == null

    override fun matching(string: String): MatcherImpl<T> {
        return when (string.length) {
            0 -> this
            1 -> matching(string.first())
            else -> string.fold(this, MatcherImpl<T>::matching)
        }
    }

    override fun matching(char: Char): MatcherImpl<T> {
        return trie.getOrPut(char, ::MatcherImpl)
    }

    override fun matching(block: CharPredicate): MatcherImpl<T> {
        val matcher = MatcherImpl<T>()
        predicates += MatcherWithPredicate(block, matcher)
        return matcher
    }

    override fun configure(block: MatchFunction<T>) {
        onMatch = block
    }
}
