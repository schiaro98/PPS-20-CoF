name := "PPS-20-CoF"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"

// https://mvnrepository.com/artifact/com.google.code.gson/gson
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.8"

//scalaTest
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"

//scala parallelism
// https://mvnrepository.com/artifact/org.scala-lang.modules/scala-parallel-collections
libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"

// https://mvnrepository.com/artifact/jfree/jfreechart
libraryDependencies += "jfree" % "jfreechart" % "1.0.13"

scalacOptions += "-deprecation"