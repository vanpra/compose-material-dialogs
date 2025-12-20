[![official project](http://jb.gg/badges/official.svg)](https://github.com/JetBrains#jetbrains-on-github)

# Multiplatform library template

## What is it?

This repository contains a simple library project, intended to demonstrate a [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) library that is deployable to [Maven Central](https://central.sonatype.com/).

The library has only one function: generate the [Fibonacci sequence](https://en.wikipedia.org/wiki/Fibonacci_sequence) starting from platform-provided numbers. Also, it has a test for each platform just to be sure that tests run.

Note that no other actions or tools usually required for the library development are set up, such as [tracking of backwards compatibility](https://kotlinlang.org/docs/jvm-api-guidelines-backward-compatibility.html#tools-designed-to-enforce-backward-compatibility), explicit API mode, licensing, contribution guideline, code of conduct and others. You can find a guide for best practices for designing Kotlin libraries [here](https://kotlinlang.org/docs/api-guidelines-introduction.html).

## Guide

Please find the detailed guide [here](https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html).

# Other resources
* [Publishing via the Central Portal](https://central.sonatype.org/publish-ea/publish-ea-guide/)
* [Gradle Maven Publish Plugin \- Publishing to Maven Central](https://vanniktech.github.io/gradle-maven-publish-plugin/central/)
