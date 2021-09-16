package com.github.adriantodt.tartar

import com.github.adriantodt.tartar.api.lexer.Source

/**
 * Creates a source from the classpath.
 * @param lazyName A lambda to get the name from.
 * @return A source loaded from the classpath.
 * @author AdrianTodt
 */
fun classpathSource(lazyName: () -> String): Source {
    /*
     * RE: But WHY?
     *
     * Using a lambda lets us get a Java class (from said enclosing class),
     * allowing us to get resources from the enclosing's class classloader,
     * without having to do major shenanigans.
     */
    val relativeTo = lazyName.javaClass.enclosingClass
    val name = lazyName()
    val inputStream = relativeTo.getResourceAsStream(name)
    val resource = relativeTo.getResource(name)
    if (inputStream == null || resource == null) {
        throw IllegalArgumentException("Resource does not exist in classpath.")
    }

    return Source(inputStream.reader().readText(), name, resource.toString())
}

