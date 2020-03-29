# tartar

[![Bintray](https://img.shields.io/bintray/v/adriantodt/maven/tartar)](https://bintray.com/adriantodt/maven/tartar/_latestVersion)
[![License](https://img.shields.io/github/license/adriantodt/tartar?color=lightgrey)](https://github.com/adriantodt/tartar/tree/master/LICENSE)
[![Twitter](https://img.shields.io/twitter/url?style=social&url=https%3A%2F%2Fgithub.com%2Fadriantodt%2Ftartar)](https://twitter.com/intent/tweet?text=Wow:&url=https%3A%2F%2Fgithub.com%2Fadriantodt%2Ftartar)

Kotlin trie-based lexical analysis and pratt-parsing as a DSL.

### Installation

Using in Gradle:

```gradle
repositories {
  jcenter()
}

dependencies {
  compile 'pw.aru.libs:fakenetwork:VERSION'
}
```

Using in Maven:

```xml
<repositories>
  <repository>
    <id>central</id>
    <name>bintray</name>
    <url>http://jcenter.bintray.com</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>pw.aru.libs</groupId>
    <artifactId>fakenetwork</artifactId>
    <version>VERSION</version>
  </dependency>
</dependencies>
```

### Usage

To create a lexer (also called a tokenizer) use `createLexer { ... }`, which exposes an interface to configure and add
matchers to the lexer.

To create a grammar use `createGrammar { ... }`, which exposes an interface to create and add prefix parsers and infix
parsers to the grammar.

To create a parser use `createParser(grammar) { ... }`, which will run the block of code on `parser.parse()`.

#### Example code

```kotlin
// Create a lexer as simple as a method call.
val lexer: Lexer<Token<TokenType>> = createLexer {
    // Implement a token type per line.
    '+' { process(makeToken(PLUS)) }
    // Built-in extension functions.
    '"' { process(makeToken(STRING, readString(it), offset = 1)) }
    // NOOP tokens.
    ' '()
    '\n'()
}

// Create a pratt-parser grammar. Dead simple.
val grammar: Grammar<TokenType, String> = createGrammar {
    // Create a prefix parselet as a lambda function.
    prefix(STRING) { token -> token.value.removeSurrounding("") }
    // Create a infix parselet, with support to precedence as a lambda function.
    infix(PLUS, 1) { left, token -> left + parseExpression() }
}

// Use your grammar to create a pratt-parser.
val parser = createParser(grammar) { // Actual code run by the parser. 
    // Extension function: Throws if there's still tokens.
    ensureEOF { 
        // Parses a expression using this parsers' grammar.
        parseExpression()
    }
}

// One line of code to rule them all.
val result = parser.parse(classpathSource { "input.str" }, lexer)
```

See the full code [here](https://github.com/adriantodt/tartar/blob/master/src/test/java/examples/StringJoiner.kt),
as well as other examples [here](https://github.com/adriantodt/tartar/tree/master/src/test/java/examples).

### Special thanks

Special thanks to [Avarel](https://github.com/Avarel), this library wouldn't be possible
without his help, feedback and source code of [Lobos](https://github.com/Avarel/Lobos)
and [Kaiper](https://github.com/Avarel/Kaiper).