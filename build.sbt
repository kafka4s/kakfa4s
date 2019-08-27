import sbt._

ThisBuild / scalaVersion           := "2.12.8"
ThisBuild / version                := "0.1.0"
ThisBuild / organization           := "io.kafka4s"
ThisBuild / organizationName       := "Kafka4s"
ThisBuild / turbo                  := true
ThisBuild / concurrentRestrictions := Seq(Tags.limitAll(1))

lazy val kafka4s = project.in(file("."))
  .settings(
    // Root project
    name := "kafka4s",
    skip in publish := true,
    description := "A minimal Scala-idiomatic library for Kafka",
  )
  .aggregate(core)

lazy val core = project.in(file("core"))
  .settings(
    commonSettings
  )


lazy val commonSettings = Seq(
  fork in Test := true,
  fork in IntegrationTest := true,
  parallelExecution in Test := false,
  parallelExecution in IntegrationTest := false,
  libraryDependencies ++= Seq(
    "org.apache.kafka"    % "kafka-clients"        % "2.3.0",

    "org.typelevel"      %% "cats-core"            % "1.6.0",
    "org.typelevel"      %% "cats-effect"          % "1.4.0",

    "org.slf4j"          % "slf4j-api"             % "1.7.25",
    "ch.qos.logback"     % "logback-classic"       % "1.2.3",

    "org.scalatest"      %% "scalatest"            % "3.0.5" % s"$Test,$IntegrationTest",
    "org.scalamock"      %% "scalamock"            % "4.2.0" % s"$Test,$IntegrationTest",
    
    compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6"),
  ),
  scalacOptions ++= Seq(
    "-target:jvm-1.8",
    "-encoding", "utf-8",
    "-deprecation",
    "-explaintypes",
    "-feature",
    "-language:existentials",
    "-language:experimental.macros",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xcheckinit",
    "-Xfuture",
    "-Xlint:adapted-args",
    "-Xlint:by-name-right-associative",
    "-Xlint:constant",
    "-Xlint:delayedinit-select",
    "-Xlint:doc-detached",
    "-Xlint:inaccessible",
    "-Xlint:infer-any",
    "-Xlint:missing-interpolator",
    "-Xlint:nullary-override",
    "-Xlint:nullary-unit",
    "-Xlint:option-implicit",
    "-Xlint:package-object-classes",
    "-Xlint:poly-implicit-overload",
    "-Xlint:private-shadow",
    "-Xlint:stars-align",
    "-Xlint:type-parameter-shadow",
    "-Xlint:unsound-match",
    "-Yno-adapted-args",
    "-Ypartial-unification",
    "-Ywarn-dead-code",
    "-Ywarn-extra-implicit",
    "-Ywarn-inaccessible",
    "-Ywarn-infer-any",
    "-Ywarn-nullary-override",
    "-Ywarn-nullary-unit",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused:implicits",
    "-Ywarn-unused:imports",
    "-Ywarn-unused:locals",
    "-Ywarn-unused:params",
    "-Ywarn-unused:patvars",
    "-Ywarn-unused:privates",
    "-Ywarn-value-discard"
  ),
  javacOptions ++= Seq(
    "-source", "1.9",
    "-target", "1.9"
  )
)