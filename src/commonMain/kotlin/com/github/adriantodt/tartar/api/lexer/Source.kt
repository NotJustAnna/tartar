package com.github.adriantodt.tartar.api.lexer

/**
 * A source of text to [lexers][Lexer].
 *
 * @constructor Creates a source based of a string.
 * @param content The content of the source.
 * @param name The name of the source.
 * @param path The path to the source.
 * @author AdrianTodt, An Tran
 */
data class Source(val content: String, val name: String = "?", val path: String = "!!no path!!") {
    /**
     * The lines of the content.
     */
    val lines = content.lines()
}
