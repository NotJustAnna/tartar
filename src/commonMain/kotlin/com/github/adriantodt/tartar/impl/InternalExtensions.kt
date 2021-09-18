package com.github.adriantodt.tartar.impl

private val lineSeparators = listOf("\r\n", "\n", "\r")

internal fun CharSequence.calculateLineRanges(): List<IntRange> {
    var currentIndex = 0 // Current Start Index
    var nextIndex = 0 // Next Search Index

    val list = mutableListOf<IntRange>()

    while (nextIndex < length) {
        val (index, s) = findAnyOf(lineSeparators, nextIndex) ?: break
        val length = s.length
        list += currentIndex until index + length
        currentIndex = index + length
        nextIndex = currentIndex + if (length == 0) 1 else 0
    }
    list += currentIndex..lastIndex

    return list
}
