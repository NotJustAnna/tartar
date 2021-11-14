package net.notjustanna.tartar.extensions.parser

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token

/**
 * After executing a given [block], throws a [SyntaxException] if remaining tokens are found in the [ParserContext].
 * Otherwise, returns the [block]'s computed result.
 *
 * @receiver The context of this extension function.
 * @param R The [block]'s return type.
 * @param block A function to execute, which receives no arguments and may return anything.
 * @throws SyntaxException if tokens are found in the [ParserContext] after executing the given [block].
 * @return The [block]'s result, if no tokens are found in the [ParserContext] after execution.
 */
public fun <R> ParserContext<*, *, *>.ensureEOF(block: () -> R): R {
    val r = block()
    if (!eof) throw SyntaxException("Should've reached end of content", eat().section)
    return r
}

/**
 * [Eats][ParserContext.eat] a sequence of tokens in a row of the specified [types],
 * returning a list of the consumed tokens if all the specified [types] matched successfully.
 *
 * If any of the types on the sequence do not match, a [SyntaxException] will be thrown.
 *
 * @receiver The context of this extension function.
 * @param T The parser's (and underlying grammar's) token type.
 * @param types a sequence of token types to match against the [context][ParserContext].
 * @throws SyntaxException if any of the types on the sequence do not match.
 * @return A list of all the consumed tokens, if all the specified [types] matched successfully.
 */
public fun <T, K: Token<T>> ParserContext<T, K, *>.eatSequence(vararg types: T): List<K> {
    return types.map(this::eat)
}

/**
 * [Eats][ParserContext.eat] a sequence of tokens in a row of the specified [types],
 * returning a list of the consumed tokens if all the specified [types] matched successfully.
 *
 * If any of the types on the sequence do not match, a [SyntaxException] will be thrown.
 *
 * ### Deprecation Notice:
 *
 * This function was renamed to [eatSequence] and will be removed in the next major release.
 *
 * @receiver The context of this extension function.
 * @param T The parser's (and underlying grammar's) token type.
 * @param types a sequence of token types to match against the [context][ParserContext].
 * @throws SyntaxException if any of the types on the sequence do not match.
 * @return A list of all the consumed tokens, if all the specified [types] matched successfully.
 * @see eatSequence
 */
@Deprecated("Renamed to eatSequence", ReplaceWith("eatSequence(*types)"))
public fun <T, K: Token<T>> ParserContext<T, K, *>.eatMulti(vararg types: T): List<K> {
    return eatSequence(*types)
}
