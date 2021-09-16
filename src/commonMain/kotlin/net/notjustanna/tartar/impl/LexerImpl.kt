package net.notjustanna.tartar.impl

import net.notjustanna.tartar.api.CharPredicate
import net.notjustanna.tartar.api.ClosureFunction
import net.notjustanna.tartar.api.lexer.Lexer
import net.notjustanna.tartar.api.lexer.LexerContext
import net.notjustanna.tartar.api.lexer.Source
import net.notjustanna.tartar.api.lexer.StringReader
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.extensions.section

class LexerImpl<T>(root: MatcherImpl<T>) : Lexer<T> {
    private val matcher = LexerMatcher(root)

    override fun parse(source: Source, output: (T) -> Unit) {
        ContextImpl(source, output).use { ctx ->
            while (ctx.hasNext()) {
                doParse(ctx)
            }
        }
    }


    fun doParse(impl: ContextImpl, ctx: LexerContext<T> = impl) {
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

            if (impl.read == 0) {
                throw IllegalStateException("No further characters consumed.")
            }
        }
    }

    data class LexerMatcher<T>(
        val trie: Map<Char, LexerMatcher<T>>,
        val predicates: List<Pair<CharPredicate, LexerMatcher<T>>>,
        val onMatch: ClosureFunction<LexerContext<T>, Char, Unit>?
    ) {
        constructor(m: MatcherImpl<T>) : this(
            m.trie.filterNot { it.value.isEmpty() }.mapValues { LexerMatcher(it.value) },
            m.predicates.filterNot { it.second.isEmpty() }.map { it.first to LexerMatcher(it.second) },
            m.onMatch
        )

        fun tryMatchChild(char: Char): LexerMatcher<T>? {
            return trie[char] ?: predicates.firstOrNull { it.first(char) }?.second
        }
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

    inner class ContextImpl(override val source: Source, private val output: (T) -> Unit) : LexerContext<T> {
        inner class CollectingContext(
            private val collection: MutableCollection<T>
        ) : LexerContext<T> by this {
            override fun process(token: T) {
                collection.add(token)
            }
        }

        override val reader: StringReader = StringReader(source.content)

        var read = 0

        override var lineNumber: Int = 1
            private set
        override var lineIndex: Int = 0
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
            val value = generateSequence { reader.read().takeUnless { it == -1 } }.elementAtOrNull(distance) ?: -1
            reader.reset()
            return value.toChar()
        }

        override fun peekString(length: Int): String {
            reader.mark(length)
            val value = generateSequence { reader.read().takeUnless { it == -1 }?.toChar() }
                .take(length)
                .fold(StringBuilder(), StringBuilder::append)
                .toString()
            reader.reset()
            return value
        }

        override fun match(expect: Char): Boolean {
            return if (peek() == expect) {
                next()
                true
            } else {
                false
            }
        }

        override fun hasNext(): Boolean {
            reader.mark(1)
            val i = reader.read()
            reader.reset()
            return i > 0
        }

        override fun next(): Char {
            read++
            val c = reader.read().toChar()

            when (c) {
                '\n' -> {
                    lineNumber++
                    lineIndex = 0
                }
                else -> {
                    lineIndex++
                }
            }

            curr = c
            return c
        }

        override fun nextString(length: Int): String {
            val buf = StringBuilder(length)
            var i = 0
            while (hasNext() && i++ < length) {
                buf.append(next())
            }
            return buf.toString()
        }

        override fun process(token: T) {
            output(token)
        }

        override fun parseOnce(): List<T> {
            val tokens = mutableListOf<T>()
            doParse(this, CollectingContext(tokens))
            return tokens
        }

        fun <R> use(block: (ContextImpl) -> R): R {
            return reader.using { block(this) }
        }
    }
}
