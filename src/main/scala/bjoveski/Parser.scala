package bjoveski

import java.io.File

import net.liftweb.json._
import org.joda.time.DateTime
import scala.io.Source

/**
  * Created by bojan on 6/16/16.
  */
object Parser extends Colors {

  def parseGoogleHistoryFromPath(path: String): LocationHistory = {
    try {
      val folder = new File(path)
      val historyFolder = folder.listFiles().find(f => f.getName == "Location History").get
      val jsonFile = historyFolder.listFiles().find(f => f.getName == "LocationHistory.json").get

      val lines = Source.fromFile(jsonFile).getLines().mkString("\n")

      parseGoogleHistory(lines)
    } catch {
      case e: Exception => {
        println(red(s"couldn't parse $path properly"))
        e.printStackTrace()
        throw e
      }
    }
  }


  def parseGoogleHistory(s: String): LocationHistory = {

    implicit val formats = DefaultFormats

    val ast = parse(s)

    val locations = (ast \ "locations").children

    val extracted = locations.map { location =>

      val ms = location \ "timestampMs"
      val lat = location \ "latitudeE7"
      val lon = location \ "longitudeE7"
      val acc = location \ "accuracy"

      val loc = Location(lat.extract[Int], lon.extract[Int])
      val time = new DateTime(ms.extract[String].toLong)

      Point(loc, time, acc.extract[Int])
    }

    LocationHistory(extracted)
  }
}

case class LocationHistory(points: Seq[Point])

