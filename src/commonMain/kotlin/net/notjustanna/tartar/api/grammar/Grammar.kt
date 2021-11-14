package net.notjustanna.tartar.api.grammar

import net.notjustanna.tartar.api.dsl.GrammarConfig
import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.tartar.impl.GrammarBuilder
import kotlin.jvm.JvmStatic

/**
 * A grammar for pratt-parsers.
 *
 * @param T The grammar's token type.
 * @param E The grammar's expression result.
 * @param prefix A map of prefix parsers for each token type.
 * @param infix A map of prefix parsers for each token type.
 * @author An Tran
 */
public data class Grammar<T, K : Token<T>, E>(
    public val prefix: Map<T, PrefixParselet<T, K, E>>,
    public val infix: Map<T, InfixParselet<T, K, E>>
) {
    public companion object {
        /**
         * Creates and configures a [Grammar].
         *
         * @param T The grammar's token type.
         * @param E The grammar's expression result.
         * @param block The grammar configurator.
         * @return A configured Grammar.
         * @author NotJustAnna
         */
        @JvmStatic
        public fun <T, K: Token<T>, E> create(block: GrammarConfig<T, K, E>): Grammar<T, K, E> {
            return GrammarBuilder<T, K, E>().apply(block).build()
        }
    }
}
