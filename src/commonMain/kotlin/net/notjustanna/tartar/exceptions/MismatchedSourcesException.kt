package net.notjustanna.tartar.exceptions

import net.notjustanna.tartar.api.lexer.Section

public class MismatchedSourcesException(first: Section, second: Section) : IllegalArgumentException(
    "Sections $first and $second have different sources and thus can't be spanned."
)
