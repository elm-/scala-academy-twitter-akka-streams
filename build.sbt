name := "scala-academy-twitter-akka-streams"

version := "1.0"

scalaVersion := "2.11.4"


resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= Seq(
  "com.typesafe.akka"      %% "akka-actor"            % "2.3.9",
  "com.typesafe.akka"      %% "akka-slf4j"            % "2.3.9",
  "io.spray"               %% "spray-can"             % "1.3.1",
  "io.spray"               %% "spray-client"          % "1.3.1",
  "io.spray"               %% "spray-routing"         % "1.3.1",
  "io.spray"               %% "spray-json"            % "1.3.1",
  "com.typesafe.akka"      %  "akka-stream-experimental_2.11" % "1.0-M2",
  "org.specs2"             %% "specs2"                % "2.4.15"       % "test",
  "io.spray"               %% "spray-testkit"         % "1.3.1"        % "test",
  "com.typesafe.akka"      %% "akka-testkit"          % "2.3.9"        % "test"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)


connectInput in run := true

outputStrategy in run := Some(StdoutOutput)
