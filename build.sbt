name := "odin-examples"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.clulab" %% "processors" % "5.6.1-SNAPSHOT",
  "org.clulab" %% "processors" % "5.6.1-SNAPSHOT" classifier "models"
)
