package net.notjustanna.tartar.api.lexer

expect class StringReader(s: String) {
    fun read(): Int
    fun skip(chars: Long): Long
    fun mark(readAheadLimit: Int)
    fun reset()
}
