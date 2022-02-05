# scalafix-unified

Set of opinionated scalafix rules to unify your codebase

## Why?

First of all, because I wanted to write a scalafix rule :)

Second, I find discussions about such basic things like `Nil` vs `List()` vs `List.empty` unproductive. But on the 
other hand it is nice to have codebase unified as it was written by a single person.

## Rules

### EmptyCollectionsUnified

Rewrite empty collection constructions like `List()`, `Set()` or `Map()` to their equivalent `.empty` method calls

## Installation

For now only snapshots are published

```scala
ThisBuild / scalafixResolvers +=  coursierapi.MavenRepository.of("https://s01.oss.sonatype.org/content/repositories/snapshots")
ThisBuild /  scalafixDependencies += "io.github.ghostbuster91.scalafix-unified" %% "unified" % "0.0.2+1-01e64859-SNAPSHOT"
```