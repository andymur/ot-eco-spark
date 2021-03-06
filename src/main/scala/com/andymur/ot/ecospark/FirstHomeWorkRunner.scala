package com.andymur.ot.ecospark

import spray.json._

import scala.io.Source

/*
 * Load  geographical data from https://raw.githubusercontent.com/mledoze/countries/master/countries.json
 * Among african countries select 10 countries with top area and store the result into json structured as below:
 * [{"name": <official country name>, "capital": <capital name>, "area": <area in sq km>},..]
 *
 * In case there are several capitals for a country choose the first one.
 */
// For reference see: https://github.com/drizham/parse_json
//                    https://blog.knoldus.com/sorting-in-scala-using-sortedsortby-and-sortwith-function/
object FirstHomeWorkRunner extends App {

  case class Name(official: String) {}
  // how to name property differently from json data field? i.e. capitals instead of capital
  case class InCountry(area: Double, name: Name, region: String, capital: List[String]) {}

  case class OutCountry(name: String, capital: String, area: Double)

  object CountryJsonProtocol extends DefaultJsonProtocol {
    // not sure what implicit does mean
    implicit val nameFormat: RootJsonFormat[Name] = jsonFormat1(Name)
    implicit val inCountryListFormat: JsonFormat[InCountry] = jsonFormat4(InCountry)
    implicit val outCountryListFormat: JsonFormat[OutCountry] = jsonFormat3(OutCountry)
  }

  // why is it here?..
  import CountryJsonProtocol._

  val source = Source.fromURL("https://raw.githubusercontent.com/mledoze/countries/master/countries.json")
  try {
    val jsonAst = JsonParser(source.mkString)

    val top10AfricanCountriesBySquareSize: List[InCountry] = handleInput(jsonAst.convertTo[List[InCountry]])

    println("Top 10 african countries by their square size: " + top10AfricanCountriesBySquareSize)
    println("In JSON format: " + top10AfricanCountriesBySquareSize.map(inOutCountryMapper).toJson)
  } finally {
    if (source != null) {
      source.close()
    }
  }

  def handleInput(countries: List[InCountry]): List[InCountry] = {
    countries.filter(c => c.region == "Africa").sortBy(_.area).reverse.take(10)
  }

  def inOutCountryMapper(inCountry: InCountry) : OutCountry = {
    val first :: _ = inCountry.capital
    OutCountry(inCountry.name.official, first, inCountry.area)
  }
}
