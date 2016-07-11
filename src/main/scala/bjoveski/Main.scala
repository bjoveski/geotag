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

  val images = Reader.readImages(photoPath)
  val history = Parser.parseGoogleHistoryFromPath(historyPath)

  images.zipWithIndex.foreach{ case (image, idx) =>
//      println(f"processed $idx%4d images")
    Decorator.addLocationMetadata(image, history)
  }

  println(green("done!"))
  println("######")

}


case class Point(loc: Location, time: DateTime, accuracy: Int)

case class Location(lat: Double, lon: Double)

case class Image(file: File, time: DateTime)


