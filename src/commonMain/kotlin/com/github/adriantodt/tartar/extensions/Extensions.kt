package com.github.adriantodt.tartar.extensions

/**
 * Returns true if the character is an ASCII letter.
 */
fun Char.isLetter(): Boolean {
    return this in ('a'..'z') || this in ('A'..'Z')
}

/**
 * Returns true if the character is an ASCII digit.
 */
fun Char.isDigit(): Boolean {
    return this in ('0'..'9')
}

/**
 * Returns true if the character is an ASCII letter or digit.
 */
fun Char.isLetterOrDigit(): Boolean {
    return isLetter() || isDigit()
}
