package bjoveski

import net.liftweb.json._
import org.joda.time.DateTime

/**
  * Created by bojan on 6/16/16.
  */
object Parser {

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

