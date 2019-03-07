name := "flink-jpmml-test"

version := "0.1"

scalaVersion := "2.11.11"

resolvers ++= Seq(
  "Radicalbit Releases" at "https://tools.radicalbit.io/artifactory/public-release/"
)

libraryDependencies ++= Seq("io.radicalbit" %% "flink-jpmml-scala" % "0.6.3",
  "org.apache.flink" %% "flink-streaming-scala" % "1.7.2",
  "org.apache.flink" %% "flink-scala" % "1.7.2",
  "org.jpmml" % "pmml-evaluator" % "1.3.9",
  "org.glassfish.jaxb" % "jaxb-runtime" % "2.3.2"
)