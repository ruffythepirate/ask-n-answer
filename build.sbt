name := """questopedia"""

version := "1.0"

scalaVersion := "2.11.7"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.mockito" % "mockito-core" % "1.9.5" % "test",

  "org.scala-lang" % "scala-swing" % "2.11+",
  "com.github.benhutchison" %% "scalaswingcontrib" % "1.6"
  )

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

