package com.github.adriantodt.tartar.impl

import com.github.adriantodt.tartar.api.CharPredicate
import com.github.adriantodt.tartar.api.ClosureFunction
import com.github.adriantodt.tartar.api.lexer.Lexer
import com.github.adriantodt.tartar.api.lexer.LexerContext
import com.github.adriantodt.tartar.api.lexer.Source
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.extensions.section
import java.io.Closeable
import java.io.Reader

class LexerImpl<T>(root: MatcherImpl<T>) : Lexer<T> {
    private val matcher = LexerMatcher(root)

    override fun parse(source: Source, output: (T) -> Unit) {
        ContextImpl(source, output).use { ctx ->
            while (ctx.hasNext()) {
                ctx.read = 0

                matcher.doMatch(ctx).onMatch?.invoke(ctx, ctx.curr)
                    ?: throw SyntaxException("No matcher registered", ctx.section(ctx.read, ctx.read))

                if (ctx.read == 0) {
                    throw IllegalStateException("No further characters consumed.")
                }
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

    private tailrec fun LexerMatcher<T>.doMatch(ctx: ContextImpl, eat: Boolean = false): LexerMatcher<T> {
        if (eat) ctx.next()
        return (tryMatchChild(ctx.peek()) ?: return this).doMatch(ctx, true)
    }

    inner class ContextImpl(override val source: Source, private val output: (T) -> Unit) : LexerContext<T>, Closeable {
        override val reader: Reader = source.content.reader()

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
            val array = CharArray(distance + 1)
            val result = when {
                reader.read(array) < distance + 1 -> (-1).toChar()
                else -> array[distance]
            }
            reader.reset()
            return result
        }

        override fun peekString(length: Int): String {
            val array = CharArray(length)
            reader.mark(length)
            val len = reader.read(array)
            reader.reset()
            return when {
                len == -1 -> ""
                len < length -> String(array.copyOf(len))
                else -> String(array)
            }
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

        override fun close() {
            reader.close()
        }
    }
}