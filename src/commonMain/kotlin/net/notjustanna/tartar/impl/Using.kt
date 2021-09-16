package net.notjustanna.tartar.impl

internal expect fun <T, R> T.using(block: (T) -> R): R
