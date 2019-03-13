package org.hs.flink.jpmml.test.model

import io.radicalbit.flink.pmml.scala.models.input.BaseEvent
import org.apache.flink.ml.math.DenseVector


case class Appointments(modelId: String,
                        day: Float,
                        month: Float,
                        zipcode: Float,
                        amount: Float,
                        occurredOn: Long)
  extends BaseEvent {
  def toDenseVector = DenseVector(amount,zipcode,day,month)
}
