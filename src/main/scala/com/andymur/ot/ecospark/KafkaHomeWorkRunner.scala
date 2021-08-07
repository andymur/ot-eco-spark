package com.andymur.ot.ecospark

import org.apache.commons.csv.CSVFormat

import java.io.{FileReader, Reader}

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

  val filePathName = "src/main/resources/data/bestsellers_with_categories.csv"
  val in: Reader = new FileReader(filePathName)
  val records = CSVFormat.RFC4180.builder()
    .setHeader("Name,Author,User Rating,Reviews,Price,Year,Genre")
    .setSkipHeaderRecord(true)
    .build().parse(in).getRecords/*.stream()
    .map(record => BookDescription(record.get("Name"), record.get("Author"))) collect Collectors.toList
  */
  println(records)
  // read csv file & serialize into JSON

  // write into the books Apache Kafka topic

  // read it and print the last 5 from each of 3 partitions
}
