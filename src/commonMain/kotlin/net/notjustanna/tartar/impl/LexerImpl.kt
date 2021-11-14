package net.notjustanna.tartar.impl

import net.notjustanna.tartar.api.dsl.CharPredicate
import net.notjustanna.tartar.api.dsl.MatchFunction
import net.notjustanna.tartar.api.lexer.*
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.extensions.lexer.section

internal class LexerImpl<T>(root: MatcherImpl<T>) : Lexer<T> {
    private val matcher = LexerMatcher(root)

    override fun parse(source: Source, output: (T) -> Unit) {
        ContextImpl(source, output).use { ctx ->
            while (ctx.hasNext()) doParse(ctx)
        }
    }

    private fun doParse(impl: ContextImpl, ctx: LexerContext<T> = impl) {
        if (impl.hasNext()) {
            impl.read = 0

            val function = matcher.doMatch(impl).onMatch
            if (function != null) {
                function(ctx, impl.curr)
            } else {
                matcher.skipUntilMatch(impl)
                val section = impl.section(impl.read)
                throw SyntaxException("No matcher registered for '${section.substring}'", section)
            }

            if (impl.read == 0) throw IllegalStateException("No further characters consumed.")
        }
    }

    private class LexerMatcher<T>(
        private val trie: Map<Char, LexerMatcher<T>>,
        private val predicates: List<LexerMatcherWithPredicate<T>>,
        val onMatch: MatchFunction<T>?
    ) {
        constructor(m: MatcherImpl<T>) : this(
            m.trie.filterNot { it.value.isEmpty() }.mapValues { LexerMatcher(it.value) },
            m.predicates.filterNot { it.isMatcherEmpty() }.map { LexerMatcherWithPredicate(it) },
            m.onMatch
        )

        fun tryMatchChild(char: Char): LexerMatcher<T>? {
            return trie[char] ?: predicates.firstOrNull { it.predicate.test(char) }?.matcher
        }
    }

    private class LexerMatcherWithPredicate<T>(val predicate: CharPredicate, val matcher: LexerMatcher<T>) {
        constructor(m: MatcherImpl.MatcherWithPredicate<T>) : this(m.predicate, LexerMatcher(m.matcher))
    }

    private tailrec fun LexerMatcher<*>.skipUntilMatch(ctx: ContextImpl) {
        if (!ctx.hasNext()) return
        val char = ctx.peek()
        if (tryMatchChild(char) != null || char == '\n') return
        ctx.next()
        skipUntilMatch(ctx)
    }

    private tailrec fun LexerMatcher<T>.doMatch(ctx: ContextImpl, eat: Boolean = false): LexerMatcher<T> {
        if (eat) ctx.next()
        return (tryMatchChild(ctx.peek()) ?: return this).doMatch(ctx, true)
    }

    private inner class ContextImpl(override val source: Source, private val output: (T) -> Unit) : LexerContext<T> {
        private inner class CollectingContext(private val collection: MutableCollection<T>) : LexerContext<T> by this {
            override fun process(token: T) {
                collection.add(token)
            }
        }

        override val reader: StringReader = StringReader(source.content)

        var read = 0

        override var index: Int = 0
            private set

        var curr: Char = (-1).toChar()

        override fun peek(): Char {
            reader.mark(1)
            val c = reader.read().toChar()
            reader.reset()
            return c
        }

        override fun peek(distance: Int): Char {
            reader.mark(distance + 1)
            var value = -1
            for (i in 0..distance) {
                val next = reader.read()
                if (next == -1) {
                    value = -1
                    break
                } else if (i != distance) {
                    continue
                }
                value = next
            }
            // val value = generateSequence { reader.read().takeUnless { it == -1 } }.elementAtOrNull(distance) ?: -1
            reader.reset()
            return value.toChar()
        }

        override fun peekString(length: Int): String {
            reader.mark(length)
            val value = buildString(length) {
                for (i in 0 until length) {
                    val next = reader.read()
                    if (next == -1) break
                    append(next.toChar())
                }
            }
            reader.reset()
            return value
        }

        override fun match(expect: Char): Boolean {
            val matched = peek() == expect
            if (matched) next()
            return matched
        }

        override fun hasNext(): Boolean {
            reader.mark(1)
            val i = reader.read()
            reader.reset()
            return i > 0
        }

        override fun next(): Char {
            val c = reader.read().toChar()
            read++
            index++
            curr = c
            return c
        }

        override fun nextString(length: Int): String {
            return buildString(length) {
                var i = 0
                while (hasNext() && i++ < length) append(next())
            }
        }

        override fun process(token: T) {
            output(token)
        }

        override fun parseOnce(): List<T> {
            return mutableListOf<T>().also { doParse(this, CollectingContext(it)) }
        }

        internal inline fun <R> use(block: (ContextImpl) -> R): R {
            return reader.using { block(this) }
        }
    }
}
