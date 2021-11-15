package com.github.adriantodt.tartar.exceptions

import com.github.adriantodt.tartar.api.lexer.Section

public class MismatchedSourcesException(first: Section, second: Section) : IllegalArgumentException(
    "Sections $first and $second have different sources and thus can't be spanned."
)
