package bjoveski

import java.io.File

import org.joda.time.DateTime


/**
  * Created by bojan on 6/21/16.
  */
object Main extends App with Colors {

  require(args.length == 2, "Please provide two arguments, photoPath and historyPath")
  val photoPath = args(0)
  val historyPath = args(1)

  val images = IoUtil.readImages(photoPath)
  val history = Parser.parseGoogleHistoryFromPath(historyPath)

  val results = images.map{ image =>
    Decorator.addLocationMetadata(image, history)
  }

  println(green(s"done! (${results.count(_.isDefined)}/${images.size}) cleaning up..."))

  IoUtil.cleanUp(photoPath, results)

  println("######")

}


case class Point(loc: Location, time: DateTime, accuracy: Int)

case class Location(lat: Double, lon: Double)

case class Image(file: File, time: DateTime)


