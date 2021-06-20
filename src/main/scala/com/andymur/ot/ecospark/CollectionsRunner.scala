package com.andymur.ot.ecospark

object CollectionsRunner extends App {
  val l: List[String] = List("Hello", "Hi", "Servus")

  val foldRes = l.fold("") {
    (a, b) => a + b
  }

  l.foreach(println)
  println(foldRes)

  val myList: List[String] = List("France", "France", "England")
  println(myList.zip(myList.map(_ => 1)))

  // https://stackoverflow.com/questions/28234555/how-to-sum-values-and-group-them-by-a-key-value-in-scalas-list-of-map

  // grouping by using fold
  val groupedList  = myList.foldLeft(Map[String, Int]()) {
    (acc, el) => {
      acc + (el -> (acc.getOrElse(el, 0) + 1))
    }
  }

  println(groupedList) // Map(France -> 2, England -> 1)

  // grouping by using groupBy & mapValues
  println(myList.groupBy(el => el)) // HashMap(France -> List(France, France), England -> List(England))
  println(myList.groupBy(el => el).view.mapValues(l => l.size).toMap) // Map(France -> 2, England -> 1)

  // grouping by groupMapReduce (group -> map -> reduce)
  print(myList.groupMapReduce(el => el)(_ => 1)(_ + _)) // Map(France -> 2, England -> 1)
}
