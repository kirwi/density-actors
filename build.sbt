val scala3Version = "3.7.4"

lazy val root = project
  .in(file("."))
  .settings(
    name := "density-actors",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.19.0" % Test
  )
