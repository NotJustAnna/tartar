package net.notjustanna.tartar

import net.notjustanna.tartar.api.Closure
import net.notjustanna.tartar.api.GrammarDSL
import net.notjustanna.tartar.api.LexerDSL
import net.notjustanna.tartar.api.lexer.Lexer
import net.notjustanna.tartar.api.lexer.Source
import net.notjustanna.tartar.api.parser.Grammar
import net.notjustanna.tartar.api.parser.Parser
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.impl.GrammarBuilder
import net.notjustanna.tartar.impl.LexerImpl
import net.notjustanna.tartar.impl.MatcherImpl
import net.notjustanna.tartar.impl.ParserImpl

/**
 * Creates and configures a [Lexer].
 *
 * @param T The type of tokens the lexer generates.
 * @param block The lexer configurator.
 * @return A configured Lexer.
 * @author NotJustAnna
 */
fun <T> createLexer(block: Closure<LexerDSL<T>, Unit>): Lexer<T> {
    return LexerImpl(MatcherImpl<T>().apply(block))
}

/**
 * Creates and configures a [Grammar].
 *
 * @param T The grammar's token type.
 * @param E The grammar's expression result.
 * @param extends If set, the created grammar inherits this grammar's parsers.
 * @param block The grammar configurator.
 * @return A configured Grammar.
 * @author NotJustAnna
 */
fun <T, E> createGrammar(block: Closure<GrammarDSL<T, E>, Unit>): Grammar<T, E> {
    return GrammarBuilder<T, E>().apply(block).build()
}

/**
 * Creates and configures a [Parser].
 *
 * @param T The parser's (and grammar's) token type.
 * @param E The parser's (and grammar's) expression result.
 * @param R The parser's result.
 * @param grammar The grammar used by this parser.
 * @param block The parser function.
 * @return A configured Parser.
 * @author NotJustAnna
 */
fun <T, E, R> createParser(grammar: Grammar<T, E>, block: Closure<ParserContext<T, E>, R>): Parser<T, E, R> {
    return ParserImpl(grammar, block)
}

/**
 * Creates a source from the classpath.
 * @param lazyName A lambda to get the name from.
 * @return A source loaded from the classpath.
 * @author NotJustAnna
 */
fun classpathSource(lazyName: () -> String): Source {
    val relativeTo = lazyName.javaClass.enclosingClass
    val name = lazyName()
    return Source(relativeTo.getResourceAsStream(name), name, relativeTo.getResource(name).toString())
}
