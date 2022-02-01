import sbt.ModuleID

val compilerOptions: Seq[String] =
  Seq(
    "-language:higherKinds",
    "-language:implicitConversions",
    "-feature",
    "-deprecation",
    "-unchecked"
  )

val libDependencies: Seq[ModuleID] =
  Seq(
    dependencies.cats.effect,
    dependencies.test.catsEffect,
    dependencies.test.minitest,
    dependencies.test.munit,
    dependencies.test.scalacheckEffect,
    dependencies.test.scalatest,
    dependencies.test.specs2,
    dependencies.test.utest,
    dependencies.test.weaver
  )

val testingFrameworks: Seq[TestFramework] =
  Seq(
    new TestFramework("minitest.runner.Framework"),
    new TestFramework("utest.runner.Framework"),
    new TestFramework("weaver.framework.CatsEffect")
)

lazy val root = (project in file("."))
  .settings(
    // Base definitions
    organization         := "com.github.aldtid",
    name                 := "testing-frameworks-comparison",
    scalaVersion         := "2.13.8",
    version              := "0.1.0-SNAPSHOT",
    scalacOptions       ++= compilerOptions,
    libraryDependencies ++= libDependencies,
    testFrameworks      ++= testingFrameworks,
    // Plugin definitions
    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)
  )

