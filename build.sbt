lazy val v = _root_.scalafix.sbt.BuildInfo
lazy val rulesCrossVersions = Seq(v.scala213, v.scala212, v.scala211)
lazy val scala3Version = "3.1.1"

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
    scalaVersion := v.scala213,
    semanticdbEnabled := true,
    semanticdbIncludeInJar := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions ++= List("-Yrangepos")
  )
)

/** Below crossVersion project setup is based on
  * https://github.com/liancheng/scalafix-organize-imports/blob/master/build.sbt
  */
lazy val rules = projectMatrix
  .settings(
    moduleName := "unified",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % v.scalafixVersion
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(rulesCrossVersions)

lazy val input = projectMatrix
  .settings(
    (publish / skip) := true
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(scalaVersions = rulesCrossVersions :+ scala3Version)

lazy val output = projectMatrix
  .settings(
    (publish / skip) := true
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(scalaVersions = rulesCrossVersions :+ scala3Version)

lazy val tests = projectMatrix
  .settings(
    (publish / skip) := true,
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % v.scalafixVersion % Test cross CrossVersion.full,
    libraryDependencies +=
      "ch.epfl.scala" % "scalafix-testkit" % v.scalafixVersion % Test cross CrossVersion.full,
    scalafixTestkitOutputSourceDirectories := TargetAxis.resolve(output, Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputSourceDirectories := TargetAxis.resolve(input, Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputClasspath := TargetAxis.resolve(input, Compile / fullClasspath).value
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)
  .defaultAxes(
    rulesCrossVersions.map(VirtualAxis.scalaABIVersion) :+ VirtualAxis.jvm: _*
  )
  .customRow(
    scalaVersions = Seq(v.scala212),
    axisValues = Seq(TargetAxis(scala3Version), VirtualAxis.jvm),
    settings = Seq()
  )
  .customRow(
    scalaVersions = Seq(v.scala213),
    axisValues = Seq(TargetAxis(v.scala213), VirtualAxis.jvm),
    settings = Seq()
  )
  .customRow(
    scalaVersions = Seq(v.scala212),
    axisValues = Seq(TargetAxis(v.scala212), VirtualAxis.jvm),
    settings = Seq()
  )
  .customRow(
    scalaVersions = Seq(v.scala211),
    axisValues = Seq(TargetAxis(v.scala211), VirtualAxis.jvm),
    settings = Seq()
  )

lazy val testsAggregate = Project("tests", file("target/testsAggregate"))
  .aggregate(tests.projectRefs: _*)

val root = project
  .in(file("."))
  .settings(publish / skip := true)
  .aggregate(
    rules.projectRefs ++
      input.projectRefs ++
      output.projectRefs ++
      tests.projectRefs: _*
  )
