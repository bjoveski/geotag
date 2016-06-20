package bjoveski

import java.io.File

import org.joda.time.DateTime

import sys.process._

/**
  * Created by bojan on 6/18/16.
  */
case class Point(loc: Location, time: DateTime, accuracy: Int)

case class Location(lat: Double, lon: Double)

case class Image(file: File, time: DateTime)



object Decorator {

  // ------
  // public API
  def addLocationMetadata(image: Image, history: LocationHistory) = {

    val guessOpt = findPoint(image, history)

    val resultOpt = guessOpt.flatMap(guess => addMetadata(image, guess))


  }

  // ------
  // internal stuff

  private def findPoint(image: Image, history: LocationHistory): Option[Point] = {
    history
      .points
      .collectFirst{case point: Point if point.time.isAfter(image.time) && point.time.isBefore(image.time.plusMinutes(10)) => point}
  }

  /*
      exiftool -GPSLongitude="22 deg 21' 43.28\" E" p1.jpg
      exiftool -GPSLatitude="40 deg 5' 15.50\" N" p1.jpg
      exiftool -GPSLongitudeRef=East p1.jpg
   */
  private[bjoveski] def addMetadata(image: Image, guess: Point): Option[Image] = {
    val path = image.file.getAbsolutePath

    val lon = s"""exiftool -GPSLongitude="${guess.loc.lon.toString}" $path"""
    val lonRef = s"exiftool -GPSLongitudeRef=East $path"

    val lat = s"""exiftool -GPSLatitude="${guess.loc.lat.toString}" $path"""
    val latRef = s"exiftool -GPSLatitudeRef=North $path"

    val commands = Seq(lonRef, lon, latRef, lat)

//    println(lon)
//    lonRef.!
//    println("DOOOOONE")
//    lon.!

    println(s"$path")
    // execute the commands
    commands.foreach(command => command.!)

    Some(image)
  }

  //   private def extractMetadata(point: Point):

}