ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "StudentsJournal"
  )

val Http4sVersion = "1.0.0-M30"
val CirceVersion = "0.15.0-M1"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,

  "io.circe" %% "circe-core" % CirceVersion,
  "io.circe" %% "circe-generic" % CirceVersion,
  "io.circe" %% "circe-parser" % CirceVersion,

  "org.scalatest" %% "scalatest" % "3.2.12",
  "org.scalamock" %% "scalamock" % "5.2.0",

  "org.reactivemongo" %% "reactivemongo" % "1.0.10",
)
