package bjoveski

import java.io.File

import org.joda.time.{DateTime, Duration, Interval}

import sys.process._

/**
  * Created by bojan on 6/18/16.
  */
object Decorator extends Util {

  // ------
  // public API
  def addLocationMetadata(image: Image, history: LocationHistory) = {
    val (resultOpt, ms) = runAndTime {
      val guessOpt = findPoint(image, history)
      guessOpt.flatMap(guess => addMetadata(image, guess))
    }

    println(s"img=${image.file.getPath}, found=${resultOpt.isDefined}, time=$ms ms")

  }

  // ------
  // internal stuff

  // if the time is within 10 min of the picture location
  private def findPointByTime(image: Image, history: LocationHistory): Option[Point] = {
    val DURATION = 30
    val interval = new Interval(image.time.minusMinutes(DURATION), image.time.plusMinutes(DURATION))
    val neighborhood = history
      .points
      .collect{case point if interval.contains(point.time) => point}
      .sortBy{case point => math.abs(point.time.getMillis - image.time.getMillis)}


    if (neighborhood.nonEmpty) {
      println(f"found location ${new Duration(image.time, neighborhood.head.time).getMillis}%6d ms away from point. neighborhood ${neighborhood.size}")
    }
    neighborhood.headOption
  }

  // returns a find if the locations don't change a lot

  private def findPoint(image: Image, history: LocationHistory): Option[Point] = {
    findPointByTime(image, history)
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


    println(s"$path")
    // execute the commands
    commands.foreach(command => command.!)

    Some(image)
  }

}