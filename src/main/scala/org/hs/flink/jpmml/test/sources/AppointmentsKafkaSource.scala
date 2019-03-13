package org.hs.flink.jpmml.test.sources
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord

import java.util.Properties

class AppointmentsKafkaSource {

  // Setting up properties for producer...
  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("acks", "all")
  props.put("delivery.timeout.ms", "30000")
  props.put("batch.size", "6384")
  props.put("linger.ms", "1")
  props.put("buffer.memory", "33554432")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("group.id", "cons1")

  val producer = new KafkaProducer[String, String](props)
  val topic = "Appointments"

  while (true) {
    producer.send(new ProducerRecord[String, String](topic, "K" ,  "V"))
  }

}
