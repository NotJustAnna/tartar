package net.notjustanna.tartar.api.dsl

/**
 * A builder of [Lexers][net.notjustanna.tartar.api.lexer.Lexer], as a domain-specific language (DSL).
 *
 * @param T The type of tokens the lexer generates.
 * @author NotJustAnna
 */
public interface LexerDSL<T> {
    /**
     * Configures the lexer to execute a specific block of code when this character matches.
     */
    public operator fun Char.invoke(block: MatchFunction<T> = {}) {
        matching(this).configure(block)
    }

    /**
     * Configures the lexer to execute a specific block of code when this sequence of characters matches.
     */
    public operator fun String.invoke(block: MatchFunction<T> = {}) {
        matching(this).configure(block)
    }

    /**
     * Returns a matcher for a sequence of characters.
     */
    public fun matching(string: String): LexerDSL<T>

    /**
     * Returns a matcher for a sequence of characters and configures it.
     */
    public fun matching(string: String, block: LexerConfig<T>) {
        matching(string).block()
    }

    /**
     * Returns a matcher for a character.
     */
    public fun matching(char: Char): LexerDSL<T>

    /**
     * Returns a matcher for a characters and configures it.
     */
    public fun matching(char: Char, block: LexerConfig<T>) {
        matching(char).block()
    }

    /**
     * Returns a matcher for a predicate.
     */
    public fun matching(block: CharPredicate): LexerDSL<T>

    /**
     * Configures the lexer to execute a specific block of code when match.
     */
    public fun configure(block: MatchFunction<T>)
}
