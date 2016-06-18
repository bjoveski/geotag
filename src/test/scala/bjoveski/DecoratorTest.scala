package bjoveski

import java.io.File

import org.joda.time.DateTime
import org.scalatest._
/**
  * Created by bojan on 6/18/16.
  */
class DecoratorTest extends FlatSpec with Matchers {

  "A Decorator" should "invoke exiftools properly" in {
    val url = getClass.getResource("/p1.jpg")
    val file = new File(url.getPath)

    val image = Image(file, DateTime.now())

    val loc = Location(41.952809, 21.297786)
    val point = Point(loc, DateTime.now(), 100)

    Decorator.addMetadata(image, point)

  }


}
