package org.hs.flink.jpmml.test

import io.radicalbit.flink.pmml.scala.api.PmmlModel
import io.radicalbit.flink.pmml.scala.models.control.{AddMessage, DelMessage, ServingMessage}
import org.apache.flink.streaming.api.scala._
import org.hs.flink.jpmml.test.model.Appointments
import org.hs.flink.jpmml.test.sources.AppointmentsSource.appointmentSource

import scala.collection.mutable.ListBuffer
import scala.util.Random

object AppsDTreeModelServing {


  private final lazy val idMap = Map(
   "/Users/harishsridhar/Documents/Temp/flink-jpmml/flink-jpmml-test/src/main/resources/DecisionTreeLotte4PC.xml" -> "4897c9f4-5226-43c7-8f2d-f9fd388cf2bc",
    "/Users/harishsridhar/Documents/Temp/flink-jpmml/flink-jpmml-test/src/main/resources/kmeans.xml" -> "4897c9f4-5226-43c7-8f2d-f9fd388cf2bc",
   "/Users/harishsridhar/Documents/Temp/flink-jpmml/flink-jpmml-test/src/main/resources/DecisionTreeLotte6PC.xml" ->  "5f919c52-2ef8-4ff2-94b2-2e64bb85005e"
  )

  private var idset  = scala.collection.mutable.Set[String]();



  def main(args: Array[String]): Unit = {
    implicit val env = StreamExecutionEnvironment.getExecutionEnvironment

    // Define data Stream...
    val appsDataStream : DataStream[Appointments] = appointmentSource(env, None)

    //Create a stream for socket
    val controlStream :DataStream[ServingMessage] = env
      .socketTextStream("localhost", 9999)
      .flatMap(path => {
        var out: scala.collection.mutable.ListBuffer[ServingMessage] = ListBuffer()
        if(idset.contains(idMap.get(path).get)){
          println(s"The model for the id {} will be re-uploaded.",idMap.get(path).get)
          out += (DelMessage(idMap.get(path).get, 1L, System.currentTimeMillis()))
        }
        idset += (idMap.get(path).get)
        out += (AddMessage(idMap.get(path).get, 1L, path, System.currentTimeMillis()))
        println(out)
        out.toList
      }
      )

    /*
     * Make a prediction withSupportStream that represents the stream from the socket
     * evaluate the model with model upload in ControlStream
     *
     * */
    val predictions = appsDataStream
      .withSupportStream(controlStream)
      .evaluate { (event: Appointments, model: PmmlModel) =>
        val vectorized = event.toDenseVector
        val prediction = model.predict(vectorized, Some(0.0))
        (event, prediction.value)
      }
    predictions.print()

    env.execute("example")
  }

}
