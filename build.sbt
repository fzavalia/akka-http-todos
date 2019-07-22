name := "akka-http-todos"

version := "0.1"

scalaVersion := "2.13.0"

// Akka
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.8"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.23"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8"


// Test Tools
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.23"
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % "10.1.8"