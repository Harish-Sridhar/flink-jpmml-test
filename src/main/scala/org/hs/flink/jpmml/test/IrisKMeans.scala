package org.hs.flink.jpmml.test


import io.radicalbit.flink.pmml.scala.api.reader.ModelReader
import org.apache.flink.streaming.api.scala._
import org.hs.flink.jpmml.test.sources.IrisSource.irisSource

object IrisKMeans {

  def main(args: Array[String]): Unit = {
    implicit val env = StreamExecutionEnvironment.getExecutionEnvironment
    val irisDataStream = irisSource(env, None)

    val (inputModel, output) = ("src/main/resources/kmeans.xml","src/main/resources/kmeans.txt")

    //Load model
    val modelReader = ModelReader(inputModel)

    //irisDataStream.print()
    //Using evaluate operator

    val prediction = irisDataStream.evaluate(modelReader) {
      //Iris data and modelReader instance
      case (event, model) =>
        val vectorized = event.toVector
        val prediction = model.predict(vectorized, Some(0.0))
        (event, prediction.value.getOrElse(-1.0))
    }

    prediction.print()

    env.execute("example")
  }
}
