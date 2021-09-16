package com.github.adriantodt.tartar.impl

internal expect fun <T, R> T.using(block: (T) -> R): R
