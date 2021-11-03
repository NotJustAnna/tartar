package com.github.adriantodt.tartar.api.dsl

/**
 * Represents a predicate (boolean-valued function) of one [Char]-valued argument.
 *
 * @since 3.0
 */
public fun interface CharPredicate {
    /**
     * Evaluates this predicate on the given argument.
     *
     * @param value the input argument
     * @return true if the input argument matches the predicate, otherwise false.
     */
    public fun test(value: Char): Boolean

    public companion object {
        /**
         * [CharPredicate] which predicate is the function [Char.isLetter].
         */
        public val isLetter: CharPredicate = CharPredicate(Char::isLetter)

        /**
         * [CharPredicate] which predicate is the function [Char.isDigit].
         */
        public val isDigit: CharPredicate = CharPredicate(Char::isDigit)

        /**
         * [CharPredicate] which predicate is the function [Char.isLetterOrDigit].
         */
        public val isLetterOrDigit: CharPredicate = CharPredicate(Char::isLetterOrDigit)
    }
}
