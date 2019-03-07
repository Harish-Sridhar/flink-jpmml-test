package org.hs.flink.jpmml.test.model

import io.radicalbit.flink.pmml.scala.models.input.BaseEvent
import org.apache.flink.ml.math.DenseVector


case class Appointments(modelId: String,
                        day: Double,
                        month: Double,
                        zipcode: Double,
                        amount: Double,
                        occurredOn: Long)
  extends BaseEvent {
  def toDenseVector = DenseVector(day, month, zipcode, amount)
}
