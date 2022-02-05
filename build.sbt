lazy val V = _root_.scalafix.sbt.BuildInfo
inThisBuild(
  List(
    organization := "io.github.ghostbuster91.scalafix-unified",
    homepage := Some(url("https://github.com/scalacenter/named-literal-arguments")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "ghostbuster91",
        "Kasper Kondzielski",
        "kghost0@gmail.com",
        url("https://github.com/ghostbuster91")
      )
    ),
    scalaVersion := V.scala213,
    semanticdbEnabled := true,
    semanticdbIncludeInJar := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions ++= List("-Yrangepos")
  )
)

(publish / skip) := true

lazy val rules = project.settings(
  moduleName := "unified",
  libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
)

lazy val input = project.settings(
  (publish / skip) := true
)

lazy val output = project.settings(
  (publish / skip) := true
)

lazy val tests = project
  .settings(
    (publish / skip) := true,
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    scalafixTestkitOutputSourceDirectories :=
      (output / Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      (input / Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputClasspath :=
      (input / Compile / fullClasspath).value
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)

val root = project
  .in(file("."))
  .settings(publish / skip := true)
  .aggregate(input, rules, output, tests)