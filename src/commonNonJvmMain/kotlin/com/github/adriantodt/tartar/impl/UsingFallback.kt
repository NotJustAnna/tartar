package com.github.adriantodt.tartar.impl

internal actual fun <T, R> T.using(block: (T) -> R): R {
    return this.let(block)
}
