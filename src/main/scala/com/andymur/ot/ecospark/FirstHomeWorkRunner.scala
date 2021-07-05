package com.andymur.ot.ecospark

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.parser._
import io.circe.syntax._

import java.io._
import scala.io.Source

/*
 * Load  geographical data from https://raw.githubusercontent.com/mledoze/countries/master/countries.json
 * Among african countries select 10 countries with top area and store the result into json structured as below:
 * [{"name": <official country name>, "capital": <capital name>, "area": <area in sq km>},..]
 *
 * In case there are several capitals for a country choose the first one.
 */
// For reference see: spray: https://github.com/drizham/parse_json
//                    circe: https://edward-huang.com/scala/tech/soft-development/etl/circe/2019/11/28/6-quick-tips-to-parse-json-with-circe/
//                    https://blog.knoldus.com/sorting-in-scala-using-sortedsortby-and-sortwith-function/
object FirstHomeWorkRunner extends App {

  case class Name(official: String)

  // how to name property differently from json data field? i.e. capitals instead of capital
  case class InCountry(area: Double, name: Name, region: String, capital: List[String])

  case class OutCountry(name: String, capital: String, area: Double)

  implicit val nameDecoder: Decoder[Name] = deriveDecoder[Name]
  implicit val nameEncoder: Encoder[Name] = deriveEncoder[Name]

  implicit val inCountryDecoder: Decoder[InCountry] = deriveDecoder[InCountry]
  implicit val inCountryEncoder: Encoder[InCountry] = deriveEncoder[InCountry]

  implicit val outCountryDecoder: Decoder[OutCountry] = deriveDecoder[OutCountry]
  implicit val outCountryEncoder: Encoder[OutCountry] = deriveEncoder[OutCountry]

  implicit val outCountryListDecoder: Decoder[List[OutCountry]] = deriveDecoder[List[OutCountry]]
  implicit val outCountryListEncoder: Encoder[List[OutCountry]] = deriveEncoder[List[OutCountry]]


  val outFileName = args(0)
  val pw = new PrintWriter(new File(outFileName))
  val source = Source.fromURL("https://raw.githubusercontent.com/mledoze/countries/master/countries.json")
  // do we have try with resources?
  try {
    decode[List[InCountry]](source.mkString) match {
      case Right(inCountries) =>
        val top10AfricanCountriesBySquareSize: List[InCountry] = handleInput("Africa")(inCountries)
        val top10AfricanCountriesBySquareSizeJson = top10AfricanCountriesBySquareSize.map(inOutCountryMapper).asJson(outCountryListEncoder)

        println("Top 10 african countries by their square size: " + top10AfricanCountriesBySquareSize)
        println("In JSON format: " + top10AfricanCountriesBySquareSizeJson)

        pw.println(top10AfricanCountriesBySquareSizeJson)
      case Left(ex) => println("Error while decoding json input", ex)
    }
  } finally {
    if (source != null) {
      source.close()
    }
    if (pw != null) {
      pw.close()
    }
  }

  def handleInput(region: String)(countries: List[InCountry]): List[InCountry] = {
    countries.filter(c => c.region == region).sortBy(- _.area).take(10)
  }

  def inOutCountryMapper(inCountry: InCountry) : OutCountry = {
    val first :: _ = inCountry.capital
    OutCountry(inCountry.name.official, first, inCountry.area)
  }
}