package com.andymur.ot.ecospark

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, Month}

import scala.io.Source

object Main extends App {
  val source = Source.fromURL("https://raw.githubusercontent.com/JeffSackmann/tennis_atp/master/atp_matches_2020.csv")
  val lines = source.getLines().toList
  val head :: rest = lines
  val headers: Map[String, Int] = head.split(",").zipWithIndex.toMap

  println("Number of games played by Medvedev: " + numberOfGamesPlayedBy("Daniil Medvedev"))

  // grouping games
  var playsGroupedByDate: Map[Month, Int] = Map()
  for (line <- rest) {
    playsGroupedByDate = playsGroupedByDate + handleLineForDate(line, playsGroupedByDate)
  }

  println("Number of games grouped by months: " + playsGroupedByDate)

  // more functional way
  // https://stackoverflow.com/questions/28234555/how-to-sum-values-and-group-them-by-a-key-value-in-scalas-list-of-map

  val functionalPlaysGroupedByDate = rest.groupMapReduce(extractMonthFromLine)(_ => 1)(_ + _)

  println("Number of games grouped by months (more functional way): " + playsGroupedByDate)


  // get number of tournaments by months

  val numberOfTournamentsGroupedByMonth = rest.groupBy(extractMonthFromLine).view.mapValues(groupMonthlyRecordsByTourneyName).toMap
  println("Number of tournaments grouped by months: " + numberOfTournamentsGroupedByMonth)

  val numberOfTournamentsGroupedByMonthViaCompoundKey = rest.groupMapReduce(extractTourneyNameAndMonth)(_ => 1)(_ + _)
  println("Number of tournaments grouped by months (second solution): " + numberOfTournamentsGroupedByMonth)

  // get number of wins of Medvedev by tournaments

  // TODO: pattern matching with condition

  def numberOfGamesPlayedBy(playerName: String) : Int = {
    rest.count(gamePlayedByFilter(playerName))
  }

  // made HOF https://docs.scala-lang.org/tour/higher-order-functions.html
  private def gamePlayedByFilter(playerName: String) : String => Boolean = {
    line => {
      val tokens = line.split(",")

      val winnerName: String = tokens(headers("winner_name"))
      val loserName: String = tokens(headers("loser_name"))
      loserName == playerName || winnerName == playerName
    }
  }

  def extractTourneyNameAndMonth(line: String): (Month, String) = {
    val tokens = line.split(",")
    val tourneyDate = tokens(headers("tourney_date"))
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val date: LocalDate = LocalDate.parse(tourneyDate, formatter)

    (date.getMonth, tokens(headers("tourney_name")))
  }

  def groupMonthlyRecordsByTourneyName(lines: List[String]): Integer = {
    lines.map(extractTourneyNameFromLine).toSet.size
  }

  def extractTourneyNameFromLine(line: String) : String = {
    val tokens = line.split(",")
    tokens(headers("tourney_name"))
  }

  def extractMonthFromLine(line: String) : Month = {
    val tokens = line.split(",")
    val tourneyDate = tokens(headers("tourney_date"))
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val date: LocalDate = LocalDate.parse(tourneyDate, formatter)
    date.getMonth
  }

  def handleLineForDate(line: String, acc: Map[Month, Int]) : (Month, Int) = {
    val tokens = line.split(",")
    val tourneyDate = tokens(headers("tourney_date"))
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val date: LocalDate = LocalDate.parse(tourneyDate, formatter)
    (date.getMonth, acc.getOrElse(date.getMonth, 0) + 1)
  }
}
