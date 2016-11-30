name := "odin-examples"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.clulab" %% "processors-main" % "6.0.1",
  "org.clulab" %% "processors-corenlp" % "6.0.1",
  "org.clulab" %% "processors-models" % "6.0.1",
  "ai.lum" %% "nxmlreader" % "0.0.7",
  "ai.lum" %% "common" % "0.0.7"
)
