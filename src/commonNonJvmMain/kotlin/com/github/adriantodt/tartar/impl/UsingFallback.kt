package com.github.adriantodt.tartar.impl

internal actual inline fun <T, R> T.using(block: (T) -> R): R {
    return block(this)
}
