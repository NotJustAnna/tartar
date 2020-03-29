package net.notjustanna.tartar.api.lexer

/**
 * Reads characters from a [Source] and outputs tokens.
 *
 * @param T The type of tokens the lexer generates.
 * @author NotJustAnna
 */
interface Lexer<T> {
    /**
     * Parses a source and outputs tokens into a consumer.
     *
     * @param source A source of characters.
     * @param output The consumer of tokens.
     */
    fun parse(source: Source, output: (T) -> Unit)

    /**
     * Parses a source and adds all tokens into a collection.
     *
     * @param source A source of characters.
     * @param collection The collection to add all tokens into.
     * @return The collection with the tokens.
     */
    fun <C : MutableCollection<in T>> parseTo(source: Source, collection: C): C {
        parse(source) { collection.add(it) }
        return collection
    }

    /**
     * Parses a source and adds all tokens into a list.
     *
     * @param source A source of characters.
     * @return A list with the tokens.
     */
    fun parseToList(source: Source): List<T> {
        return parseTo(source, ArrayList())
    }
}