package net.notjustanna.tartar.api.parser

import net.notjustanna.tartar.api.grammar.Grammar
import net.notjustanna.tartar.api.lexer.Source

/**
 * A parsing context, created by a [Parser.parse] call, which exposes an interface for pratt-parsing.
 *
 * @param T The parser's (and grammar's) token type.
 * @param E The parser's (and grammar's) expression result.
 * @author NotJustAnna
 */
public interface ParserContext<T, E> {
    /**
     * The source of this grammar's tokens.
     */
    public val source: Source

    /**
     * The grammar of this parser's context.
     */
    public val grammar: Grammar<T, E>

    /**
     * The current index in the list of tokens.
     */
    public var index: Int

    /**
     * A property which is true if there's no more tokens.
     */
    public val eof: Boolean

    /**
     * A property which contains the last token.
     */
    public val last: Token<T>

    /**
     * Creates a child parser context with the specified grammar.
     */
    public fun withGrammar(grammar: Grammar<T, E>): ParserContext<T, E>

    /**
     * Parses the expression using this parser's grammar.
     */
    public fun parseExpression(precedence: Int = 0): E

    /**
     * Parses the expression using another grammar.
     */
    public fun Grammar<T, E>.parseExpression(precedence: Int = 0): E = withGrammar(grammar).parseExpression(precedence)

    /**
     * Eats the current token, advancing the index by one.
     */
    public fun eat(): Token<T>

    /**
     * Eats the current token, advancing the index by one. Throws a [SyntaxException] if the token type doesn't match.
     */
    public fun eat(type: T): Token<T>

    /**
     * Equivalent to [nextIs], but eats the current token if true.
     */
    public fun match(type: T): Boolean

    /**
     * Equivalent to [nextIsAny], but eats the current token if true.
     */
    public fun matchAny(vararg type: T): Boolean

    /**
     * Move the index backwards one token and returns it.
     */
    public fun back(): Token<T>

    /**
     * Peeks a token a distance far away of the reader.
     */
    public fun peek(distance: Int = 0): Token<T>

    /**
     * Peeks the next token and if the token types are equal, returns true.
     */
    public fun nextIs(type: T): Boolean

    /**
     * Peeks the next token and if the token type is equal to any of the types, returns true.
     */
    public fun nextIsAny(vararg types: T): Boolean

    /**
     * Peeks the tokens ahead until a token of any of the types is found.
     */
    public fun peekAheadUntil(vararg type: T): List<Token<T>>

    /**
     * Skips tokens ahead until a token of any of the types is found.
     */
    public fun skipUntil(vararg type: T)
}
