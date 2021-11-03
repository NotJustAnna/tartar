package com.github.adriantodt.tartar.impl

internal expect inline fun <T, R> T.using(block: (T) -> R): R
