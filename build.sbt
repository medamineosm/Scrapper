name := "ScrapperRemote"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  val akkaV = "2.4.0"
  val kamonVersion = "0.3.4"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-remote" % akkaV,
    "org.jsoup" % "jsoup" % "1.8.3+"
  )
}