package com.andymur.ot.ecospark

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.jawn.decode
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder}
import org.apache.commons.csv.{CSVFormat, CSVRecord}
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.io.FileReader
import java.time.Duration
import java.util.{Collections, Properties}
import scala.collection.mutable
import scala.jdk.CollectionConverters._
import scala.util.Using

object KafkaHomeWorkRunner extends App {

  case class BookDescription(
    bookTitle: String,
    author: String,
    userRating: Double,
    numberOfReviews: Int,
    price: Double,
    year: Int,
    genre: String
  )

  implicit val bookDescriptionDecoder: Decoder[BookDescription] = deriveDecoder[BookDescription]
  implicit val bookDescriptionEncoder: Encoder[BookDescription] = deriveEncoder[BookDescription]

  implicit val bookDescriptionListDecoder: Decoder[List[BookDescription]] = deriveDecoder[List[BookDescription]]
  implicit val bookDescriptionListEncoder: Encoder[List[BookDescription]] = deriveEncoder[List[BookDescription]]

  // First part: read csv file & serialize into JSON
  val filePathName = "src/main/resources/data/bestsellers_with_categories.csv"
  var csvRecords: List[CSVRecord] = List()
  Using(new FileReader(filePathName)) {
    in => {
      // read the content of the file
       csvRecords = CSVFormat.RFC4180.builder()
        .setHeader("Name,Author,User Rating,Reviews,Price,Year,Genre")
        .setSkipHeaderRecord(true)
        .build().parse(in).getRecords.asScala.toList
    }
  }

  // convert it to the list of scala case class objects
  val bookDescriptions: List[BookDescription] = csvRecords.map(rec
    =>
    BookDescription(rec.get(0), rec.get(1), rec.get(2).toDouble, rec.get(3).toInt, rec.get(4).toDouble,
      rec.get(5).toInt, rec.get(6))
  )

  // prepare content for kafka
  val contentForKafka: List[String] = bookDescriptions.map(r => r.asJson(bookDescriptionEncoder).toString())

  // Second part: write the content into the books Apache Kafka topics
  val topicName = "books"
  val baseConfig: Properties = new Properties()
  baseConfig.put("bootstrap.servers", "localhost:29092")
  baseConfig.put("key.serializer", Class.forName("org.apache.kafka.common.serialization.StringSerializer"))
  baseConfig.put("value.serializer", Class.forName("org.apache.kafka.common.serialization.StringSerializer"))

  // we need to know how many records we're supposed to write
  val recWritten = contentForKafka.size
  Using (new KafkaProducer[String, String](baseConfig)){
    producer => {
      contentForKafka.foreach(rec => producer.send(new ProducerRecord[String, String](topicName, rec))) // use round robin partitioning
      producer.flush()
      println("Records written by producer: " + recWritten)
    }
  }

  // Third part: read it and print the last 5 from each of 3 partitions

  val maxNumberOfRecordsPerPartition = 5
  // here we store our last records per partition (not more than maxNumberOfRecordsPerPartition value for each partition)
  val partitionMap: mutable.Map[Int, mutable.ArrayDeque[String]] = mutable.Map()
  var recordProcessed = 0

  val consumerConfig: Properties = new Properties()
  consumerConfig.put("bootstrap.servers", "localhost:29092")
  consumerConfig.put("key.deserializer", Class.forName("org.apache.kafka.common.serialization.StringDeserializer"))
  consumerConfig.put("value.deserializer", Class.forName("org.apache.kafka.common.serialization.StringDeserializer"))
  consumerConfig.put("group.id", "otus")
  // set options in order to read from the beginning each time (this is more for testing purpose)
  consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
  consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  Using (new KafkaConsumer[String, String](consumerConfig)) {
    consumer => {
      consumer.subscribe(Collections.singleton(topicName))
      while (recordProcessed < recWritten) {
        // read the portion of data and do the magic...remember we shouldn't store more than maxNumberOfRecordsPerPartition per 1 partition
        consumer.poll(Duration.ofSeconds(1L)).forEach(
          rec => {
            recordProcessed += 1
            val partition = rec.partition()
            partitionMap get partition match {
              case None =>
                val deq: mutable.ArrayDeque[String] = mutable.ArrayDeque()
                partitionMap += (partition -> deq)
                deq.append(rec.value())
              case Some(deq) =>
                if (deq.size >= maxNumberOfRecordsPerPartition) {
                  deq.removeHead()
                }
                deq.append(rec.value())
            }
          }
        )
      }
    }
  }

  println("Records processed by consumer: " + recordProcessed)
  partitionMap.foreach(
    e => {
      println("Partition #" + e._1 + ":")
      e._2.foreach(rec => {
        decode[BookDescription](rec) match {
          case Right(bookDescription: BookDescription) => println(bookDescription)
          case Left(ex) => println("Error while decoding json", ex)
        }
      })
    }
  )
  println("Fin!")
}
