package net.notjustanna.tartar.impl

internal actual fun <T, R> T.using(block: (T) -> R): R {
    return this.let(block)
}
