name := "flink-jpmml-test"

version := "0.1"

scalaVersion := "2.11.11"

resolvers ++= Seq(
  "Radicalbit Releases" at "https://tools.radicalbit.io/artifactory/public-release/",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

libraryDependencies ++= Seq("io.radicalbit" %% "flink-jpmml-scala" % "0.6.3",
  "org.apache.flink" %% "flink-streaming-scala" % "1.7.2",
  "org.apache.flink" %% "flink-scala" % "1.7.2",
  "org.jpmml" % "pmml-evaluator" % "1.3.9",
  "org.glassfish.jaxb" % "jaxb-runtime" % "2.3.2",
  "org.apache.kafka" % "kafka-clients" % "2.1.0",
  "com.storm-enroute" %% "scalameter" % "0.17"
  //, "org.slf4j" % "slf4j-simple" % "1.7.9"
)

mainClass in assembly := Some("org.hs.flink.jpmml.test.AppsDTree")

testFrameworks += new TestFramework(
  "org.scalameter.ScalaMeterFramework")

logBuffered := false

parallelExecution in Test := false

