name := "akkademy-db"

version := "1.0"

organization := "com.akkademy"

scalaVersion := "2.12.6"

lazy val akkaVersion = "2.5.16"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.0",
  "com.typesafe.akka" %% "akka-remote" % "2.5.16",

  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "junit" % "junit" % "4.12")

mappings in (Compile, packageBin) ~= { _.filterNot { case (_, name) =>
   Seq("application.conf").contains(name)
}}
