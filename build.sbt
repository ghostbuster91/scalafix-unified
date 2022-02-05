lazy val v = _root_.scalafix.sbt.BuildInfo
lazy val rulesCrossVersions = Seq(v.scala213, v.scala212, v.scala211)
lazy val scala3Version = "3.1.1"

val commonSettings = Seq(
  organization := "io.github.ghostbuster91.scalafix-unified",
  homepage := Some(url("https://github.com/ghostbuster91/scalafix-unified")),
  licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(
    Developer(
      "ghostbuster91",
      "Kasper Kondzielski",
      "kghost0@gmail.com",
      url("https://github.com/ghostbuster91")
    )
  ),
  sonatypeCredentialHost := "s01.oss.sonatype.org",
  sonatypeRepository := "https://s01.oss.sonatype.org/service/local",
  sonatypeProfileName := "io.github.ghostbuster91",
  scalacOptions ++= List("-Yrangepos"),
  sonatypeSnapshotResolver := {
    MavenRepository(
      "s01-sonatype-snapshots",
      s"https://${sonatypeCredentialHost.value}/content/repositories/snapshots"
    )
  },
  sonatypeStagingResolver := {
    MavenRepository(
      "s01-sonatype-staging",
      s"https://${sonatypeCredentialHost.value}/service/local/staging/deploy/maven2"
    )
  },
  sonatypeBundleDirectory := {
    (ThisBuild / baseDirectory).value / "target" / "s01-sonatype-staging" / s"${(ThisBuild / version).value}"
  },
  publishTo :=  {
    val profileM   = sonatypeTargetRepositoryProfile.?.value
    val repository = sonatypeRepository.value
    val staged = profileM.map { stagingRepoProfile =>
      "releases" at s"${repository}/${stagingRepoProfile.deployPath}"
    }
    Some(staged.getOrElse(if (version.value.endsWith("-SNAPSHOT")) {
      sonatypeSnapshotResolver.value
    } else {
      sonatypeStagingResolver.value
    }))
  }
)

inThisBuild(
  List(
    semanticdbEnabled := true,
    semanticdbIncludeInJar := true,
    semanticdbVersion := scalafixSemanticdb.revision,
  )
)

/** Below crossVersion project setup is based on
  * https://github.com/liancheng/scalafix-organize-imports/blob/master/build.sbt
  */
lazy val rules = projectMatrix
  .settings(commonSettings)
  .settings(
    moduleName := "unified",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % v.scalafixVersion
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(rulesCrossVersions)

lazy val input = projectMatrix
  .settings(commonSettings)
  .settings(
    (publish / skip) := true
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(scalaVersions = rulesCrossVersions :+ scala3Version)

lazy val output = projectMatrix
  .settings(commonSettings)
  .settings(
    (publish / skip) := true
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(scalaVersions = rulesCrossVersions :+ scala3Version)

lazy val tests = projectMatrix
  .settings(commonSettings)
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
  .settings(commonSettings)
  .aggregate(tests.projectRefs: _*)

val root = project
  .in(file("."))
  .settings(commonSettings)
  .settings(publish / skip := true)
  .aggregate(
    rules.projectRefs ++
      input.projectRefs ++
      output.projectRefs ++
      tests.projectRefs: _*
  )
