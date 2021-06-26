package com.andymur.ot.ecospark
import scala.io.Source
import java.io._

object Games extends App {

  case class Date(year: Int, month: Int, day: Int)
  case class TennisMatch(
                          tourneyName: String,
                          winnerName: String,
                          loserName: String,
                          date: Date)

  def parseHeaders(headers: String) = {
    headers.split(",").zipWithIndex.toMap
  }

  def parseLine(line: String, headers: Map[String, Int]) = {
    val tokens = line.split(",")
    val dateString = tokens(headers("tourney_date"))
    val date = Date(dateString.take(4).toInt, dateString.drop(4).take(2).toInt, dateString.drop(6).toInt)
    TennisMatch(
      tokens(headers("tourney_name")),
      tokens(headers("winner_name")),
      tokens(headers("loser_name")),
      date
    )
  }

  def source = Source.fromURL(
    "https://raw.githubusercontent.com/JeffSackmann/tennis_atp/master/atp_matches_2020.csv"
  )

  val head :: records = source.getLines.toList
  val headers = parseHeaders(head)
  val matches = records.map(parseLine(_, headers))


  def filterByMonth(month: Int)(tennisMatch: TennisMatch): Boolean = {
    tennisMatch.date.month == month
  }

  def playedBy(player: String)(tennisMatch: TennisMatch): Boolean = {
    tennisMatch.winnerName == player || tennisMatch.loserName == player
  }

 def isWinner(player: String)(tennisMatch: TennisMatch): Boolean = {
    tennisMatch.winnerName == player
  }


  def processMatches(player: String, month: Int) = {
    matches.filter(filterByMonth(month))
           .filter(playedBy(player)).map {
              tennisMatch => 
                val tourneyName = tennisMatch.tourneyName
                val opponentName = if (isWinner(player)(tennisMatch)) tennisMatch.loserName else tennisMatch.winnerName
                val winLossFlag = if (isWinner(player)(tennisMatch)) "W" else "L"
                s"$tourneyName,$opponentName,$winLossFlag"
           }
  }

  println(matches.take(5))
  val playerName = args(0)
  val month = args(1).toInt
  val outFileName = args(2)

  val output = processMatches(playerName, month).mkString("\n")
  val pw = new PrintWriter(new File(outFileName))
  pw.println(output)
  pw.close()
}
