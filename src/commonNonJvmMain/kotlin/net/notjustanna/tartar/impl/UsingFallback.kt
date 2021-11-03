package net.notjustanna.tartar.impl

internal actual inline fun <T, R> T.using(block: (T) -> R): R {
    return block(this)
}
