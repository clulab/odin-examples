name := "odin-examples"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "edu.arizona.sista" %% "processors" % "5.5",
  "edu.arizona.sista" %% "processors" % "5.5" classifier "models"
)
