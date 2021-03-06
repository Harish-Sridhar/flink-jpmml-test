/*
 * Copyright (C) 2017  Radicalbit
 *
 * This file is part of flink-JPMML
 *
 * flink-JPMML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * flink-JPMML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with flink-JPMML.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.hs.flink.jpmml.test.sources

import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.apache.flink.streaming.api.scala._
import org.hs.flink.jpmml.test.model.Iris

import scala.util.Random

object IrisSource {
  private final val NumberOfParameters = 4
  private final lazy val RandomGenerator = scala.util.Random
  private final val RandomMin = 0.2
  private final val RandomMax = 6.0

  private final def truncateDouble(n: Double) = (math floor n * 10) / 10

  @throws(classOf[Exception])
  def irisSource(env: StreamExecutionEnvironment, availableModelIdOp: Option[Seq[String]]): DataStream[Iris] = {
    val availableModelId = availableModelIdOp.getOrElse(Seq.empty[String])
    env.addSource((sc: SourceContext[Iris]) => {
      while (true) {
        def randomVal = RandomMin + (RandomMax - RandomMin) * RandomGenerator.nextDouble()
        val dataForIris = Seq.fill(NumberOfParameters)(truncateDouble(randomVal))
        val iris =
          Iris(Random.nextInt(2).toString,
               dataForIris(0),
               dataForIris(1),
               dataForIris(2),
               dataForIris(3),
               System.currentTimeMillis())
        sc.collect(iris)
        Thread.sleep(1000)
      }
    })

  }

}
