package com.github.adriantodt.tartar.api.lexer

import kotlin.math.max
import kotlin.math.min

actual class StringReader actual constructor(s: String) {
    private var str: String = s
    private val length: Int = s.length
    private var next = 0
    private var mark = 0

    actual fun read(): Int {
        return if (next >= length) -1 else str[next++].code
    }

    actual fun skip(chars: Long): Long {
        if (next >= length) return 0
        var n = min(length - next.toLong(), chars)
        n = max(-next.toLong(), n)
        next += n.toInt()
        return n
    }

    actual fun mark(readAheadLimit: Int) {
        require(readAheadLimit >= 0) { "Read-ahead limit < 0" }
        mark = next
    }

    actual fun reset() {
        next = mark
    }
}
