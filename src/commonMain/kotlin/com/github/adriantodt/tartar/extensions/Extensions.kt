package com.github.adriantodt.tartar.extensions

fun Char.isLetter(): Boolean {
    return this in ('a'..'z') || this in ('A'..'Z')
}

fun Char.isDigit(): Boolean {
    return this in ('0'..'9')
}

fun Char.isLetterOrDigit(): Boolean {
    return isLetter() || isDigit()
}
