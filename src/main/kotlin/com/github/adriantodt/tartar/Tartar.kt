package com.github.adriantodt.tartar

import com.github.adriantodt.tartar.api.Closure
import com.github.adriantodt.tartar.api.GrammarDSL
import com.github.adriantodt.tartar.api.LexerDSL
import com.github.adriantodt.tartar.api.lexer.Lexer
import com.github.adriantodt.tartar.api.lexer.Source
import com.github.adriantodt.tartar.api.parser.Grammar
import com.github.adriantodt.tartar.api.parser.Parser
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.impl.GrammarBuilder
import com.github.adriantodt.tartar.impl.LexerImpl
import com.github.adriantodt.tartar.impl.MatcherImpl
import com.github.adriantodt.tartar.impl.ParserImpl

/**
 * Creates and configures a [Lexer].
 *
 * @param T The type of tokens the lexer generates.
 * @param block The lexer configurator.
 * @return A configured Lexer.
 * @author AdrianTodt
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
 * @author AdrianTodt
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
 * @author AdrianTodt
 */
fun <T, E, R> createParser(grammar: Grammar<T, E>, block: Closure<ParserContext<T, E>, R>): Parser<T, E, R> {
    return ParserImpl(grammar, block)
}

/**
 * Creates a source from the classpath.
 * @param lazyName A lambda to get the name from.
 * @return A source loaded from the classpath.
 * @author AdrianTodt
 */
fun classpathSource(lazyName: () -> String): Source {
    val relativeTo = lazyName.javaClass.enclosingClass
    val name = lazyName()
    return Source(relativeTo.getResourceAsStream(name), name, relativeTo.getResource(name).toString())
}
