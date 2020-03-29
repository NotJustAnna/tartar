package net.notjustanna.tartar.api.lexer

import java.io.File
import java.io.InputStream
import java.io.Reader

/**
 * A source of text to [lexers][Lexer].
 *
 * @constructor Creates a source based of a string.
 * @param content The content of the source.
 * @param name The name of the source.
 * @param path The path to the source.
 * @author NotJustAnna, An Tran
 */
data class Source(val content: String, val name: String = "?", val path: String = "!!no path!!") {
    /**
     * Creates a source based of a reader.
     *
     * @param reader The reader to get it's contents from.
     * @param name The name of the source.
     * @param path The path to the source.
     */
    constructor(reader: Reader, name: String, path: String) : this(reader.use(Reader::readText), name, path)

    /**
     * Creates a source based of a input stream.
     *
     * @param reader The input stream to get it's contents from.
     * @param name The name of the source.
     * @param path The path to the source.
     */
    constructor(inputStream: InputStream, name: String, path: String) : this(inputStream.reader(), name, path)

    /**
     * Creates a source based of a file.
     *
     * @param file The source file.
     */
    constructor(file: File, name: String = file.name, path: String = file.path) : this(file.reader(), name, path)

    /**
     * The lines of the content.
     */
    val lines = content.lines()
}
