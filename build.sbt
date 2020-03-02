import Dependencies._

ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "rpsls",
    resolvers += Resolver.bintrayRepo("buildo", "maven"),
    libraryDependencies ++= Seq(
      "io.buildo" %% "enumero" % "1.3.0",
      "io.buildo" %% "enumero-circe-support" % "1.3.0",
      "com.typesafe.akka" %% "akka-http" % "10.0.10",
      "de.heikoseeberger" %% "akka-http-circe" % "1.18.0",
      "io.circe" %% "circe-core" % "0.10.1",
      "io.circe" %% "circe-generic" % "0.10.1",
      "io.buildo" %% "wiro-http-server" % "0.7.2",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
      "com.typesafe.slick" %% "slick" % "3.3.2",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
      "org.postgresql" % "postgresql" % "42.0.0",
      "org.typelevel" %% "cats-effect" % "1.1.0"
    ),
    addCompilerPlugin(
      "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
    )
  )
