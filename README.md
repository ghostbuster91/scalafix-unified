# scalafix-unified

Set of opinionated scalafix rules to unify your codebase

## Why?

First of all, because I wanted to write a scalafix rule :)

Second, I find discussions about such basic things like `Nil` vs `List()` vs `List.empty` unproductive. But on the 
other hand it is nice to have codebase unified as it was written by a single person.

## Rules

### EmptyCollectionsUnified

Rewrite empty collection constructions like `List()`, `Set()` or `Map()` to their equivalent `.empty` method calls.

Rewrite `List()` when used inside match-case statement to `Nil`.

## Installation

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.ghostbuster91.scalafix-unified/unified/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.ghostbuster91.scalafix-unified/unified)

```scala
ThisBuild /  scalafixDependencies += "io.github.ghostbuster91.scalafix-unified" %% "unified" % "<version>"
```