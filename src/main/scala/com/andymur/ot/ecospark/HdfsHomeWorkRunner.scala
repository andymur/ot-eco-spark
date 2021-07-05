package com.andymur.ot.ecospark

import org.apache.hadoop.conf._
import org.apache.hadoop.fs._

import java.io.{InputStream, OutputStream}
import java.net.URI
import java.util

/**
 * Scala application which moves all the data from hdfs://stage to hdfs://ods using following rules:
 * structure of directories (date=<date>) must be preserved but all files in each directory must be merged into one
 *
 * Example:
 *  we have directory /stage/date=2020-11-11 with the following files part-0000.csv, part-0001.csv
 *  afterwards there should be directory /ods/date=2020-11-11 with the one file part-0000.csv which has all the date from the two above mentioned files.
 *
 */
// For reference see: https://github.com/ExNexu/hdfs-scala-example/blob/master/src/main/scala/HDFSFileService.scala
object HdfsHomeWorkRunner extends App {

  // check if /ods directory exists and warn if it does
  // check if /stage directory exists and warn if it doesn't
  // create ods directory

  // loop through the directories inside /stage with proper name pattern (date=<ISO date>)
  // for each directory from the source create its counterpart inside the destionation (i.e. /ods)
  // merge files from the source directory into the destination with a name of part-0000.csv

  val conf = new Configuration()
  val fileSystem = FileSystem.get(new URI("hdfs://localhost:9000"), conf)
  try {

    val srcPath = new Path("/stage")

    if (fileSystem.exists(new Path("/ods"))) {
      println("Destination directory already exists!")
      System.exit(0)
    }

    if (!fileSystem.exists(srcPath)) {
      println("Source directory doesn't exist!")
      System.exit(0)
    }

    for (fileStatus <- fileSystem.listStatus(srcPath, new GlobFilter("date=*")).filter(_.isDirectory)) {
      val dirName = fileStatus.getPath.getName
      println("Processing source directory " + dirName)
      val srcDirectoryName = "/stage" + Path.SEPARATOR + dirName
      val srcDirPath = new Path(srcDirectoryName)
      val destDirectoryName = "/ods" + Path.SEPARATOR + dirName
      createDirectory(destDirectoryName)
      val parts : List[Path] = fileSystem.listStatus(srcDirPath, new GlobFilter("part-[0-9][0-9][0-9][0-9].csv*"))
        .sortBy(_.getPath.getName).map(_.getPath).toList
      mergeParts(parts, new Path(destDirectoryName + Path.SEPARATOR + "part-0000.csv"))
      remove(srcDirectoryName)
    }
    println("Successfully completed!")
  } finally {
    if (fileSystem != null) {
      fileSystem.close()
    }
  }

  def createDirectory(directoryName: String): Unit = {
    val directoryPath = new Path(directoryName)
    if (fileSystem.exists(directoryPath)) {
      throw new IllegalArgumentException("Directory already exists!")
    }
    fileSystem.mkdirs(directoryPath)
  }

  def remove(fileName: String): Unit = {
    val filePath = new Path(fileName)
    if (!fileSystem.exists(filePath)) {
      throw new IllegalArgumentException("File or directory doesn't exist!")
    }
    fileSystem.delete(filePath, true)
  }

  def mergeParts(parts: List[Path], resultFilePath: Path): Unit = {
    val fos: OutputStream = fileSystem.create(resultFilePath)
    try {
      for (part <- parts) {
        val fis: InputStream = fileSystem.open(part)
        try {
          val barr = new Array[Byte](1024)
          var bytesRead = fis.read(barr)
          writePortion(bytesRead, barr, fos)
          while (bytesRead > 0) {
            bytesRead = fis.read(barr)
            writePortion(bytesRead, barr, fos)
          }
        } finally {
          fis.close()
        }
      }
    } finally {
      fos.close()
    }
  }

  def writePortion(bytesRead: Int, readData: Array[Byte], out: OutputStream): Unit = {
    if (bytesRead <= 0) {
      return
    }
    out.write(readData, 0, bytesRead)
  }

  // merge to string buffer
  def writePortion(bytesRead: Int, readData: Array[Byte], out: StringBuilder): Unit = {
    if (bytesRead <= 0) {
      return
    }
    out.append(new String(util.Arrays.copyOf(readData, bytesRead), "UTF-8"))
  }
}