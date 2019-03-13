package org.hs.flink.jpmml.test.sources

import java.util.UUID

import io.radicalbit.flink.pmml.scala.models.core.ModelId
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.apache.flink.streaming.api.scala._
import org.hs.flink.jpmml.test.model.Appointments

import scala.util.Random

object AppointmentsSource {

  private final lazy val idSet = Set(
    "4897c9f4-5226-43c7-8f2d-f9fd388cf2bc",
    "5f919c52-2ef8-4ff2-94b2-2e64bb85005e"
  )

  private final val NumberOfParameters = 4
  private final lazy val RandomGenerator = scala.util.Random
  private final val RandomMin = 0.2
  private final val RandomMax = 6.0

  private final def truncateDouble(n: Double) = (math floor n * 10) / 10

  private final val anomalylimits = Map(1 -> 0.8, 100 -> 0.1, 300 -> 0.1 )
  final def anomaly: Int = {
    val p = scala.util.Random.nextDouble
    val it = anomalylimits.iterator
    var accum = 0.0
    while (it.hasNext) {
      val (item, itemProb) = it.next
      accum += itemProb
      if (accum >= p)
        return item  // return so that we don't have to search through the whole distribution
    }
    return 1
  }

  @throws(classOf[Exception])
  def appointmentSource(env: StreamExecutionEnvironment, availableModelIdOp: Option[Seq[String]]): DataStream[Appointments] = {
    env.addSource(
      (sc: SourceContext[Appointments]) => {
      while (true) {
        def randomPostcode: Float =  (1000 + RandomGenerator.nextInt((9999 - 1000)+1)).toFloat
        def randomAmount : Float = (anomaly  + RandomGenerator.nextInt(6)).toFloat
        def randomDay: Float = (1 + RandomGenerator.nextInt(28)).toFloat
        def randomMonth: Float = (1 + RandomGenerator.nextInt(12)).toFloat

        val appointments =
          Appointments(idSet.toVector(0) + ModelId.separatorSymbol + "1",
               randomDay,
               randomMonth,
               randomPostcode,
               randomAmount,
               System.currentTimeMillis())
        sc.collect(appointments)
        Thread.sleep(10000)
      }
    }
    )
  }

}
