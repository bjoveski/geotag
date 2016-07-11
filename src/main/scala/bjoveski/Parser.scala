package bjoveski

import java.io.File

import net.liftweb.json._
import org.joda.time.{DateTime, Duration, Interval}

import scala.io.Source

/**
  * Created by bojan on 6/16/16.
  */
object Parser extends Util with Colors {

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

    val (extracted, ms) = runAndTime {
      val ast = parse(s)

      val locations = (ast \ "locations").children

      locations.map { location =>

        val ms = location \ "timestampMs"
        val lat = location \ "latitudeE7"
        val lon = location \ "longitudeE7"
        val acc = location \ "accuracy"

        // need to multiply by 10^-7 in order to normalize
        val loc = Location(lat.extract[Int] * 0.0000001, lon.extract[Int] * 0.0000001)
        val time = new DateTime(ms.extract[String].toLong)

        Point(loc, time, acc.extract[Int])
      }
    }
    println(yellow(s"parsed ${extracted.size} location points in ${Duration.millis(ms).toString}"))

    val res = LocationHistory(extracted)

//    locationStats(res)

    res
  }

  private def locationStats(l: LocationHistory) {
    var histogram = Map[Long, Long]()

    // the points are ordered in desc order
    l.points.sliding(2).foreach{case Seq(second, first) =>
      val interval = new Interval(first.time, second.time)
      val duration = interval.toDurationMillis

      // 5 min buckets
      val k = duration / (1000 * 60 * 5)
      val v = histogram.getOrElse(k, 0L)
      histogram = histogram + (k -> (v + 1))
    }


    histogram.toSeq.sortBy(_._1).foreach{case (k, v) =>
      println(f"${k * 5}%6d -> $v%9d")
    }
  }
}

case class LocationHistory(points: Seq[Point])

