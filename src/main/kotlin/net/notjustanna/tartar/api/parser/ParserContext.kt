package net.notjustanna.tartar.api.parser

import net.notjustanna.tartar.api.lexer.Source

/**
 * A parsing context, created by a [Parser.parse] call, which exposes an interface for pratt-parsing.
 *
 * @param T The parser's (and grammar's) token type.
 * @param E The parser's (and grammar's) expression result.
 * @author NotJustAnna
 */
interface ParserContext<T, E> {
    /**
     * The source of this grammar's tokens.
     */
    val source: Source

    /**
     * The grammar of this parser's context.
     */
    val grammar: Grammar<T, E>

    /**
     * The current index in the list of tokens.
     */
    val index: Int

    /**
     * A property which is true if there's no more tokens.
     */
    val eof: Boolean

    /**
     * A property which contains the last token.
     */
    val last: Token<T>

    /**
     * Creates a child parser context with the specified grammar.
     */
    fun withGrammar(grammar: Grammar<T, E>): ParserContext<T, E>

    /**
     * Parses the expression using this parser's grammar.
     */
    fun parseExpression(precedence: Int = 0): E

    /**
     * Parses the expression using another grammar.
     */
    fun Grammar<T, E>.parseExpression(precedence: Int = 0): E = withGrammar(grammar).parseExpression(precedence)

    /**
     * Eats the current token, advancing the index by one.
     */
    fun eat(): Token<T>

    /**
     * Eats the current token, advancing the index by one. Throws a [SyntaxException] if the token type doesn't match.
     */
    fun eat(type: T): Token<T>

    /**
     * Equivalent to [nextIs], but eats the current token if true.
     */
    fun match(type: T): Boolean

    /**
     * Equivalent to [nextIsAny], but eats the current token if true.
     */
    fun matchAny(vararg type: T): Boolean

    /**
     * Move the index backwards one token and returns it.
     */
    fun back(): Token<T>

    /**
     * Peeks a token a distance far away of the reader.
     */
    fun peek(distance: Int = 0): Token<T>

    /**
     * Peeks the next token and if the token types are equal, returns true.
     */
    fun nextIs(type: T): Boolean

    /**
     * Peeks the next token and if the token type is equal to any of the types, returns true.
     */
    fun nextIsAny(vararg types: T): Boolean

    /**
     * Peeks the tokens ahead until a token of any of the types is found.
     */
    fun peekAheadUntil(vararg type: T): List<Token<T>>

    /**
     * Skips tokens ahead until a token of any of the types is found.
     */
    fun skipUntil(vararg type: T)
}