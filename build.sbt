scalaVersion := "2.11.6"

name := "dsd-streaming"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-stream" % "4.0.2"
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)

