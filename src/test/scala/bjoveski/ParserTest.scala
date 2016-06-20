package bjoveski

import org.scalatest._

/**
  * Created by bojan on 6/20/16.
  */
class ParserTest extends FlatSpec with Matchers {

  val example = """
    |{
    |  "locations" : [ {
    |    "timestampMs" : "1455341576327",
    |    "latitudeE7" : -338155191,
    |    "longitudeE7" : 1511768385,
    |    "accuracy" : 29,
    |    "activitys" : [ {
    |      "timestampMs" : "1455341580599",
    |      "activities" : [ {
    |        "type" : "still",
    |        "confidence" : 100
    |      } ]
    |    } ]
    |  }, {
    |    "timestampMs" : "1455341326702",
    |    "latitudeE7" : -338131128,
    |    "longitudeE7" : 1511783735,
    |    "accuracy" : 600
    |  }
    |  ]
    |}
  """.stripMargin


  "A parser" should "parse an example" in {

    val history = Parser.parseGoogleHistory(example)

    history.points.size should be(2)
  }

}
