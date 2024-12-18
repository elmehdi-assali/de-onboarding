ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

val scalaVersionMajor = "2.13"
val scalaVersionMinor = "12"

lazy val root = (project in file("."))
  .settings(
    name := "de-onboarding-elmehdi-assali"
  )

val akkaVersion = "2.6.20"
val akkaHttpVersion = "10.2.10"
val tapirVersion = "1.1.3"
val clickhouse4sVersion = "v21.3.13.65-alpha"
val prometheusVersion = "1.3.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.40.0-RC3",

  "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,

  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "ch.qos.logback" % "logback-classic" % "1.4.12",

  "io.prometheus" % "prometheus-metrics-core" % prometheusVersion,
  "io.prometheus" % "prometheus-metrics-exporter-httpserver" % prometheusVersion,
  "io.prometheus" % "prometheus-metrics-instrumentation-jvm" % prometheusVersion,


  "com.contentsquare" %% "clickhouse4s" % clickhouse4sVersion
    exclude ("io.circe", "circe-core_" ++ scalaVersionMajor)
    exclude ("io.circe", "circe-generic_" ++ scalaVersionMajor)
    exclude ("io.circe", "circe-parser_" ++ scalaVersionMajor)
    exclude ("io.circe", "circe-generic-extras_" ++ scalaVersionMajor)
    exclude ("de.heikoseeberger", "akka-http-circe_" ++ scalaVersionMajor),
)

resolvers ++= Seq(
  "Contentsquare New Maven Repository" at "https://artifactory.csq.fr/libs-release",
  "[Local] Contentsquare New Maven Repository" at "https://artifactory.csq.fr/libs-release-local",
  "Contentsquare Snapshot New Maven Repository" at "https://artifactory.csq.fr/libs-snapshot",
  "[Local] Contentsquare Snapshot New Maven Repository" at "https://artifactory.csq.fr/libs-snapshot-local"
)

ThisBuild / credentials ++= Seq(
  Credentials(
    "Artifactory Realm",
    "artifactory.csq.fr",
    System.getenv("ARTIFACTORY_USR"),
    System.getenv("ARTIFACTORY_PSW")
  )
)
