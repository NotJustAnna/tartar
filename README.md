# tartar

[![Maven metadata URL](https://img.shields.io/github/v/release/adriantodt/tartar?sort=semver)](https://maven.cafeteria.dev/releases/com/github/adriantodt/tartar)
[![GitHub issues](https://img.shields.io/github/issues/adriantodt/tartar)](https://github.com/adriantodt/tartar/issues)
[![License](https://img.shields.io/github/license/adriantodt/tartar)](https://github.com/adriantodt/tartar/tree/master/LICENSE)
[![Twitter](https://img.shields.io/twitter/url?style=social&url=https%3A%2F%2Fgithub.com%2Fadriantodt%2Ftartar)](https://twitter.com/intent/tweet?text=Wow:&url=https%3A%2F%2Fgithub.com%2Fadriantodt%2Ftartar)

Kotlin trie-based lexical analysis and pratt-parsing as a DSL.

### Installation

Using in Gradle:

```gradle
repositories {
  maven { url = 'https://maven.cafeteria.dev' }
}

dependencies {
  implementation 'com.github.adriantodt:tartar:VERSION'
}
```

Using in Maven:

```xml

<repositories>
    <repository>
        <id>cafeteria</id>
        <name>cafeteria</name>
        <url>https://maven.cafeteria.dev</url>
    </repository>
</repositories>

<dependencies>
<dependency>
    <groupId>com.github.adriantodt</groupId>
    <artifactId>tartar</artifactId>
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
val lexer = Lexer.create<Token<TokenType>> {
    // Implement a token type per line.
    '+' { process(makeToken(PLUS)) }
    // Built-in extension functions.
    '"' { process(makeToken(STRING, readString(it), offset = 1)) }
    // NOOP tokens.
    ' '()
    '\n'()
}

// Create a pratt-parser grammar. Dead simple.
val grammar = Grammar.create<TokenType, String> {
    // Create a prefix parselet as a lambda function.
    prefix(STRING) { token -> token.value }
    // Create an infix parselet, with support to precedence as a lambda function.
    infix(PLUS, 1) { left, _ -> left + parseExpression() }
}

// Use your grammar to create a pratt-parser.
val parser = SourceParser.create(lexer, grammar) { // Actual code run by the parser.
    // Extension function: Throws if there's still tokens.
    ensureEOF {
        // Parses a expression using this parsers' grammar.
        parseExpression()
    }
}

// One line of code to rule them all.
val result = parser.parse(Source.classpath { "input.str" })
println(result)
```

See the full code [here](https://github.com/adriantodt/tartar/blob/master/src/test/java/examples/StringJoiner.kt),
as well as other examples [here](https://github.com/adriantodt/tartar/tree/master/src/test/java/examples).

### Special thanks

Special thanks to [Avarel](https://github.com/Avarel), this library wouldn't be possible
without his help, feedback and source code of [Lobos](https://github.com/Avarel/Lobos)
and [Kaiper](https://github.com/Avarel/Kaiper).
