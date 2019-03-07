package org.hs.flink.jpmml.test

import io.radicalbit.flink.pmml.scala.api.reader.ModelReader
import org.apache.flink.streaming.api.scala._
import org.hs.flink.jpmml.test.sources.AppointmentsSource.appointmentSource

object AppsDTree {

  def main(args: Array[String]): Unit = {
    implicit val env = StreamExecutionEnvironment.getExecutionEnvironment
    val appsDataStream = appointmentSource(env, None)

    val (inputModel, output) = ("src/main/resources/DecisionTreeLotte4PC.pmml","src/main/resources/kmeans.txt")

    //Load model
    val modelReader = ModelReader(inputModel)

    //appsDataStream.print()
    //Using evaluate operator

    val prediction = appsDataStream.evaluate(modelReader) {
      //Iris data and modelReader instance
      case (event, model) =>
        val vectorized = event.toDenseVector
        val prediction = model.predict(vectorized, Some(0.0))

        (event, prediction)
    }

    prediction.print()

    env.execute("example")
  }
}
