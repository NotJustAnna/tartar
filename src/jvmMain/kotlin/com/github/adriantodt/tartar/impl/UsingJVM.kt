package com.github.adriantodt.tartar.impl

import java.io.Closeable

internal actual fun <T, R> T.using(block: (T) -> R): R {
    return if (this is Closeable) this.use(block) else this.let(block)
}
